package com.example.magicarcade.zwevendeBelg;

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
    private final SurfaceHolder holder;
    private final Bitmap birdDown;
    private final Bitmap birdUp;
    private final Bitmap birdStationary;
    private final Paint paint;
    private final int birdX;
    private final Bitmap pipeTop;
    private final Bitmap pipeBottom;
    private final int pipeSpeedX;
    private Thread gameThread = null;
    private volatile boolean running = false;
    private Bitmap currentBird;
    private int birdY;
    private int birdSpeedY;
    private int pipeX, pipeY;

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

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(0xFF87CEEB);
            canvas.drawBitmap(currentBird, birdX, birdY, paint);
            canvas.drawBitmap(pipeTop, pipeX, pipeY - pipeTop.getHeight(), paint);
            canvas.drawBitmap(pipeBottom, pipeX, pipeY + 200, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            birdSpeedY = -30;
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
