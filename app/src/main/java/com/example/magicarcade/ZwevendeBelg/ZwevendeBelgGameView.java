package com.example.magicarcade.ZwevendeBelg;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.magicarcade.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ZwevendeBelgGameView extends SurfaceView implements Runnable {
    // Sprites have a width of 16 and a height of 10
    private Thread gameThread = null;
    private Paint paint;
    private SurfaceHolder holder;
    private volatile boolean running = false;
    private Bitmap birdDown, birdUp, birdStationary;
    private Bitmap pipeTopBack, pipeTopMiddle, pipeTopFront, pipeBottomBack, pipeBottomMiddle, pipeBottomFront;
    private Bitmap currentBird;
    private int imageHeight;
    private int imageWidth;
    private int gapHeight;
    private int screenWidth;
    private int screenHeight;
    private int belgX, belgY;
    private int belgSpeedY;
    private int pipeX, pipeY;
    private int pipeSpeedX;
    private int prevPipeSpawnTime;
    private int pipeSpawnTime;
    private HashMap<Integer, PipePart> idPipes = new HashMap<>();

    public ZwevendeBelgGameView(Context context) {
        // Configure game settings
        super(context);
        holder = getHolder();
        paint = new Paint();

        birdDown = BitmapFactory.decodeResource(getResources(), R.drawable.belg_down);
        birdUp = BitmapFactory.decodeResource(getResources(), R.drawable.belg_up);
        birdStationary = BitmapFactory.decodeResource(getResources(), R.drawable.belg_stationary);

        currentBird = birdStationary;
        belgX = 100;
        belgY = 100;
        belgSpeedY = 0;

        imageWidth = 30;
        imageHeight = 20;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        gapHeight = 500;

        pipeSpawnTime = 1000;
        prevPipeSpawnTime = pipeSpawnTime;

        pipeTopBack = BitmapFactory.decodeResource(getResources(), R.drawable.car_top_back_red);
        pipeTopMiddle = BitmapFactory.decodeResource(getResources(), R.drawable.car_top_middle_red);
        pipeTopFront = BitmapFactory.decodeResource(getResources(), R.drawable.car_top_front_red);

        pipeBottomFront = BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_front_red);
        pipeBottomMiddle = BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_middle_red);
        pipeBottomBack = BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_back_red);

        pipeX = screenWidth+100;
        pipeY = 300;
        pipeSpeedX = -5;
    }

    @Override
    public void run() {
        while (running) {
            if (!holder.getSurface().isValid()) {
                continue;
            }

            update();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    draw(canvas);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void update() {
        // Set current state of belg
        belgY += belgSpeedY;
        belgSpeedY += 2;
        if (belgSpeedY > 0) {
            currentBird = birdDown;
        } else if (belgSpeedY < 0) {
            currentBird = birdUp;
        }

        // Check collision
        if (belgX >= pipeX){
            if (belgY + currentBird.getHeight() < pipeY ||
                    belgY - currentBird.getHeight() < pipeY + gapHeight) {
                running = false;
            }
        }

        // Generate new pipe images
        int id = 0;
        int heightCounter = 0;

        PipePart topPart = new PipePart(pipeTopBack, pipeX, heightCounter);
        idPipes.put(id, topPart);
        id++;
        while (heightCounter + imageHeight < pipeY){
            PipePart topMiddlepart = new PipePart(pipeTopMiddle, pipeX, heightCounter + imageHeight);
            idPipes.put(id, topMiddlepart);
            id++;
            heightCounter += imageHeight;
        }
        PipePart topBottomPart = new PipePart(pipeTopFront, pipeX, heightCounter + imageHeight);
        idPipes.put(id, topBottomPart);
        id++;

        PipePart bottomTopPart = new PipePart(pipeBottomFront, pipeX, heightCounter + imageHeight + gapHeight);
        idPipes.put(id, bottomTopPart);
        id++;
        while (heightCounter + imageHeight + gapHeight + imageHeight < screenHeight){
            PipePart bottomMiddlePart = new PipePart(pipeBottomMiddle, pipeX, heightCounter + imageHeight + gapHeight + imageHeight);
            idPipes.put(id, bottomMiddlePart);
            id++;
            heightCounter += imageHeight;
        }
        PipePart bottomBottomPart = new PipePart(pipeBottomBack, pipeX - 50, heightCounter + imageHeight + gapHeight + imageHeight - 100);
        idPipes.put(id, bottomBottomPart);

        // Timers and adjustable values
        pipeX += pipeSpeedX;
        pipeSpawnTime-=1;
        if (pipeSpawnTime <= 0 || pipeX <= -pipeTopMiddle.getWidth()){
            pipeX = getWidth();
            pipeY = (int) (Math.random() * getHeight()) + 100;

            // Make game more difficult
            pipeSpeedX += 1;
            gapHeight -= 5;
            pipeSpawnTime = prevPipeSpawnTime-10;
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null){
            canvas.drawColor(0xFF87CEEB);

            // Draw belg
            canvas.drawBitmap(currentBird, belgX, belgY, paint);

            // Calculate distances and draw the images based on the pipeY value
            for (Integer i : idPipes.keySet()) {
                canvas.drawBitmap(idPipes.get(i).getImage(), idPipes.get(i).getPipeX(), idPipes.get(i).getPipeY(), paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            belgSpeedY = -30;
            currentBird = birdUp;
        }
        return true;
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        running = false;
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
