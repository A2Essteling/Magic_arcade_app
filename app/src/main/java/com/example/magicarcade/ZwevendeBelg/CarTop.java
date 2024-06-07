package com.example.magicarcade.ZwevendeBelg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;

public class CarTop extends GameObject {

    public CarTop(HashMap<String, Bitmap> images, int locationX, int locationY) {
        super(images, locationX, locationY);

    }
    @Override
    void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(super.getImage("car_top_front"), super.getLocationX(), super.getLocationY(), paint);
    }

    @Override
    void update() {

    }
}
