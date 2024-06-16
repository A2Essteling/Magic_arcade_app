package com.example.magicarcade;

import android.os.Parcel;
import android.os.Parcelable;

public class Voucher implements Parcelable {
    private String name;
    private int cost;
    private int imageResId;

    public Voucher(String name, int cost, int imageResId) {
        this.name = name;
        this.cost = cost;
        this.imageResId = imageResId;
    }

    protected Voucher(Parcel in) {
        name = in.readString();
        cost = in.readInt();
        imageResId = in.readInt();
    }

    public static final Creator<Voucher> CREATOR = new Creator<Voucher>() {
        @Override
        public Voucher createFromParcel(Parcel in) {
            return new Voucher(in);
        }

        @Override
        public Voucher[] newArray(int size) {
            return new Voucher[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(cost);
        dest.writeInt(imageResId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getImageResId() {
        return imageResId;
    }
}
