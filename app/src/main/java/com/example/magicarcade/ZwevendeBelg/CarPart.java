package com.example.magicarcade.ZwevendeBelg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CarPart {

    private Bitmap image;
    private int partX;
    private int partY;

    // TODO: Implement this class somewhere else?
    public CarPart(Bitmap pipeImage, int partX, int partY){
        this.image = pipeImage;
        this.partX = partX;
        this.partY = partY;
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(this.image, this.partX, this.partY, paint);
    }

    public void update(int newPartX){
        this.partX = newPartX;
    }
}
