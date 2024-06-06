package com.example.magicarcade;

public class Voucher {
    private String name;
    private int cost;
    private int imageResId;

    public Voucher(String name, int cost, int imageResId) {
        this.name = name;
        this.cost = cost;
        this.imageResId = imageResId;
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
