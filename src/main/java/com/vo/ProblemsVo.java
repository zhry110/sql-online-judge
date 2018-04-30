package com.vo;

public class ProblemsVo {
    private long id;
    private String title;
    private int score;
    private String sourse;
    private String correct;
    private boolean accept;

    public void setId(long id) {
        this.id = id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getCorrect() {
        return correct;
    }

    public void setSourse(String sourse) {
        this.sourse = sourse;
    }

    public String getSourse() {
        return sourse;
    }

    public boolean getAccept()
    {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }
}
