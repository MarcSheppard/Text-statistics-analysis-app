package com.textStatisticsApp.Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MockSentenceDao extends SentenceDao
{
    List<DocumentSentence> sentenceTable;

    public MockSentenceDao() {
        sentenceTable = new ArrayList<>();
    }

    public int addSentence(DocumentSentenceInput sentence) {
        DocumentSentence snippet = new DocumentSentence(sentenceTable.size(), sentence.documentId(), sentence.sentence());
        sentenceTable.add(snippet);
        return 1;
    }
    public int addSentences(List<DocumentSentenceInput> sentences) {
        for(DocumentSentenceInput sentence : sentences) {
            DocumentSentence snippet = new DocumentSentence(sentenceTable.size(), sentence.documentId(), sentence.sentence());
            sentenceTable.add(snippet);
        }
        return 1;
    }

    public List<DocumentSentence> getSentencesByRegex(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        final List<DocumentSentence> results = new ArrayList<>();
        for(DocumentSentence result : sentenceTable) {
            final Matcher matcher = pattern.matcher(result.sentence());
            if(matcher.find())  {
                results.add(result);
            }
        }
        return results;
    }

    public DocumentSentence getSentenceById(long id) {
        return sentenceTable.get((int)id);
    }

    public int deleteSentenceById(long id) {
        sentenceTable.remove(id);
        return 1;
    }
    public int deleteSentencesByDocumentId(long documentId) {
        for(DocumentSentence result : sentenceTable) {
            if(result.documentId() == documentId) {
                sentenceTable.remove(result);
            }
        }
        return 1;
    }

    public int clearAll() {
        sentenceTable.clear();
        return 1;
    }
}
