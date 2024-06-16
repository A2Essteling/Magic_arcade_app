package com.example.magicarcade.cobra;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class CobraGameView extends View {

    private static final int GRID_SIZE = 40;
    private static final int CELL_SIZE = 20;
    private static final int SNAKE_LENGTH = 3;
    private static final int MOVE_DELAY = 300;
    private static Direction currentDirection;

    private ArrayList<Coordinate> snake;
    private Coordinate food;
    private Handler handler;
    private int playerScore;

    //movement
    public static boolean isMoving = false;
    private int directionSpeedX;
    private int directionSpeedY;
    int nextLocationX;
    int nextLocationY;


    public CobraGameView(Context context) {
        super(context);
        init();
    }


    private void init() {
        playerScore = 0;
        currentDirection = Direction.RIGHT;
        setDirectionSpeed(Direction.RIGHT);

        snake = new ArrayList<>();
        for (int i = SNAKE_LENGTH - 1; i >= 0; i--) {
            snake.add(new Coordinate(i + 30, 30));
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
        Log.d("Cobra", "start");
        handler.postDelayed(moveSnakeRunnable, MOVE_DELAY);
    }

    public void terminateGame() {
        isMoving = false;
        Log.d("Cobra", "pause");
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
                    currentDirection = direction;
                    directionSpeedX = 0;
                    directionSpeedY = -1;
                    break;
                case DOWN:
                    currentDirection = direction;
                    directionSpeedX = 0;
                    directionSpeedY = 1;
                    break;
                case LEFT:
                    currentDirection = direction;
                    directionSpeedX = -1;
                    directionSpeedY = 0;
                    break;
                case RIGHT:
                    currentDirection = direction;
                    directionSpeedX = 1;
                    directionSpeedY = 0;
                    break;
            }
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
        playerScore += 1;
    }

    private boolean locationIsValid() {
        if (nextLocationX < 0 || nextLocationX >= GRID_SIZE || nextLocationY < 0 || nextLocationY >= GRID_SIZE) {
            return false;
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
            Log.d("Cobra", "run");
            update();
        }
    };

}