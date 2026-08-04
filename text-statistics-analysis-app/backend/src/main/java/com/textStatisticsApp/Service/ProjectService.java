package com.textStatisticsApp.Service;

import java.util.List;

import com.textStatisticsApp.Dao.ProjectDao;
import com.textStatisticsApp.Dao.ProjectDocumentDao;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private final ProjectDocumentDao projectDocumentDao;
    private final ProjectDao projectDao;

    public ProjectService(ProjectDocumentDao projectDocumentDao, ProjectDao projectDao) {
        this.projectDocumentDao = projectDocumentDao;
        this.projectDao = projectDao;
    }

    public List<ProjectDao.Project> getProjects() {
        return projectDao.getProjects();
    }

    public ProjectDao.Project getProjectById(long projectId) {
        return projectDao.getProjectById(projectId);
    }

    public List<ProjectDocumentDao.ProjectDocument> getProjectDocuments(long projectId) {
        return projectDocumentDao.getProjectDocumentsByProjectId(projectId);
    }

    public int createProject(String name) {
        return projectDao.addProject(name);
    }

    public int deleteProject(long projectId) {
        return projectDao.deleteProjectById(projectId);
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
