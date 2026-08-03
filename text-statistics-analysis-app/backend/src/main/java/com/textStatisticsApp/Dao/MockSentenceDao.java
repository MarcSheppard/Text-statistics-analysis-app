package com.textStatisticsApp.Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Repository;

@Repository
public class MockSentenceDao extends SentenceDao
{
    private List<DocumentSentence> sentenceTable;

    public MockSentenceDao() {
        sentenceTable = new ArrayList<>();
    }

    @Override
    public int addSentence(final DocumentSentenceInput sentence) {
        DocumentSentence snippet = new DocumentSentence(sentenceTable.size(), sentence.documentId(), sentence.sentence());
        sentenceTable.add(snippet);
        return 1;
    }

    @Override
    public int addSentences(final List<DocumentSentenceInput> sentences) {
        for(DocumentSentenceInput sentence : sentences) {
            DocumentSentence snippet = new DocumentSentence(sentenceTable.size(), sentence.documentId(), sentence.sentence());
            sentenceTable.add(snippet);
        }
        return 1;
    }

    @Override
    public List<DocumentSentence> getSentencesByRegex(final String regex, final List<Long> documentIds) {
        final Pattern pattern = Pattern.compile(regex);
        final List<DocumentSentence> results = new ArrayList<>();
        for(DocumentSentence result : sentenceTable) {
            if(!documentIds.contains(result.documentId())) { continue; }
            final Matcher matcher = pattern.matcher(result.sentence());
            if(matcher.find())  {
                results.add(result);
            }
        }
        return results;
    }

    @Override
    public DocumentSentence getSentenceById(final long id) {
        return sentenceTable.get((int)id);
    }

    @Override
    public int deleteSentenceById(final long id) {
        sentenceTable.remove(id);
        return 1;
    }

    @Override
    public int deleteSentencesByDocumentId(final long documentId) {
        for(DocumentSentence result : sentenceTable) {
            if(result.documentId() == documentId) {
                sentenceTable.remove(result);
            }
        }
        return 1;
    }

    @Override
    public int deleteSentences() {
        sentenceTable.clear();
        return 1;
    }
}
