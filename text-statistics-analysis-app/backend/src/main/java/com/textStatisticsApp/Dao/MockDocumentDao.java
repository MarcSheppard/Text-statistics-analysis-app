package com.textStatisticsApp.Dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class MockDocumentDao extends DocumentDao {
    private List<Document> documentTable;
    private long idCount;

    public MockDocumentDao() {
        documentTable = new ArrayList<>();
        idCount = 0;
    }

    @Override
    public long addDocument(final String name) {
        documentTable.add(new Document(idCount, name));
        idCount++;
        return idCount - 1;
    }

    @Override
    public List<Document> getDocuments() {
        return documentTable;
    }

    @Override
    public Document getDocumentById(final long id) {
        int index = 0;
        for(Document document: documentTable) {
            if(document.id() == id) {
                return documentTable.get(index);
            }
            index++;
        }
        return null;
    }

    @Override
    public int deleteDocumentById(final long id) {
        documentTable.removeIf(document -> document.id() == id);
        return 1;
    }

    @Override
    public int deleteDocuments() {
        documentTable.clear();
        return 1;
    }
}
