package com.example.magicarcade;

public class Voucher {
    private final String name;
    private final int cost;
    private final int imageResId;

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
