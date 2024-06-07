package com.example.magicarcade.ZwevendeBelg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;

import java.util.HashMap;

public class Player extends GameObject{
    private Bitmap currentImage;
    public Player(HashMap<String, Bitmap> images, int locationX, int locationY){
        super(images, locationX, locationY);
        this.currentImage = images.get("stationary");
    }

    public void setCurrentImage(Bitmap image){
        this.currentImage = image;
    }

    @Override
    void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(this.currentImage, super.getLocationX(), super.getLocationY(), paint);
    }

    @Override
    void update() {
        super.setLocationY(super.getLocationY() + super.getSpeedYDirection());
        super.setSpeedYDirection(super.getSpeedYDirection() + 2);
        if (super.getSpeedYDirection() > -5 && super.getSpeedYDirection() < 5) {
            currentImage = super.getImage("stationary");
        } else if (super.getSpeedYDirection() >= 5) {
            currentImage = super.getImage("down");
        } else if (super.getSpeedYDirection() <= 5) {
            currentImage = super.getImage("up");
        }
    }
}
