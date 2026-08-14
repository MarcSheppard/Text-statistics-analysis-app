package com.textStatisticsApp.Dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class MockProjectDao extends ProjectDao {
    private List<Project> projectTable;
    private long idCount;

    public MockProjectDao() {
        projectTable = new ArrayList<>();
        idCount = 0;
    }

    @Override
    public int addProject(final String name) {
        projectTable.add(new Project(idCount, name));
        idCount++;
        return 1;
    }

    @Override
    public List<Project> getProjects() { return projectTable; }

    @Override
    public Project getProjectById(final long id) {
        int index = 0;
        for(Project project: projectTable) {
            if(project.id() == id) {
                return projectTable.get(index);
            }
            index++;
        }
        return null;
    }

    @Override
    public int deleteProjectById(final long id) {
        projectTable.removeIf(project -> project.id() == id);
        return 1;
    }

    @Override
    public int clearAll() {
        projectTable.clear();
        return 1;
    }
}
