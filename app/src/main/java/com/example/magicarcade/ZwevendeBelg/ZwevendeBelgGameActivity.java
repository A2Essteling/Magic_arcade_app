package com.example.magicarcade.ZwevendeBelg;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ZwevendeBelgGameActivity extends AppCompatActivity {
    private ZwevendeBelgGameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new ZwevendeBelgGameView(this);
        setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }
}