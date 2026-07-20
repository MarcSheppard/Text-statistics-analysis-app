package com.textStatisticsApp;

import java.util.List;

public abstract class Dao
{
    public Dao() {}

    public abstract int addDocument(String name, List<String> text);

    public abstract List<SearchResult> getSnippets(String query);

    public abstract int deleteDocument(long documentId);

    public abstract int clearTextData();

    public record DocumentSentence(long id, long documentId, String snippet) {}
    public record QueryMark(long start, long end) {}
    public record SearchResult(long id, long documentId, String snippet, List<QueryMark> queryMarks) {}
    public record CountResult(String label, long count) {}
}
