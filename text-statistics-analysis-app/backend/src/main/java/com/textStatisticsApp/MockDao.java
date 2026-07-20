package com.textStatisticsApp;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.min;

import org.springframework.web.bind.annotation.GetMapping;

public class MockDao extends Dao
{
    private List<DocumentSentence> textSamples;
    private int num_documents;

    public MockDao() {
        textSamples = new ArrayList<>();
        num_documents = 0;
    }

    public int addDocument(String name, List<String> sentences) {
        for(String sentence : sentences) {
            textSamples.add(new DocumentSentence(textSamples.size(), num_documents, sentence));
        }
        num_documents++;
        return 1;
    }

    public List<SearchResult> getSnippets(String query)
    {
        final Pattern pattern = Pattern.compile(query);
        final List<SearchResult> results = new ArrayList<>();
        for(DocumentSentence result : textSamples) {
            final List<QueryMark> queryMarks = new ArrayList<>();
            final Matcher matcher = pattern.matcher(result.snippet());
            while(matcher.find())  {
                queryMarks.add(new QueryMark(matcher.start(), matcher.end()));
            }
            if(queryMarks.size() > 0) {
                results.add(new SearchResult(result.id(), result.documentId(), result.snippet(), queryMarks));
            }
        }
        return results;
    }

    public int deleteDocument(long documentId) {
        for(DocumentSentence result : textSamples) {
            if(result.documentId() == documentId) {
                textSamples.remove(result);
            }
        }
        return 1;
    }

    public int clearTextData() {
        textSamples.clear();
        return 1;
    }

    private static List<String> findSentencesContaining(String text, String searchTerm) {
        return Arrays.stream(text.split("[。.!?]"))
                     .filter(sentence ->
                               sentence.toLowerCase().contains(searchTerm.toLowerCase()))
                     .collect(Collectors.toList());
    }
}
