package com.czdxwx.test.model;

import android.content.Intent;
import android.media.Image;

import net.tsz.afinal.annotation.sqlite.Table;


public class Main {
    private String setting_name;
    private String intentInfo;

    public Main() {
    }

    public Main(String setting_name, String intentInfo) {
        this.setting_name = setting_name;
        this.intentInfo = intentInfo;
    }

    public String getIntentInfo() {
        return intentInfo;
    }

    public void setIntentInfo(String intentInfo) {
        this.intentInfo = intentInfo;
    }

    public String getSetting_name() {
        return setting_name;
    }

    public void setSetting_name(String setting_name) {
        this.setting_name = setting_name;
    }
}
