package com.example.magicarcade.cobra;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicarcade.mqtt.CobraConverter;
import com.example.magicarcade.objects.Controller;
import com.example.magicarcade.objects.Profile;

public class CobraGameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        CobraGameView gameView = new CobraGameView(this);


        Controller controller = Profile.getController();
        CobraConverter cobraConverter = new CobraConverter(controller, this, gameView);

        setContentView(gameView);
        gameView.startGame();
    }
}
