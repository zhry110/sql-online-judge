package com.vo;

public class ProblemDetailVo {
    private Long id;
    private String title;
    private Integer score;
    private String description;
    private String table;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public Integer getScore() {
        return score;
    }

    public String getDescription() {
        return description;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }
}
