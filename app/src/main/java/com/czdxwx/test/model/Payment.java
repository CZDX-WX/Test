package com.czdxwx.test.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Payment implements Parcelable {
    private String username;
    private String number;
    private int money;

    protected Payment(Parcel in) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(number);
        dest.writeInt(money);
    }
}
