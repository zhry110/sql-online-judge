package com.pojo;

import java.util.Date;

public class Problems {
    private Long id;

    private String title;

    private Integer admin;

    private Integer score;

    private Boolean type;

    private Boolean isUse;

    private Date createTime;

    public Problems(Long id, String title, Integer admin, Integer score, Boolean type, Boolean isUse, Date createTime) {
        this.id = id;
        this.title = title;
        this.admin = admin;
        this.score = score;
        this.type = type;
        this.isUse = isUse;
        this.createTime = createTime;
    }

    public Problems() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Boolean getIsUse() {
        return isUse;
    }

    public void setIsUse(Boolean isUse) {
        this.isUse = isUse;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}