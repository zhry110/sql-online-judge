package com.pojo;

public class Problems {
    private Long id;

    private String title;

    private Integer admin;

    private Integer score;

    private String description;

    public Problems(Long id, String title, Integer admin, Integer score, String description) {
        this.id = id;
        this.title = title;
        this.admin = admin;
        this.score = score;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}