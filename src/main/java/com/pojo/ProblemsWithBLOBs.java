package com.pojo;

import java.util.Date;

public class ProblemsWithBLOBs extends Problems {
    private String description;

    private String answer;

    public ProblemsWithBLOBs(Long id, String title, Integer admin, Integer score, Boolean type, Boolean isUse, Date createTime, String description, String answer) {
        super(id, title, admin, score, type, isUse, createTime);
        this.description = description;
        this.answer = answer;
    }

    public ProblemsWithBLOBs() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }
}