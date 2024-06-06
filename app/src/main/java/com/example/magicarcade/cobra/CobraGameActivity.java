package com.example.magicarcade.cobra;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class CobraGameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CobraGameView gameView = new CobraGameView(this);
        setContentView(gameView);
        gameView.startGame();
    }
}
