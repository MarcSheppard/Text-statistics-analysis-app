package com.textStatisticsApp.Controller;

import java.util.List;

import com.textStatisticsApp.Dao.ProjectDao;
import com.textStatisticsApp.Service.ProjectService;
import org.apache.catalina.connector.Response;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    public List<ProjectDao.Project> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping("/getProjectById")
    public ProjectDao.Project getProjectById(@RequestParam long projectId) {
        return projectService.getProjectById(projectId);
    }

    @PostMapping("/createProject")
    public int createProject(@RequestParam String name) {
        projectService.createProject(name);
        return Response.SC_OK;
    }

    @PostMapping("/deleteProject")
    public int deleteProject(@RequestParam String name) {
        projectService.createProject(name);
        return Response.SC_OK;
    }

    @PostMapping("/addDocumentToProject")
    public int addDocumentToProject(@RequestParam long projectId, @RequestParam long documentId) {
        projectService.addDocumentToProject(projectId, documentId);
        return Response.SC_OK;
    }

    @PostMapping("/removeDocumentFromProject")
    public int removeDocumentFromProject(@RequestParam long projectId, @RequestParam long documentId) {
        projectService.removeDocumentFromProject(projectId, documentId);
        return Response.SC_OK;
    }

    @PostMapping("/removeAllDocumentsFromProject")
    public int removeAllDocumentsFromProject(@RequestParam long projectId) {
        projectService.removeAllDocumentsFromProject(projectId);
        return Response.SC_OK;
    }
}
