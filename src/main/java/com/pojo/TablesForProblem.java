package com.pojo;

public class TablesForProblem extends TablesForProblemKey {
    private Integer access;

    public TablesForProblem(String userTableName, Integer proId, Integer access) {
        super(userTableName, proId);
        this.access = access;
    }

    public TablesForProblem() {
        super();
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
    }
}