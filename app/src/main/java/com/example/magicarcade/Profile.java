package com.example.magicarcade;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private static String userID;
    private static int points;
    private static int highScore;
    private static List<String> vouchers = new ArrayList<>();

    private Profile() {}

    public static String getUserID() {
        return userID;
    }

    public static void setId(String userID) {
        Profile.userID = userID;
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

    public static List<String> getVouchers() {
        return vouchers;
    }

    public static void addVoucher(String voucher) {
        Profile.vouchers.add(voucher);
    }

    public static void clearVouchers() {
        Profile.vouchers.clear();
    }
}
