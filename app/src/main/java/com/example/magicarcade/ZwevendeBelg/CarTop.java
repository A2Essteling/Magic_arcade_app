package com.example.magicarcade.ZwevendeBelg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.HashMap;

public class CarTop extends GameObject {
    private int length;
    private int screenHeight;
    private int screenWidth;
    private int heightCounter = 0;
    private ArrayList<CarPart> carParts = new ArrayList<>();
    private int imageHeight;

    public CarTop(HashMap<String, Bitmap> images, int locationX, int locationY) {
        super(images, locationX, locationY);
        this.screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        this.length = locationY/2;

        // TODO: Calculate image height for scaling on different screens
        this.imageHeight = 20;

        // Generate car parts:
        carParts.add(new CarPart(super.getImage("top_front"), super.getLocationX(), heightCounter));
        while (heightCounter + imageHeight < super.getLocationY() + length - imageHeight) {
            carParts.add(new CarPart(super.getImage("top_middle"), super.getLocationX(), heightCounter + imageHeight));
            heightCounter += imageHeight;
        }
        carParts.add(new CarPart(super.getImage("top_back"), super.getLocationX(), heightCounter + imageHeight));
    }
    @Override
    void draw(Canvas canvas, Paint paint) {
        for (CarPart carPart : carParts) {
            carPart.draw(canvas, paint);
        }
    }

    @Override
    void update() {
        // Move Car
        // TODO: Get speed from View?
        super.setLocationX(super.getLocationX() - super.getSpeedXDirection()-1);
        if (super.getLocationX() < 0)
            super.setLocationX(screenWidth);

        for (CarPart carPart : carParts) {
            carPart.update(getLocationX()-getSpeedXDirection());
        }
    }
}
