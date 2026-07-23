package com.textStatisticsApp.Dao;

public abstract class ProjectDao {
    public ProjectDao() {}

    public abstract long addProject(String name);

    public abstract Project getProjectById(long id);

    public abstract int deleteProjectById(long id);

    public abstract int clearAll();

    public record Project(long id, String name) {}
}
