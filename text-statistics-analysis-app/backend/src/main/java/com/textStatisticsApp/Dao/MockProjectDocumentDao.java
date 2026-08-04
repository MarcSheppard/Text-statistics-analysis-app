package com.textStatisticsApp.Dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class MockProjectDocumentDao extends ProjectDocumentDao {
    private List<ProjectDocument> projectDocumentTable;

    public MockProjectDocumentDao() {
        projectDocumentTable = new ArrayList<>();
    }

    @Override
    public int addProjectDocument(final long projectId, final long documentId) {
        // don't do anything if it's already in
        for(ProjectDocument projectDocument : projectDocumentTable) {
            if(projectDocument.projectId() == projectId && projectDocument.documentId() == documentId) {
                return 1;
            }
        }
        projectDocumentTable.add(new ProjectDocument(projectId, documentId));
        return 1;
    }


    @Override
    public List<ProjectDocument> getProjectDocumentsByProjectId(final long projectId) {
        List<ProjectDocument> projectDocuments = new ArrayList<>();
        for(ProjectDocument projectDocument : projectDocumentTable) {
            if(projectDocument.projectId() == projectId) {
                projectDocuments.add(projectDocument);
            }
        }
        return projectDocuments;
    }


    @Override
    public List<ProjectDocument> getProjectDocumentsByDocumentId(final long documentId) {
        List<ProjectDocument> projectDocuments = new ArrayList<>();
        for(ProjectDocument projectDocument : projectDocumentTable) {
            if(projectDocument.projectId() == documentId) {
                projectDocuments.add(projectDocument);
            }
        }
        return projectDocuments;
    }


    @Override
    public int deleteProjectDocumentByProjectId(final long projectId) {
        for(ProjectDocument projectDocument : projectDocumentTable) {
            if(projectDocument.projectId() == projectId) {
                projectDocumentTable.remove(projectDocument);
            }
        }
        return 1;
    }


    @Override
    public int deleteProjectDocumentByDocumentId(final long documentId) {
        projectDocumentTable.removeIf(projectDocument -> projectDocument.documentId() == documentId);
        return 1;
    }


    @Override
    public int deleteProjectDocument(final long projectId, final long documentId) {
        projectDocumentTable.removeIf(projectDocument -> projectDocument.documentId() == documentId &&
                                                           projectDocument.projectId() == projectId);
        return 1;
    }


    @Override
    public int clearAll() {
        projectDocumentTable.clear();
        return 1;
    }
}
