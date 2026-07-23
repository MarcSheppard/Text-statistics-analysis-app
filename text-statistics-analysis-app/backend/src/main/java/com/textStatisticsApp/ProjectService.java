package com.textStatisticsApp;

import com.textStatisticsApp.Dao.DocumentDao;
import com.textStatisticsApp.Dao.ProjectDao;
import com.textStatisticsApp.Dao.ProjectDocumentDao;
import com.textStatisticsApp.Dao.SentenceDao;

public class ProjectService {
    private SentenceDao sentenceDao;
    private DocumentDao documentDao;
    private ProjectDocumentDao projectDocumentDao;
    private ProjectDao projectDao;

    public ProjectService(SentenceDao sentenceDao, DocumentDao documentDao, ProjectDocumentDao projectDocumentDao, ProjectDao projectDao) {
        this.sentenceDao = sentenceDao;
        this.documentDao = documentDao;
        this.projectDocumentDao = projectDocumentDao;
        this.projectDao = projectDao;
    }

    public void createProject(String name) {
        projectDao.addProject(name);
    }

    public void addDocumentToProject(long projectId, long documentId) {
        projectDocumentDao.addProjectDocument(projectId, documentId);
    }

    public void removeDocumentFromProject(long projectId, long documentId) {
        projectDocumentDao.deleteProjectDocument(projectId, documentId);
    }

    public void removeAllDocumentsFromProject(long projectId) {
        projectDocumentDao.deleteProjectDocumentByProjectId(projectId);
    }
}
