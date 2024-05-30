package com.example.magicarcade.ZwevendeBelg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.magicarcade.R;

public class ZwevendeBelgGameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private SurfaceHolder holder;
    private boolean running;
    private Bitmap bird;
    private Paint paint;
    private Bitmap birdDown, birdUp, birdStationary;
    private Bitmap currentBird;
    private int birdX, birdY;
    private int birdSpeedY;
    private Bitmap pipeTop, pipeBottom;
    private int pipeX, pipeY;
    private int pipeSpeedX;

    public ZwevendeBelgGameView(Context context) {
        super(context);
        holder = getHolder();
        paint = new Paint();
        birdDown = BitmapFactory.decodeResource(getResources(), R.drawable.belg_down);
        birdUp = BitmapFactory.decodeResource(getResources(), R.drawable.belg_up);
        birdStationary = BitmapFactory.decodeResource(getResources(), R.drawable.belg_stationary);
        currentBird = birdStationary;
        birdX = 100;
        birdY = 100;
        birdSpeedY = 0;
        pipeTop = BitmapFactory.decodeResource(getResources(), R.drawable.car_top_red);
        pipeBottom = BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_red);
        pipeX = 500;
        pipeY = 300;
        pipeSpeedX = -10;
    }

    @Override
    public void run() {
        while (running) {
            if (!holder.getSurface().isValid()) {
                continue;
            }

            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(0xFF87CEEB); // Sky blue background
            canvas.drawBitmap(bird, 100, 100, paint); // Drawing the bird at position (100, 100)
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void update() {
        birdY += birdSpeedY;
        birdSpeedY += 2;
        if (birdSpeedY > 0) {
            currentBird = birdDown;
        } else if (birdSpeedY < 0) {
            currentBird = birdUp;
        }

        pipeX += pipeSpeedX;
        if (pipeX < -pipeTop.getWidth()) {
            pipeX = getWidth();
            pipeY = (int) (Math.random() * (getHeight() - 200)) + 100;
        }

        if (birdX < pipeX + pipeTop.getWidth() && birdX + currentBird.getWidth() > pipeX) {
            if (birdY < pipeY || birdY + currentBird.getHeight() > pipeY + 200) {
                running = false;
            }
        }
    }

    private void draw() {
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(0xFF87CEEB);
        canvas.drawBitmap(currentBird, birdX, birdY, paint);
        holder.unlockCanvasAndPost(canvas);

        canvas.drawBitmap(pipeTop, pipeX, pipeY - pipeTop.getHeight(), paint);
        canvas.drawBitmap(pipeBottom, pipeX, pipeY + 200, paint);

        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            birdSpeedY = -30;
            currentBird = birdUp;
        }
        return true;
    }


}
