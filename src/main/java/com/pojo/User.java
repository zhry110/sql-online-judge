package com.pojo;

public class User {
    private Integer id;

    private String username;

    private String passwd;

    private String name;

    private boolean isJudgeing;

    public User(Integer id, String username, String passwd, String name) {
        this.id = id;
        this.username = username;
        this.passwd = passwd;
        this.name = name;
        isJudgeing = false;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public void setJudgeing(boolean judgeing) {
        isJudgeing = judgeing;
    }

    public boolean isJudgeing() {
        return isJudgeing;
    }
}