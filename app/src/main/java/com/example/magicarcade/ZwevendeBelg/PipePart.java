package com.example.magicarcade.ZwevendeBelg;

import android.graphics.Bitmap;

public class PipePart {

    private Bitmap image;
    private int pipeX;
    private int pipeY;

    public PipePart(Bitmap pipeImage, int pipeX, int pipeY){
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
}
