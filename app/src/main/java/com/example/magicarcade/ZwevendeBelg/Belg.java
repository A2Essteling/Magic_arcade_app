package com.example.magicarcade.ZwevendeBelg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;

public class Belg extends GameObject {
    private Bitmap currentImage;
    private double speedCap = 6;
    private double jumpForce = 5;

    public Belg(HashMap<String, Bitmap> images, int locationX, int locationY) {
        super(images, locationX, locationY);
        this.currentImage = images.get("stationary");
    }

    public void setCurrentImage(Bitmap image) {
        this.currentImage = image;
    }

    @Override
    void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(this.currentImage, (float) super.getLocationX(), (float) super.getLocationY(), paint);
    }

    @Override
    void update() {
        this.addLocationY(super.getSpeedYDirection());
        super.setSpeedYDirection(super.getSpeedYDirection() + 0.1);
        if (super.getSpeedYDirection() > -speedCap/2 && super.getSpeedYDirection() < speedCap/2) {
            currentImage = super.getImage("stationary");
        } else if (super.getSpeedYDirection() >= speedCap/2) {
            currentImage = super.getImage("down");
        } else if (super.getSpeedYDirection() <= speedCap/2) {
            currentImage = super.getImage("up");
        }
    }

    private void addLocationY(double speedYDirection) {
        if (speedYDirection > speedCap) {
            speedYDirection = speedCap;
        }
        super.setSpeedYDirection(speedYDirection);
        double newLocation = getLocationY() + speedYDirection;
        super.setLocationY(newLocation);
    }

    public void bounce() {
        double bounceFactor = jumpForce;
        super.setSpeedYDirection(0);
        addLocationY(-bounceFactor);
    }
}
