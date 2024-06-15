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
    private int direction = Direction.RIGHT;
    private boolean isMoving = false;
    private Handler handler;
    private int playerScore;
    int newX;
    int newY;

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
        snake = new ArrayList<>();
        for (int i = SNAKE_LENGTH - 1; i >= 0; i--) {
            snake.add(new Coordinate(i, 0));
        }
        spawnFood();
        playerScore = 0;
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

    private void moveSnake() {
        Coordinate head = snake.get(0);
        newX = head.getX();
        newY = head.getY();
        switch (direction) {
            case Direction.UP:
                newY--;
                break;
            case Direction.DOWN:
                newY++;
                break;
            case Direction.LEFT:
                newX--;
                break;
            case Direction.RIGHT:
                newX++;
                break;
        }
        if (!locationIsValid()) {
            pauseGame();
            return;
        }
        for (Coordinate c : snake) {
            if (c.getX() == newX && c.getY() == newY) {
                pauseGame();
                return;
            }
        }
        snake.add(0, new Coordinate(newX, newY));
        if (newX == food.getX() && newY == food.getY()) {
            foodConsumed();
        } else {
            snake.remove(snake.size() - 1);
        }
        invalidate();
        if (isMoving) {
            handler.postDelayed(moveSnakeRunnable, MOVE_DELAY);
        }
    }

    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(GRID_SIZE);
        int y = random.nextInt(GRID_SIZE);
        food = new Coordinate(x, y);
    }

    public void setDirection(int direction) {
        if (Math.abs(this.direction - direction) != 2) {
            this.direction = direction;
        }
    }

    private final Runnable moveSnakeRunnable = new Runnable() {
        @Override
        public void run() {
            moveSnake();
        }
    };

    private void foodConsumed(){
        spawnFood();
        playerScore += 100;
    }

    private boolean locationIsValid(){
        for (Coordinate coordinate : snake) {
            if (newX == coordinate.getX() || newY == coordinate.getY())
                return false;
        }

        return !(newX < 0 || newX >= GRID_SIZE || newY < 0 || newY >= GRID_SIZE);
    }

}