package com.textStatisticsApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.min;

import com.ibm.icu.text.BreakIterator;

public class SearchService
{
    private Dao dao;
    public SearchService() {
        dao = new MockDao();
    }

    // takes a regex query and returns a set of data and statistics
    public AnalysisResults getResults(String query) {
        List<Dao.SearchResult> snippets = dao.getSnippets(query);

        HashSet<Long> documentIds = new HashSet<>();
        for(Dao.SearchResult snippet : snippets) {
            documentIds.add(snippet.documentId());
        }

        GeneralSearchStatistics statistics = new GeneralSearchStatistics(snippets.size(), documentIds.size());
        AnalysisResults results = new AnalysisResults(statistics, snippets, getCharacterCounts(snippets), getNgramCounts(snippets, 1),
                                                      getNgramCounts(snippets, 2), getNgramCounts(snippets, 3));
        return results;
    }

    public void addDocument(String name, String text) {
        String[] sentenceCandidates = text.split("[。.!?\n]");
        List<String> sentences = new ArrayList<>();
        for(String sentence: sentenceCandidates) {
            if(!sentence.isBlank()) {
                sentences.add(sentence);
            }
        }
        dao.addDocument(name, sentences);
    }

    public void clearTextData() {
        dao.clearTextData();
    }

    // takes a set of snippets found and counts the appearance of each character in them
    public List<Dao.CountResult> getCharacterCounts(List<Dao.SearchResult> snippets) {
        final int MAX_LIST_SIZE = 1000;

        List<String> sentences = new ArrayList<>();
        for(Dao.SearchResult snippet : snippets) {
            sentences.add(snippet.snippet());
        }
        List<Dao.CountResult> counts = sentences.stream()
                                                .flatMapToInt(String::chars)//convert strings to ints
                                                .filter(Character::isLetterOrDigit) //only consider letters and digits
                                                .mapToObj(c -> Character.toLowerCase((char) c)) //convert all to lower case
                                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                                .entrySet()
                                                .stream()
                                                .sorted(Map.Entry.comparingByKey()) // optional
                                                .map(entry -> new Dao.CountResult(String.valueOf(entry.getKey()), entry.getValue()))
                                                .toList();
        return counts.subList(0, min(counts.size(), MAX_LIST_SIZE));
    }

    // takes a set of snippets found and counts the appearance of each Ngram of the given order (e.g 1-unigram, 2-bigram)
    public List<Dao.CountResult> getNgramCounts(List<Dao.SearchResult> snippets, int order) {
        final int MAX_LIST_SIZE = 1000;

        // get counts
        Map<String, Long> counts = new HashMap<>();
        for(Dao.SearchResult result : snippets) {
            List<String> words = splitWords(result.snippet());
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
                     .map(count -> new Dao.CountResult(count.getKey(), count.getValue()))
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

    public record GeneralSearchStatistics(int numResults, int numDocuments) {}
    public record AnalysisResults(GeneralSearchStatistics generalStatistics, List<Dao.SearchResult> snippets,
                                  List<Dao.CountResult> characterCounts, List<Dao.CountResult> wordCounts,
                                  List<Dao.CountResult> bigramCounts, List<Dao.CountResult> trigramCounts) {}
}
