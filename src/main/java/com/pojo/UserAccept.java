package com.pojo;

import java.util.Date;

public class UserAccept {
    private Long id;

    private Integer uid;

    private Date time;

    private Long proId;

    private Boolean correct;

    private String postSql;

    public UserAccept(Long id, Integer uid, Date time, Long proId, Boolean correct, String postSql) {
        this.id = id;
        this.uid = uid;
        this.time = time;
        this.proId = proId;
        this.correct = correct;
        this.postSql = postSql;
    }

    public UserAccept() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public String getPostSql() {
        return postSql;
    }

    public void setPostSql(String postSql) {
        this.postSql = postSql == null ? null : postSql.trim();
    }
}