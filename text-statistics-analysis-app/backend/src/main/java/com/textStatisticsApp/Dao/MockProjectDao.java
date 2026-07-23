package com.textStatisticsApp.Dao;

import java.util.List;

public class MockProjectDao extends ProjectDao {
    private List<Project> projectTable;

    @Override
    public long addProject(final String name) {
        projectTable.add(new Project(projectTable.size(), name));
        return projectTable.size() - 1;
    }

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
