package com.example.magicarcade.ZwevendeBelg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CarPart {

    private Bitmap image;
    private double partX;
    private double partY;

    // TODO: Implement this class somewhere else?
    public CarPart(Bitmap pipeImage, double partX, double partY){
        this.image = pipeImage;
        this.partX = partX;
        this.partY = partY;
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(this.image, (float) this.partX, (float) this.partY, paint);
    }

    public void update(double newPartX){
        this.partX = newPartX;
    }
}
