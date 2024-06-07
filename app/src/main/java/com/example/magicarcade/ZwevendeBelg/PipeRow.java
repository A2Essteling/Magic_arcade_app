package com.example.magicarcade.ZwevendeBelg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

public class PipeRow {
    private ArrayList<PipePart> pipeParts;
    private int pipeX;
    private int pipeY;

    public PipeRow(int pipeX, int pipeY) {
        this.pipeParts = new ArrayList<>();
        this.pipeX = pipeX;
        this.pipeY = pipeY;
    }

    public void drawPipes(Canvas canvas, Paint paint) {
        for (PipePart pipePart : pipeParts) {
            pipePart.draw(canvas, paint);
        }
    }

    public void update(int pipeX) {
        for (PipePart pipePart : pipeParts) {
            pipePart.update(pipeX);
        }
    }

    public void generatePipeRowImages(Bitmap pipeTopBack, Bitmap pipeTopMiddle, Bitmap pipeTopFront, Bitmap pipeBottomFront,
                                  Bitmap pipeBottomMiddle, Bitmap pipeBottomBack, int imageHeight, int gapHeight, int screenHeight) {
        int heightCounter = 0;
        PipePart topPart = new PipePart(pipeTopBack, pipeX, heightCounter);
        pipeParts.add(topPart);
        while (heightCounter + imageHeight < pipeY - imageHeight){
            heightCounter += imageHeight;
            PipePart topMiddlepart = new PipePart(pipeTopMiddle, pipeX, heightCounter);
            pipeParts.add(topMiddlepart);
        }
        PipePart topBottomPart = new PipePart(pipeTopFront, pipeX, heightCounter + imageHeight);
        pipeParts.add(topBottomPart);

        // PipeY value is here, at the bottom of the top pipe
        PipePart bottomTopPart = new PipePart(pipeBottomFront, pipeX, heightCounter + gapHeight);
        pipeParts.add(bottomTopPart);
        while (heightCounter + imageHeight + gapHeight < screenHeight + 20){
            heightCounter += imageHeight;
            PipePart bottomMiddlePart = new PipePart(pipeBottomMiddle, pipeX, heightCounter + gapHeight);
            pipeParts.add(bottomMiddlePart);
        }
        PipePart bottomBottomPart = new PipePart(pipeBottomBack, pipeX, heightCounter + imageHeight + gapHeight);
        pipeParts.add(bottomBottomPart);
    }
}
