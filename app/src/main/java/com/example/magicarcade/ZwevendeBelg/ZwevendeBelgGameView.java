package com.example.magicarcade.ZwevendeBelg;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.core.content.ContextCompat;
import com.example.magicarcade.R;
import java.util.HashMap;

public class ZwevendeBelgGameView extends SurfaceView implements Runnable {
    // Sprites have a width of 16 and a height of 10
    private Thread gameThread = null;
    private Paint paint;
    private SurfaceHolder holder;
    private volatile boolean running = false;
    private Bitmap belgDown, belgUp, belgStationary;
    private Bitmap pipeTopBack, pipeTopMiddle, pipeTopFront, pipeBottomBack, pipeBottomMiddle, pipeBottomFront;
    private Bitmap currentBird;
    private int imageHeight;
    private int imageWidth;
    private int gapHeight;
    private int screenWidth;
    private int screenHeight;
    private int belgX, belgY;
    private double belgSpeedY;
    private int pipeX, pipeY;
    private int pipeSpeedX;
    private int prevPipeSpawnTime;
    private int pipeSpawnTime;
    private int dayNightCycle;
    private HashMap<Integer, PipePart> idPipes = new HashMap<>();
    private Color theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.lightBlue));

    public ZwevendeBelgGameView(Context context) {
        // Configure game settings
        super(context);
        holder = getHolder();
        paint = new Paint();

        belgUp = BitmapFactory.decodeResource(getResources(), R.drawable.belg_up);
        belgStationary = BitmapFactory.decodeResource(getResources(), R.drawable.belg_stationary);
        belgDown = BitmapFactory.decodeResource(getResources(), R.drawable.belg_down);

        currentBird = belgStationary;
        belgX = 100;
        belgY = 100;
        belgSpeedY = 0;

        imageWidth = 30;
        imageHeight = 20;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels-110;
        gapHeight = 500;

        pipeSpawnTime = 1000;
        prevPipeSpawnTime = pipeSpawnTime;
        dayNightCycle = 0;

        pipeTopBack = BitmapFactory.decodeResource(getResources(), R.drawable.car_top_back_red);
        pipeTopMiddle = BitmapFactory.decodeResource(getResources(), R.drawable.car_top_middle_red);
        pipeTopFront = BitmapFactory.decodeResource(getResources(), R.drawable.car_top_front_red);

        pipeBottomFront = BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_front_red);
        pipeBottomMiddle = BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_middle_red);
        pipeBottomBack = BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_back_red);

        pipeX = screenWidth+100;
        pipeY = (int) (Math.random() * (screenHeight-(gapHeight+(imageHeight*2))) + (imageHeight*2));
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
        setBelgImage();
        checkCollision();
        generatePipeImages();
        setDayNightCycle();

        // Timers and adjustable values
        pipeX += pipeSpeedX;
        if (pipeSpawnTime <= 0 || pipeX <= -pipeTopMiddle.getWidth()){
            pipeX = getWidth();
            pipeY = (int) (Math.random() * (screenHeight-(gapHeight+(imageHeight*2))) + (imageHeight*2));

            // Make game more difficult
            pipeSpeedX += 1;
            gapHeight -= 5;
            pipeSpawnTime = prevPipeSpawnTime-10;
        }
        pipeSpawnTime-=1;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null){
            // Colors (light to dark)
            canvas.drawColor(theColor.toArgb());

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
            currentBird = belgUp;
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

    public void setBelgImage(){
        // Set current state of belg
        belgY += belgSpeedY;
        belgSpeedY += 2;
        if (belgSpeedY > -5 && belgSpeedY < 5){
            currentBird = belgStationary;
        } else if (belgSpeedY >= 5) {
            currentBird = belgDown;
        } else if (belgSpeedY <= -5) {
            currentBird = belgUp;
        }
    }

    public void checkCollision(){
        // Check collision
        if (belgY <= 0 || belgY >= screenHeight){
            running = false;
        }
        if (belgX >= pipeX){
            if (belgY + currentBird.getHeight() > pipeY + gapHeight ||
                    belgY - currentBird.getHeight() < pipeY) {
                running = false;
            }
        }
    }

    public void generatePipeImages(){
        // Generate new pipe images
        int id = 0;
        int heightCounter = 0;

        PipePart topPart = new PipePart(pipeTopBack, pipeX, heightCounter);
        idPipes.put(id, topPart);
        id++;
        while (heightCounter + imageHeight < pipeY - imageHeight){
            heightCounter += imageHeight;
            PipePart topMiddlepart = new PipePart(pipeTopMiddle, pipeX, heightCounter);
            idPipes.put(id, topMiddlepart);
            id++;
        }
        PipePart topBottomPart = new PipePart(pipeTopFront, pipeX, heightCounter + imageHeight);
        idPipes.put(id, topBottomPart);
        id++;

        // PipeY value is here, at the bottom of the top pipe
        PipePart bottomTopPart = new PipePart(pipeBottomFront, pipeX, heightCounter + gapHeight);
        idPipes.put(id, bottomTopPart);
        id++;
        while (heightCounter + imageHeight + gapHeight < screenHeight + 20){
            heightCounter += imageHeight;
            PipePart bottomMiddlePart = new PipePart(pipeBottomMiddle, pipeX, heightCounter + gapHeight);
            idPipes.put(id, bottomMiddlePart);
            id++;
        }
        PipePart bottomBottomPart = new PipePart(pipeBottomBack, pipeX, heightCounter + imageHeight + gapHeight);
        idPipes.put(id, bottomBottomPart);
    }

    public void setDayNightCycle(){
        // Day / Nigh cycle
        if (dayNightCycle >= 3000){
            dayNightCycle = 1;
        } else if (dayNightCycle > 0 && dayNightCycle <= 1050) {
            theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.lightBlue));
        } else if (dayNightCycle > 1050 && dayNightCycle <= 1150) {
            theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.lightDarkerBlue));
        } else if (dayNightCycle > 1150 && dayNightCycle <= 1250) {
            theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.blue));
        } else if (dayNightCycle > 1250 && dayNightCycle <= 1350) {
            theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.goingDarkBlue));
        } else if (dayNightCycle > 1350 && dayNightCycle <= 1450) {
            theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.almostDarkBlue));
        } else if (dayNightCycle > 1550 && dayNightCycle <= 2600) {
            theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.darkBlue));
        } else if (dayNightCycle > 2600 && dayNightCycle <= 2700) {
            theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.almostDarkBlue));
        } else if (dayNightCycle > 2700 && dayNightCycle <= 2800) {
            theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.goingDarkBlue));
        } else if (dayNightCycle > 2800 && dayNightCycle <= 2900) {
            theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.blue));
        } else if (dayNightCycle > 2900 && dayNightCycle < 3000) {
            theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.lightDarkerBlue));
        }
        dayNightCycle+=1;
    }


}
