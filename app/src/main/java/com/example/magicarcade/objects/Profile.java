package com.example.magicarcade.objects;

import com.example.magicarcade.Voucher;
import com.example.magicarcade.mqtt.MqttService;
import java.util.ArrayList;
import java.util.List;

public class Profile {
    private static final List<Voucher> vouchers = new ArrayList<>();
    private static String id;
    private static int points;
    private static int highScore;
    private static Controller controller = new Controller();

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
        MqttService.publishMsg("lcd", String.valueOf(highScore));
    }

    public static List<Voucher> getVouchers() {
        return vouchers;
    }

    public static void addVoucher(Voucher voucher) {
        vouchers.add(voucher);
    }

    public static void setControllerID(int id) {
        controller.setID(id);
    }

    public static Controller getController() {
        return controller;
    }
}
