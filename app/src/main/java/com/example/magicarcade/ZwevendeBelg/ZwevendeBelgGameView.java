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
import java.util.HashMap;
import java.util.Random;

public class ZwevendeBelgGameView extends SurfaceView implements Runnable {
    private Thread gameThread = null;
    private Paint paint = new Paint();
    private SurfaceHolder holder = getHolder();
    private volatile boolean running = false;
    private Bitmap belgDown, belgUp, belgStationary,
            carTopBack, carTopMiddle, carTopFront, carBottomBack, carBottomMiddle, carBottomFront;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Belg belg;
    private int objectSpawnTime = 0;
    private int prevObjectSpawnTime = 200;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private Color theColor = Color.valueOf(ContextCompat.getColor(getContext(), R.color.lightBlue));
    private int gapHeight = 400;
    private int gameSpeedX = 2;
    private int imageWidth = 20;
    private int dayNightCycle = 0;
    private int gameX;

    public ZwevendeBelgGameView(Context context) {
        // TODO:
        //  -collision detection
        //  -spawning pipes (minor fixes)
        //  -menu popup with an exit game button
        //  -difficulty (final adjustments)

        // Configure game settings
        super(context);

        HashMap<String, Bitmap> playerMap = new HashMap<>();
        playerMap.put("up", BitmapFactory.decodeResource(getResources(), R.drawable.belg_up));
        playerMap.put("stationary", BitmapFactory.decodeResource(getResources(), R.drawable.belg_stationary));
        playerMap.put("down", BitmapFactory.decodeResource(getResources(), R.drawable.belg_down));

        this.belg = new Belg(playerMap, screenWidth/6, screenHeight/6);
        gameObjects.add(this.belg);
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
        gameX += gameSpeedX;

        setDayNightCycle();

        // Update game objects
        for (GameObject gameObject : gameObjects) {
            gameObject.update();
            gameObject.setSpeedXDirection(gameSpeedX);
        }

        // Timers and adjustable values
        if (objectSpawnTime <= 1 || gameX <= -imageWidth) {
            gameX = screenWidth;

            HashMap<String, Bitmap> newCarImages = new HashMap<>();
            newCarImages.put("top_back", BitmapFactory.decodeResource(getResources(), R.drawable.car_top_back_red));
            newCarImages.put("top_middle", BitmapFactory.decodeResource(getResources(), R.drawable.car_top_middle_red));
            newCarImages.put("top_front", BitmapFactory.decodeResource(getResources(), R.drawable.car_top_front_red));

            newCarImages.put("bottom_front", BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_front_red));
            newCarImages.put("bottom_middle", BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_middle_red));
            newCarImages.put("bottom_back", BitmapFactory.decodeResource(getResources(), R.drawable.car_bottom_back_red));

            Random random = new Random();
            int rndY = random.nextInt(screenHeight-200)+100;
            Car car = new Car(newCarImages, gameX, rndY, gapHeight, screenHeight);
            gameObjects.add(car);

            // Make the game more difficult
            gameSpeedX += 1;
            gapHeight -= 1;
            objectSpawnTime = prevObjectSpawnTime -10;
        }
        objectSpawnTime -= 1;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null){
            // Colors (light to dark)
            canvas.drawColor(theColor.toArgb());

            // Draw player
            belg.draw(canvas, paint);

            // Draw game objects
            for (GameObject gameObject : gameObjects) {
                gameObject.draw(canvas, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            belg.bounce();
            belg.setCurrentImage(belg.getImage("up"));
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

    public void bounce() {
        belg.bounce();
        belg.setCurrentImage(belg.getImage("up"));
    }
}
