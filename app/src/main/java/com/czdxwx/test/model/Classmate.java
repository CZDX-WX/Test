package com.czdxwx.test.model;

import android.widget.CheckBox;

public class Classmate {
    private boolean beSelected;
    private String name;
    private String number;

    public Classmate() {
    }

    public Classmate(boolean beSelected, String name, String number) {
        this.beSelected = beSelected;
        this.name = name;
        this.number = number;
    }

    public boolean isBeSelected() {
        return beSelected;
    }

    public void setBeSelected(boolean beSelected) {
        this.beSelected = beSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
