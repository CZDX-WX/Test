package com.czdxwx.test.model;

import net.tsz.afinal.annotation.sqlite.Table;


@Table(name = "Major_info")
public class Major {
    private int id;
    private int majorid;
    private String major;


    public Major() {
    }

    public int getMajorid() {
        return majorid;
    }

    public void setMajorid(int majorid) {
        this.majorid = majorid;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Major(int majorid, String major) {
        this.majorid = majorid;
        this.major = major;
    }

}
