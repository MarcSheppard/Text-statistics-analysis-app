package com.textStatisticsApp.Service;

import java.util.ArrayList;
import java.util.List;

import com.textStatisticsApp.Dao.DocumentDao;
import com.textStatisticsApp.Dao.ProjectDocumentDao;
import com.textStatisticsApp.Dao.SentenceDao;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    private SentenceDao sentenceDao;
    private DocumentDao documentDao;
    private ProjectDocumentDao projectDocumentDao;
    public DocumentService(SentenceDao sentenceDao, DocumentDao documentDao, ProjectDocumentDao projectDocumentDao) {
        this.sentenceDao = sentenceDao;
        this.documentDao = documentDao;
        this.projectDocumentDao = projectDocumentDao;
    }

    public List<DocumentDao.Document> getDocuments() {
        return documentDao.getDocuments();
    }

    public DocumentDao.Document getDocumentById(long documentId) {
        return documentDao.getDocumentById(documentId);
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

    public void deleteDocumentById(long id) {
        documentDao.deleteDocumentById(id);
        sentenceDao.deleteSentencesByDocumentId(id);
        projectDocumentDao.deleteProjectDocumentByDocumentId(id);
    }

    public void deleteAllDocuments() {
        documentDao.deleteDocuments();
        sentenceDao.deleteSentences();
        projectDocumentDao.clearAll();
    }
}
