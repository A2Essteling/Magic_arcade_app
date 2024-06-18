package com.example.magicarcade.ZwevendeBelg;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicarcade.mqtt.BelgConverter;
import com.example.magicarcade.objects.Controller;
import com.example.magicarcade.objects.Profile;

public class ZwevendeBelgGameActivity extends AppCompatActivity {
    //private ZwevendeBelgGameView gameView;
    private ZwevendeBelgGameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //gameView = new ZwevendeBelgGameView(this);
        gameView = new ZwevendeBelgGameView(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Controller controller = Profile.getController();
        BelgConverter belgConverter = new BelgConverter(controller, this, gameView);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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