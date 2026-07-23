package com.textStatisticsApp.Dao;

import java.util.List;

public abstract class ProjectDocumentDao {
    public ProjectDocumentDao() {}

    public abstract int addProjectDocument(long projectId, long documentId);

    public abstract List<ProjectDocument> getProjectDocumentsByProjectId(long projectId);
    public abstract List<ProjectDocument> getProjectDocumentsByDocumentId(long documentId);

    public abstract int deleteProjectDocumentByProjectId(long projectId);
    public abstract int deleteProjectDocumentByDocumentId(long documentId);
    public abstract int deleteProjectDocument(long projectId, long documentId);

    public abstract int clearAll();

    public record ProjectDocument(long projectId, long documentId) {}
}
