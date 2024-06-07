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

import java.util.ArrayList;

public class ZwevendeBelgGameView extends SurfaceView implements Runnable {
    // Sprites have a width of 16 and a height of 10
    private Thread gameThread = null;
    private Paint paint;
    private SurfaceHolder holder;
    private volatile boolean running = false;
    private Bitmap belgDown, belgUp, belgStationary;
    private Bitmap carTopBack, carTopMiddle, carTopFront, carBottomBack, carBottomMiddle, carBottomFront;
    private Bitmap currentBird;
    private int imageHeight;
    private int imageWidth;
    private int gapHeight;
    private int screenWidth;
    private int screenHeight;
    private int belgX, belgY;
    private double belgSpeedY;
    private int carX, carY;
    private int carSpeedX;
    private int prevCarSpawnTime;
    private int carSpawnTime;
    private int dayNightCycle;
    private ArrayList<CarRow> cars = new ArrayList<>();
    private Color theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.lightBlue));
    private ArrayList<GameObject> gameObjects = new ArrayList<>();

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

        carSpawnTime = 50;
        prevCarSpawnTime = 100;
        dayNightCycle = 0;

        carTopBack = BitmapFactory.decodeResource(getResources(), R.drawable.car_top_back_red);
        carTopMiddle = BitmapFactory.decodeResource(getResources(), R.drawable.car_top_middle_red);
        carTopFront = BitmapFactory.decodeResource(getResources(), R.drawable.car_top_front_red);

        carBottomFront = BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_front_red);
        carBottomMiddle = BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_middle_red);
        carBottomBack = BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_back_red);

        carX = screenWidth+100;
        carY = (int) (Math.random() * (screenHeight-(gapHeight+(imageHeight*2))) + (imageHeight*2));
        carSpeedX = -5;
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
        carX += carSpeedX;

        setBelgImage();
        checkCollision();
        setDayNightCycle();

        // Update carrows
        for (CarRow carRow : cars) {
            carRow.update(carX);
        }

        // Timers and adjustable values
        if (carSpawnTime <= 0 || carX <= -carTopMiddle.getWidth()){
            carX = getWidth();
            carY = (int) (Math.random() * (screenHeight-(gapHeight+(imageHeight*2))) + (imageHeight*2));

            CarRow row = new CarRow(carX, carY);
            row.generatePipeRowImages(carTopBack, carTopMiddle, carTopFront, carBottomFront, carBottomMiddle, carBottomFront,
                                      imageHeight, gapHeight, screenHeight);
            cars.add(row);

            // Make game more difficult
            carSpeedX += 1;
            gapHeight -= 5;
            carSpawnTime = prevCarSpawnTime -10;
        }
        carSpawnTime -=1;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null){
            // Colors (light to dark)
            canvas.drawColor(theColor.toArgb());

            // Draw belg
            canvas.drawBitmap(currentBird, belgX, belgY, paint);

            // Draw cars
            for (CarRow carRow : cars) {
                carRow.drawPipes(canvas, paint);
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
        // Collision: Out of Bounds
        if (belgY <= 0 || belgY >= screenHeight){
            running = false;
        }
        // Collision: Car
        if (belgX >= carX){
            if (belgY + currentBird.getHeight() > carY + gapHeight ||
                    belgY - currentBird.getHeight() < carY) {
                running = false;
            }
        }
    }

    public void setDayNightCycle(){
        // Day / Night cycle
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
