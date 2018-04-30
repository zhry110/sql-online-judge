package com.pojo;

public class ProblemsForExamKey {
    private Integer proId;

    private Integer examId;

    public ProblemsForExamKey(Integer proId, Integer examId) {
        this.proId = proId;
        this.examId = examId;
    }

    public ProblemsForExamKey() {
        super();
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }
}