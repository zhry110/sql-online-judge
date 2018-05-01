package com.pojo;

public class Admin {
    private Integer id;

    private Integer pri;

    public Admin(Integer id, Integer pri) {
        this.id = id;
        this.pri = pri;
    }

    public Admin() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPri() {
        return pri;
    }

    public void setPri(Integer pri) {
        this.pri = pri;
    }
}