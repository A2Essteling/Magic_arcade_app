package com.example.magicarcade;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private String userID;
    private int points;
    private int highScore;
    private List<String> vouchers;

    public Profile(String userID, int points, int highScore) {
        this.userID = userID;
        this.points = points;
        this.highScore = highScore;
        this.vouchers = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public List<String> getVouchers() {
        return vouchers;
    }

    public void addVoucher(String voucher) {
        this.vouchers.add(voucher);
    }
}

