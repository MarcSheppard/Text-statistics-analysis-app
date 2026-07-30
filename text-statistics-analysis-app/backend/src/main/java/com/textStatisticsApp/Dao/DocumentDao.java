package com.textStatisticsApp.Dao;

import java.util.List;

public abstract class DocumentDao {
    public DocumentDao() {}

    public abstract long addDocument(String name);

    public abstract List<Document> getDocuments();

    public abstract Document getDocumentById(long id);

    public abstract int deleteDocumentById(long id);

    public abstract int deleteDocuments();

    public record Document(long id, String name) {}
}
