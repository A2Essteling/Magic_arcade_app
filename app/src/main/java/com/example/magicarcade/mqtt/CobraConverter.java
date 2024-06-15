package com.example.magicarcade.mqtt;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.magicarcade.cobra.CobraGameView;
import com.example.magicarcade.cobra.Direction;
import com.example.magicarcade.objects.Controller;

public class CobraConverter {
    private static final String TAG = "CobraConverter";
    private Controller controller;

    public CobraConverter(Controller controller, LifecycleOwner lifecycleOwner) {
        this.controller = controller;
        observeController(lifecycleOwner);
    }

    private void update() {
        if (controller.getJoyY().getValue() < -15)
            CobraGameView.setDirectionSpeed(Direction.UP);
        if (controller.getJoyY().getValue() > 15)
            CobraGameView.setDirectionSpeed(Direction.DOWN);
        if (controller.getJoyX().getValue() < -15)
            CobraGameView.setDirectionSpeed(Direction.LEFT);
        if (controller.getJoyX().getValue() > 15)
            CobraGameView.setDirectionSpeed(Direction.RIGHT);
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
