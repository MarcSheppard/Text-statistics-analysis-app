package com.textStatisticsApp.Dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class MockProjectDao extends ProjectDao {
    private List<Project> projectTable;

    public MockProjectDao() {
        projectTable = new ArrayList<>();
    }

    @Override
    public int addProject(final String name) {
        projectTable.add(new Project(projectTable.size(), name));
        return 1;
    }

    @Override
    public List<Project> getProjects() { return projectTable; }

    @Override
    public Project getProjectById(final long id) {
        return projectTable.get((int)id);
    }

    @Override
    public int deleteProjectById(final long id) {
        projectTable.remove((int)id);
        return 1;
    }

    @Override
    public int clearAll() {
        projectTable.clear();
        return 1;
    }
}
