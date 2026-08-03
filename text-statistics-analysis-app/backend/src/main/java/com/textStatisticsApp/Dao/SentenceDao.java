package com.textStatisticsApp.Dao;

import java.util.List;

public abstract class SentenceDao  {

    public SentenceDao() {}

    public abstract int addSentence(DocumentSentenceInput sentence);
    public abstract int addSentences(List<DocumentSentenceInput> sentences);

    public abstract List<DocumentSentence> getSentencesByRegex(String regex, List<Long> documentIds);
    public abstract DocumentSentence getSentenceById(long id);

    public abstract int deleteSentenceById(long id);
    public abstract int deleteSentencesByDocumentId(long documentId);

    public abstract int deleteSentences();

    public record DocumentSentence(long sentenceId, long documentId, String sentence) {}
    public record DocumentSentenceInput(long documentId, String sentence) {}
}
