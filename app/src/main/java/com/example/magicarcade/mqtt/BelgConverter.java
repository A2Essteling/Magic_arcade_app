package com.example.magicarcade.mqtt;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.magicarcade.objects.Controller;
import com.example.magicarcade.ZwevendeBelg.ZwevendeBelgGameView;

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
        if (Boolean.FALSE.equals(controller.getButton1().getValue())||Boolean.FALSE.equals(controller.getButton2().getValue())||Boolean.FALSE.equals(controller.getButtonJoy().getValue())){
            zbgv.bounce();
        }

    }

    private void observeController(LifecycleOwner owner) {
        controller.getButtonJoy().observe(owner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean buttonJoy) {
                update();
                Log.d(TAG, "Button Joy updated: " + buttonJoy);
            }
        });
        controller.getButton1().observe(owner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean button1) {
                update();
                Log.d(TAG, "Button 1 updated: " + button1);
            }
        });
        controller.getButton2().observe(owner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean button2) {
                update();
                Log.d(TAG, "Button 2 updated: " + button2);
            }
        });
    }
}
