package com.textStatisticsApp.Dao;

import java.util.ArrayList;
import java.util.List;

public class MockDocumentDao extends DocumentDao {
    private List<Document> documentTable;

    public MockDocumentDao() {
        documentTable = new ArrayList<>();
    }

    @Override
    public long addDocument(final String name) {
        documentTable.add(new Document(documentTable.size(), name));
        return documentTable.size() - 1;
    }

    @Override
    public Document getDocumentById(final long id) {
        return documentTable.get((int)id);
    }

    @Override
    public int deleteDocumentById(final long id) {
        documentTable.remove((int)id);
        return 1;
    }

    @Override
    public int clearAll() {
        documentTable.clear();
        return 1;
    }
}
