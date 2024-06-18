package com.example.magicarcade.ZwevendeBelg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

public class CarRow {
    private ArrayList<CarPart> carParts;
    private int pipeX;
    private int pipeY;

    public CarRow(int pipeX, int pipeY) {
        this.carParts = new ArrayList<>();
        this.pipeX = pipeX;
        this.pipeY = pipeY;
    }

    public void drawPipes(Canvas canvas, Paint paint) {
        for (CarPart carPart : carParts) {
            carPart.draw(canvas, paint);
        }
    }

    public void update(int pipeX) {
        for (CarPart carPart : carParts) {
            carPart.update(pipeX);
        }
    }

    public void generatePipeRowImages(Bitmap pipeTopBack, Bitmap pipeTopMiddle, Bitmap pipeTopFront, Bitmap pipeBottomFront,
                                  Bitmap pipeBottomMiddle, Bitmap pipeBottomBack, int imageHeight, int gapHeight, int screenHeight) {
        int heightCounter = 0;
        CarPart topPart = new CarPart(pipeTopBack, pipeX, heightCounter);
        carParts.add(topPart);
        while (heightCounter + imageHeight < pipeY - imageHeight){
            heightCounter += imageHeight;
            CarPart topMiddlepart = new CarPart(pipeTopMiddle, pipeX, heightCounter);
            carParts.add(topMiddlepart);
        }
        CarPart topBottomPart = new CarPart(pipeTopFront, pipeX, heightCounter + imageHeight);
        carParts.add(topBottomPart);

        // PipeY value is here, at the bottom of the top pipe
        CarPart bottomTopPart = new CarPart(pipeBottomFront, pipeX, heightCounter + gapHeight);
        carParts.add(bottomTopPart);
        while (heightCounter + imageHeight + gapHeight < screenHeight + 20){
            heightCounter += imageHeight;
            CarPart bottomMiddlePart = new CarPart(pipeBottomMiddle, pipeX, heightCounter + gapHeight);
            carParts.add(bottomMiddlePart);
        }
        CarPart bottomBottomPart = new CarPart(pipeBottomBack, pipeX, heightCounter + imageHeight + gapHeight);
        carParts.add(bottomBottomPart);
    }
}
