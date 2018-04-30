package com.pojo;

public class UidAndAccept {
    private Integer uid;
    private Integer accept;
    public UidAndAccept(Integer uid,Integer accept)
    {
        this.uid = uid;
        this.accept = accept;
    }
    public void setAccept(Integer accept) {
        this.accept = accept;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getUid() {
        return uid;
    }

    public Integer getAccept() {
        return accept;
    }
}
