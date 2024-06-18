package com.example.magicarcade.cobra;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicarcade.mqtt.CobraConverter;
import com.example.magicarcade.mqtt.MqttService;
import com.example.magicarcade.objects.Controller;
import com.example.magicarcade.objects.Profile;

public class CobraGameActivity extends AppCompatActivity {


    private CobraGameView gameView;

    private MqttService mqttService;
    private boolean isBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        gameView = new CobraGameView(this);


        Controller controller = Profile.getController();
        CobraConverter cobraConverter = new CobraConverter(controller, this, gameView);

        setContentView(gameView);
        gameView.startGame();

        bindMqttService();
    }

    private void bindMqttService() {
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameView.terminateGame();
        Log.d("Cobra", "destroy");
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Cobra", "Service connected");
            MqttService.LocalBinder binder = (MqttService.LocalBinder) service;
            mqttService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Cobra", "Service disconnected");
            mqttService = null;
            isBound = false;
        }
    };
}
