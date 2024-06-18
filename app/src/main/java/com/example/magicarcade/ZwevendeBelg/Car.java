package com.example.magicarcade.ZwevendeBelg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.Random;

public class Car extends GameObject {
    private CarTop carTop;
    private CarBottom carBottom;
    private int carTopLength;
    private int carBottomLength;
    private int gapHeight;
    private int screenHeight;

    public Car(HashMap<String, Bitmap> images, int locationX, int locationY, int gapHeight, int screenHeight) {
        super(images, locationX, locationY);

        this.gapHeight = gapHeight;
        this.screenHeight = screenHeight;

        // TODO: Change generation of random lengths?
        Random rnd = new Random();

        this.carTopLength = rnd.nextInt(screenHeight-gapHeight-200)+100;
        this.carBottomLength = rnd.nextInt(screenHeight-gapHeight-200)+100;

        this.carTop = new CarTop(images, locationX, carTopLength/2);
        this.carBottom = new CarBottom(images, locationX, carTopLength+gapHeight+(carBottomLength/2));
    }
    @Override
    void draw(Canvas canvas, Paint paint) {
        // TODO: Draw correct images (front & back)
        carTop.draw(canvas, paint);
        carBottom.draw(canvas, paint);
    }

    @Override
    void update() {
        carTop.update();
        carBottom.update();
    }
}
