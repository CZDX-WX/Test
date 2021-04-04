package com.czdxwx.test.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;

public class Payment implements Parcelable {
    private String username;
    private String number;
    private float money;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Payment(Parcel in) {
        username = in.readString();
        number = in.readString();
        money = in.readInt();
    }

    public static final Creator<Payment> CREATOR = new Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };

    public Payment() {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(number);
        dest.writeFloat(money);
    }
}
