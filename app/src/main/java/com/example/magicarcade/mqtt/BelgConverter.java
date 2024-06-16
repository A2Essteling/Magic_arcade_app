package com.example.magicarcade.mqtt;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.magicarcade.objects.Controller;
import com.example.magicarcade.zwevendeBelg.ZwevendeBelgGameActivity;
import com.example.magicarcade.zwevendeBelg.ZwevendeBelgGameView;

public class BelgConverter implements MqttConverter{
    private static final String TAG = "BelgConverter";
    private Controller controller;
    private ZwevendeBelgGameView zbgv;

    public BelgConverter(Controller controller, LifecycleOwner lifecycleOwner, ZwevendeBelgGameView zbgv) {
        this.controller = controller;
        this.zbgv = zbgv;
        observeController(lifecycleOwner);
    }

    @Override
    public void update() {
        if (controller.getButton1().getValue()){
            zbgv.setBirdY();
        }

    }

    private void observeController(LifecycleOwner owner) {
        controller.getJoyX().observe(owner, new Observer<Double>() {
            @Override
            public void onChanged(Double joyX) {
                update();
                Log.d(TAG, "Joy X updated: " + joyX);
            }
        });
        controller.getJoyY().observe(owner, new Observer<Double>() {
            @Override
            public void onChanged(Double joyY) {
                update();
                Log.d(TAG, "Joy Y updated: " + joyY);
            }
        });
        controller.getButtonJoy().observe(owner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean buttonJoy) {
                update();
//                Log.d(TAG, "Button Joy updated: " + buttonJoy);
            }
        });
        controller.getButton1().observe(owner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean button1) {
                update();
//                Log.d(TAG, "Button 1 updated: " + button1);
            }
        });
        controller.getButton2().observe(owner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean button2) {
                update();
//                Log.d(TAG, "Button 2 updated: " + button2);
            }
        });
    }






}
