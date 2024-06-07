package com.example.magicarcade.ZwevendeBelg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;

public abstract class GameObject {
    private int locationX;
    private int locationY;
    private HashMap<String, Bitmap> images = new HashMap<>();
    private int speedXDirection;
    private int speedYDirection;
    public GameObject(HashMap<String, Bitmap> images, int initialX, int initialY){
        this.images = images;
        this.locationX = initialX;
        this.locationY = initialY;
    }
    abstract void draw(Canvas canvas, Paint paint);
    abstract void update();
    public void setLocationX(int xValue) {
        this.locationX = xValue;
    }
    public int getLocationX() {
        return this.locationX;
    }
    public void setLocationY(int yValue) {
        this.locationY = yValue;
    }
    public int getLocationY() {
        return this.locationY;
    }
    public void setSpeedXDirection(int xValue) {
        this.speedXDirection = xValue;
    }
    public int getSpeedXDirection() {
        return this.speedXDirection;
    }
    public void setSpeedYDirection(int yValue) {
        this.speedYDirection = yValue;
    }
    public int getSpeedYDirection() {
        return this.speedYDirection;
    }
    public Bitmap getImage(String searchImage) {
        if (!images.containsKey(searchImage))
            return null;

        return images.get(searchImage);
    }
}
