package com.textStatisticsApp.Dao;

public abstract class DocumentDao {
    public DocumentDao() {}

    public abstract long addDocument(String name);

    public abstract Document getDocumentById(long id);

    public abstract int deleteDocumentById(long id);

    public abstract int clearAll();

    public record Document(long id, String name) {}
}
