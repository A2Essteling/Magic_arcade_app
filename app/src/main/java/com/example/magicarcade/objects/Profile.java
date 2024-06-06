package com.example.magicarcade.objects;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private static String id;
    private static int points;
    private static int highScore;
    private static List<Voucher> vouchers = new ArrayList<>();

    public static String getUserID() {
        return id;
    }

    public static void setUserID(String id) {
        Profile.id = id;
    }

    public static int getPoints() {
        return points;
    }

    public static void setPoints(int points) {
        Profile.points = points;
    }

    public static int getHighScore() {
        return highScore;
    }

    public static void setHighScore(int highScore) {
        Profile.highScore = highScore;
    }

    public static List<Voucher> getVouchers() {
        return vouchers;
    }

    public static void addVoucher(Voucher voucher) {
        vouchers.add(voucher);
    }
}
