package com.textStatisticsApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.min;

import com.ibm.icu.text.BreakIterator;
import com.textStatisticsApp.Dao.DocumentDao;
import com.textStatisticsApp.Dao.MockDocumentDao;
import com.textStatisticsApp.Dao.MockSentenceDao;
import com.textStatisticsApp.Dao.SentenceDao;

public class SearchService
{
    private SentenceDao sentenceDao;
    private DocumentDao documentDao;
    public SearchService() {
        sentenceDao = new MockSentenceDao();
        documentDao = new MockDocumentDao();
    }

    // takes a regex query and returns a set of data and statistics
    public AnalysisResults getResults(String query) {
        List<SentenceDao.DocumentSentence> sentences = sentenceDao.getSentencesByRegex(query);

        HashSet<Long> documentIds = new HashSet<>();
        List<SearchResult> snippets = new ArrayList<>();
        final Pattern pattern = Pattern.compile(query);
        for(SentenceDao.DocumentSentence sentence : sentences) {
            final List<QueryMark> queryMarks = new ArrayList<>();
            final Matcher matcher = pattern.matcher(sentence.sentence());
            while(matcher.find())  {
                queryMarks.add(new QueryMark(matcher.start(), matcher.end()));
            }
            snippets.add(new SearchResult(sentence.sentenceId(), sentence.documentId(), sentence.sentence(), queryMarks));
            documentIds.add(sentence.documentId());
        }

        GeneralSearchStatistics statistics = new GeneralSearchStatistics(snippets.size(), documentIds.size());
        AnalysisResults results = new AnalysisResults(statistics, snippets, getCharacterCounts(sentences),
                                                      getNgramCounts(sentences, 1),
                                                      getNgramCounts(sentences, 2),
                                                      getNgramCounts(sentences, 3));
        return results;
    }

    public void addDocument(String name, String text) {
        //add document
        long documentId = documentDao.addDocument(name);

        String[] sentenceCandidates = text.split("[。.!?\n]");
        List<SentenceDao.DocumentSentenceInput> sentences = new ArrayList<>();
        for(String sentence: sentenceCandidates) {
            if(!sentence.isBlank()) {
                sentences.add(new SentenceDao.DocumentSentenceInput(documentId, sentence));
            }
        }
        sentenceDao.addSentences(sentences);
    }

    // takes a set of snippets found and counts the appearance of each character in them
    public List<CountResult> getCharacterCounts(List<SentenceDao.DocumentSentence> snippets) {
        final int MAX_LIST_SIZE = 1000;

        List<String> sentences = new ArrayList<>();
        for(SentenceDao.DocumentSentence snippet : snippets) {
            sentences.add(snippet.sentence());
        }
        List<CountResult> counts = sentences.stream()
                                                .flatMapToInt(String::chars)//convert strings to ints
                                                .filter(Character::isLetterOrDigit) //only consider letters and digits
                                                .mapToObj(c -> Character.toLowerCase((char) c)) //convert all to lower case
                                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                                .entrySet()
                                                .stream()
                                                .sorted(Map.Entry.comparingByKey()) // optional
                                                .map(entry -> new CountResult(String.valueOf(entry.getKey()), entry.getValue()))
                                                .toList();
        return counts.subList(0, min(counts.size(), MAX_LIST_SIZE));
    }

    // takes a set of snippets found and counts the appearance of each Ngram of the given order (e.g 1-unigram, 2-bigram)
    public List<CountResult> getNgramCounts(List<SentenceDao.DocumentSentence> snippets, int order) {
        final int MAX_LIST_SIZE = 1000;

        // get counts
        Map<String, Long> counts = new HashMap<>();
        for(SentenceDao.DocumentSentence result : snippets) {
            List<String> words = splitWords(result.sentence());
            for(int i = 0; i < words.size() - order; i++) {
                boolean areAnyWordsBlank = false;
                String ngram = "";
                for(int n=0; n < order ;n++) {
                    areAnyWordsBlank |= words.get(i + n).isBlank();
                    ngram += words.get(i + n) + " ";
                }
                if(areAnyWordsBlank) { continue; }
                ngram.trim();
                counts.merge(ngram, 1L, Long::sum);
            }
        }
        return counts.entrySet()
                     .stream()
                     .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                     .map(count -> new CountResult(count.getKey(), count.getValue()))
                     .toList()
                     .subList(0, min(counts.size(), MAX_LIST_SIZE));
    }

    // takes a sentence as a string and splits it into words in a language agnostic way
    public static List<String> splitWords(String sentence) {
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.ROOT);
        iterator.setText(sentence);

        List<String> words = new ArrayList<>();

        int start = iterator.first();

        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {

            String token = sentence.substring(start, end);

            if (token.codePoints().anyMatch(Character::isLetterOrDigit)) {
                words.add(token);
            }
        }

        return words;
    }

    public record QueryMark(long start, long end) {}
    public record SearchResult(long id, long documentId, String snippet, List<QueryMark> queryMarks) {}
    public record CountResult(String label, long count) {}
    public record GeneralSearchStatistics(int numResults, int numDocuments) {}
    public record AnalysisResults(GeneralSearchStatistics generalStatistics, List<SearchResult> snippets,
                                  List<CountResult> characterCounts, List<CountResult> wordCounts,
                                  List<CountResult> bigramCounts, List<CountResult> trigramCounts) {}
}
