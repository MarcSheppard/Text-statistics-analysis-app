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
    private long idCount;

    public MockSentenceDao() {
        sentenceTable = new ArrayList<>();
        idCount = 0;
    }

    @Override
    public int addSentence(final DocumentSentenceInput sentence) {
        DocumentSentence snippet = new DocumentSentence(idCount, sentence.documentId(), sentence.sentence());
        sentenceTable.add(snippet);
        idCount++;
        return 1;
    }

    @Override
    public int addSentences(final List<DocumentSentenceInput> sentences) {
        for(DocumentSentenceInput sentence : sentences) {
            DocumentSentence snippet = new DocumentSentence(idCount, sentence.documentId(), sentence.sentence());
            sentenceTable.add(snippet);
            idCount++;
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
        int index = 0;
        for(DocumentSentence sentence: sentenceTable) {
            if(sentence.sentenceId() == id) {
                return sentenceTable.get(index);
            }
            index++;
        }
        return null;
    }

    @Override
    public int deleteSentenceById(final long id) {
        sentenceTable.removeIf(sentence -> sentence.sentenceId() == id);
        return 0;
    }

    @Override
    public int deleteSentencesByDocumentId(final long documentId) {
        sentenceTable.removeIf(sentence -> sentence.documentId() == documentId);
        return 1;
    }

    @Override
    public int deleteSentences() {
        sentenceTable.clear();
        return 1;
    }
}
