package com.example.magicarcade.cobra;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicarcade.mqtt.CobraConverter;
import com.example.magicarcade.objects.Controller;
import com.example.magicarcade.objects.Profile;

public class CobraGameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CobraGameView gameView = new CobraGameView(this);

        Controller controller = Profile.getController();
        CobraConverter cobraConverter = new CobraConverter(controller, this);

        setContentView(gameView);
        gameView.startGame();
    }
}
