package com.pojo;

import java.util.Date;

public class Exam {
    private Integer id;

    private Date startTime;

    private Date endTime;

    private Integer fullScore;

    private Integer passScore;

    private String name;

    public Exam(Integer id, Date startTime, Date endTime, Integer fullScore, Integer passScore, String name) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fullScore = fullScore;
        this.passScore = passScore;
        this.name = name;
    }

    public Exam() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getFullScore() {
        return fullScore;
    }

    public void setFullScore(Integer fullScore) {
        this.fullScore = fullScore;
    }

    public Integer getPassScore() {
        return passScore;
    }

    public void setPassScore(Integer passScore) {
        this.passScore = passScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}