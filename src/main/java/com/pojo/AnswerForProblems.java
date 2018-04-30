package com.pojo;

public class AnswerForProblems {
    private Long proId;

    private String answer;

    public AnswerForProblems(Long proId, String answer) {
        this.proId = proId;
        this.answer = answer;
    }

    public AnswerForProblems() {
        super();
    }

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }
}