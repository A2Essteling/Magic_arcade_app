package com.example.magicarcade.cobra;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.magicarcade.mqtt.MqttService;
import com.example.magicarcade.objects.Profile;

import java.util.ArrayList;
import java.util.Random;

public class CobraGameView extends View {

    private static final int GRID_SIZE = 40;
    private static final int CELL_SIZE = 20;
    private static final int SNAKE_LENGTH = 3;
    private static final int MOVE_DELAY = 300;
    private static final int SAFE_TIME = 5000;
    private static Direction currentDirection;

    private ArrayList<Coordinate> snake;
    private Coordinate food;
    private Handler handler;
    private int playerScore;
    private long startTime;

    //movement
    public static boolean isMoving = false;
    private int directionSpeedX;
    private int directionSpeedY;
    private int SCOREADD = 100;
    int nextLocationY;
    int nextLocationX;
    private int lives;


    public CobraGameView(Context context) {
        super(context);
        setFocusable(true);
        init();
    }


    private void init() {
        playerScore = 0;
        lives = 3;
        currentDirection = Direction.RIGHT;
        setDirectionSpeed(Direction.LEFT);

        snake = new ArrayList<>();
        for (int i = SNAKE_LENGTH - 1; i >= 0; i--) {
            snake.add(new Coordinate(i + 30, 30));
        }
        spawnFood();

        handler = new Handler();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        for (Coordinate c : snake) {
            canvas.drawRect(c.getX() * CELL_SIZE, c.getY() * CELL_SIZE,
                    (c.getX() + 1) * CELL_SIZE, (c.getY() + 1) * CELL_SIZE, paint);
        }
        paint.setColor(Color.RED);
        canvas.drawRect(food.getX() * CELL_SIZE, food.getY() * CELL_SIZE,
                (food.getX() + 1) * CELL_SIZE, (food.getY() + 1) * CELL_SIZE, paint);
    }

    public void startGame() {
        isMoving = true;
        Log.d("Cobra", "start");
        handler.postDelayed(moveSnakeRunnable, MOVE_DELAY);
    }

    public void terminateGame() {
        Profile.addScore(playerScore);
        isMoving = false;
        Log.d("Cobra", "died");
        handler.removeCallbacks(moveSnakeRunnable);
    }

    private void update() {
        Coordinate head = snake.get(0);

        nextLocationX = head.getX() + directionSpeedX;
        nextLocationY = head.getY() + directionSpeedY;

        if (!locationIsValid()) {
            terminateGame();
            Intent intent = new Intent(getContext(), GameOverActivity.class);
            intent.putExtra("SCORE", playerScore);
            getContext().startActivity(intent);
            lowerHealth();
            return;
        }

        snake.add(0, new Coordinate(nextLocationX, nextLocationY));
        if (nextLocationX == food.getX() && nextLocationY == food.getY()) {
            foodConsumed();
        } else {
            snake.remove(snake.size() - 1);
        }
        invalidate();
        if (isMoving) {
            handler.postDelayed(moveSnakeRunnable, MOVE_DELAY);
        }
    }

    public void setDirectionSpeed(Direction direction) {
        if ((currentDirection == Direction.UP && direction == Direction.DOWN) ||
                (currentDirection == Direction.DOWN && direction == Direction.UP) ||
                (currentDirection == Direction.LEFT && direction == Direction.RIGHT) ||
                (currentDirection == Direction.RIGHT && direction == Direction.LEFT)) {
            return;
        }

        Log.d("cobra", String.valueOf(direction));
        if (currentDirection != direction) {
            switch (direction) {
                case UP:
                    if (currentDirection == Direction.DOWN){
                        break;
                    }
                    currentDirection = direction;
                    directionSpeedX = 0;
                    directionSpeedY = -1;
                    break;
                case DOWN:
                    if (currentDirection == Direction.UP){
                        break;
                    }
                    currentDirection = direction;
                    directionSpeedX = 0;
                    directionSpeedY = 1;
                    break;
                case LEFT:
                    if (currentDirection == Direction.RIGHT){
                        break;
                    }
                    currentDirection = direction;
                    directionSpeedX = -1;
                    directionSpeedY = 0;
                    break;
                case RIGHT:
                    if (currentDirection == Direction.LEFT){
                        break;
                    }
                    currentDirection = direction;
                    directionSpeedX = 1;
                    directionSpeedY = 0;
                    break;
            }
        }
    }

    private void lowerHealth(){
        lives -= 1;
        if (lives < 0){
            terminateGame();
        }
    }

    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(GRID_SIZE);
        int y = random.nextInt(GRID_SIZE);
        food = new Coordinate(x, y);
    }

    private void foodConsumed() {
        spawnFood();
        playerScore += SCOREADD;
    }

    private boolean locationIsValid() {
        long currentTime = System.currentTimeMillis();
        if (nextLocationX < 0 || nextLocationX >= GRID_SIZE || nextLocationY < 0 || nextLocationY >= GRID_SIZE) {
            return false;
        }

        if (currentTime - startTime < SAFE_TIME) {
            return true;
        }

        for (Coordinate coordinate : snake) {
            if (nextLocationX == coordinate.getX() && nextLocationY == coordinate.getY()) {
                return false;
            }
        }
        return true;
    }

    private final Runnable moveSnakeRunnable = new Runnable() {
        @Override
        public void run() {
            MqttService.publishMsgID("lcd", String.valueOf(playerScore));
            Log.d("Cobra", "run");
            update();
        }
    };

}