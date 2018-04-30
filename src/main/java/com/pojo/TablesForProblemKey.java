package com.pojo;

public class TablesForProblemKey {
    private String userTableName;

    private Integer proId;

    public TablesForProblemKey(String userTableName, Integer proId) {
        this.userTableName = userTableName;
        this.proId = proId;
    }

    public TablesForProblemKey() {
        super();
    }

    public String getUserTableName() {
        return userTableName;
    }

    public void setUserTableName(String userTableName) {
        this.userTableName = userTableName == null ? null : userTableName.trim();
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }
}