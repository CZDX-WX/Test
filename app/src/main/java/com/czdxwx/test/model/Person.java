package com.czdxwx.test.model;

import android.os.Parcel;
import android.os.Parcelable;

import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;
import java.util.ArrayList;

@Table(name = "person")
public class Person implements Serializable{
    private transient int id;
    private String name;
    private String number;
    private String gender;
    private String hobby;
    private String nativePlace;
    private int majorid;
    private int isOnFocus;
    private String profile;
    private String password;
    private float balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person() {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public int getMajorid() {
        return majorid;
    }

    public void setMajorid(int majorid) {
        this.majorid = majorid;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Person(int id, String name, String number, String gender, String hobby, String nativePlace, int majorid, int isOnFocus, String profile) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.gender = gender;
        this.hobby = hobby;
        this.nativePlace = nativePlace;
        this.majorid = majorid;
        this.isOnFocus = isOnFocus;
        this.profile = profile;
    }

    public int getIsOnFocus() {
        return isOnFocus;
    }

    public void setIsOnFocus(int isOnFocus) {
        this.isOnFocus = isOnFocus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

}
