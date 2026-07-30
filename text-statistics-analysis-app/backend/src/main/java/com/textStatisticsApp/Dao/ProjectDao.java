package com.textStatisticsApp.Dao;

import java.util.List;

public abstract class ProjectDao {
    public ProjectDao() {}

    public abstract int addProject(String name);

    public abstract List<Project> getProjects();

    public abstract Project getProjectById(long id);

    public abstract int deleteProjectById(long id);

    public abstract int clearAll();

    public record Project(long id, String name) {}
}
