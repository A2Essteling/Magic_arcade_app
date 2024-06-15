package com.example.magicarcade.cobra;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class CobraGameView extends View {

    private static final int GRID_SIZE = 40;
    private static final int CELL_SIZE = 20;
    private static final int SNAKE_LENGTH = 3;
    private static final int MOVE_DELAY = 300;

    private ArrayList<Coordinate> snake;
    private Coordinate food;
    private Direction direction;

    private Handler handler;
    private int playerScore;

    //movement
    private boolean isMoving = false;
    private int directionSpeedX;
    private int directionSpeedY;
    int nextLocationX;
    int nextLocationY;


    public CobraGameView(Context context) {
        super(context);
        init();
    }

    public CobraGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CobraGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        playerScore = 0;
        directionSpeedX = 1;
        directionSpeedY = 0;
        direction = Direction.RIGHT;

        snake = new ArrayList<>();
        for (int i = SNAKE_LENGTH - 1; i >= 0; i--) {
            snake.add(new Coordinate(i, 0));
        }
        spawnFood();

        handler = new Handler();
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
        handler.postDelayed(moveSnakeRunnable, MOVE_DELAY);
    }

    public void pauseGame() {
        isMoving = false;
        handler.removeCallbacks(moveSnakeRunnable);
    }

    private void update() {
        Coordinate head = snake.get(0);

        nextLocationX = head.getX() + directionSpeedX;
        nextLocationY = head.getY() + directionSpeedY;

        if (!locationIsValid()) {
            pauseGame();
            return;
        }
        for (Coordinate c : snake) {
            if (c.getX() == nextLocationX && c.getY() == nextLocationY) {
                pauseGame();
                return;
            }
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

    private void setDirectionSpeed(Direction direction) {
        switch (direction){
            case UP:
                directionSpeedX = 0;
                directionSpeedY = 1;
                break;
            case DOWN:
                directionSpeedX = 0;
                directionSpeedY = -1;
                break;
            case LEFT:
                directionSpeedX = 1;
                directionSpeedY = 0;
                break;
            case RIGHT:
                directionSpeedX = -1;
                directionSpeedY = 0;
                break;

        }
    }

    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(GRID_SIZE);
        int y = random.nextInt(GRID_SIZE);
        food = new Coordinate(x, y);
    }

    private void foodConsumed(){
        spawnFood();
        playerScore += 100;
    }

    private boolean locationIsValid(){
        for (Coordinate coordinate : snake) {
            if (nextLocationX == coordinate.getX() || nextLocationY == coordinate.getY())
                return false;
        }

        return !(nextLocationX < 0 || nextLocationX >= GRID_SIZE || nextLocationY < 0 || nextLocationY >= GRID_SIZE);
    }

    private final Runnable moveSnakeRunnable = new Runnable() {
        @Override
        public void run() {
            update();
        }
    };

}