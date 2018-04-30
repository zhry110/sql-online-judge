package com.pojo;

public class TypeForProblems {
    private Long proId;

    private Boolean type;

    public TypeForProblems(Long proId, Boolean type) {
        this.proId = proId;
        this.type = type;
    }

    public TypeForProblems() {
        super();
    }

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }
}