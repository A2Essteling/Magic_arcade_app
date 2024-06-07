package com.example.magicarcade.ZwevendeBelg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CarPart {

    private Bitmap image;
    private int pipeX;
    private int pipeY;

    public CarPart(Bitmap pipeImage, int pipeX, int pipeY){
        this.image = pipeImage;
        this.pipeX = pipeX;
        this.pipeY = pipeY;
    }

    public Bitmap getImage(){
        return this.image;
    }

    public int getPipeX(){
        return this.pipeX;
    }

    public int getPipeY(){
        return this.pipeY;
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(this.image, this.pipeX, this.pipeY, paint);
    }

    public void update(int pipeX){
        this.pipeX -= pipeX;
    }
}
