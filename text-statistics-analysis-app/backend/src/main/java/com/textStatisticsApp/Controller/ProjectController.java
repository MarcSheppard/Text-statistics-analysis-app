package com.textStatisticsApp.Controller;

import java.util.ArrayList;
import java.util.List;

import com.textStatisticsApp.Dao.ProjectDao;
import com.textStatisticsApp.Dao.ProjectDocumentDao;
import com.textStatisticsApp.Service.ProjectService;
import org.apache.catalina.connector.Response;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectController
{
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/getProjects")
    public List<ProjectResponse> getProjects() {
        List<ProjectResponse> response = new ArrayList<>();
        for(ProjectDao.Project project : projectService.getProjects()) {
            response.add(new ProjectResponse(Long.toString(project.id()), project.name()));
        }
        return response;
    }

    @GetMapping("/getProjectById")
    public ProjectDao.Project getProjectById(@RequestParam long projectId) {
        return projectService.getProjectById(projectId);
    }

    @GetMapping("/getProjectDocuments")
    public List<ProjectDocumentResponse> getProjectDocuments(@RequestParam long projectId) {
        List<ProjectDocumentResponse> response = new ArrayList<ProjectDocumentResponse>();
        for(ProjectDocumentDao.ProjectDocument document : projectService.getProjectDocuments(projectId)) {
            response.add(new ProjectDocumentResponse(Long.toString(document.projectId()), Long.toString(document.documentId())));
        }
        return response;
    }

    @PostMapping("/createProject")
    public int createProject(@RequestParam String name) {
        projectService.createProject(name);
        return Response.SC_OK;
    }

    @DeleteMapping("/deleteProject")
    public int deleteProject(@RequestParam long projectId) {
        projectService.deleteProject(projectId);
        return Response.SC_OK;
    }

    @PostMapping("/addDocumentToProject")
    public int addDocumentToProject(@RequestParam long projectId, @RequestParam long documentId) {
        projectService.addDocumentToProject(projectId, documentId);
        return Response.SC_OK;
    }

    @DeleteMapping("/removeDocumentFromProject")
    public int removeDocumentFromProject(@RequestParam long projectId, @RequestParam long documentId) {
        projectService.removeDocumentFromProject(projectId, documentId);
        return Response.SC_OK;
    }

    @PostMapping("/removeAllDocumentsFromProject")
    public int removeAllDocumentsFromProject(@RequestParam long projectId) {
        projectService.removeAllDocumentsFromProject(projectId);
        return Response.SC_OK;
    }

    private record ProjectResponse(String id, String name) {}
    private record ProjectDocumentResponse(String projectId, String documentId) {}
}
