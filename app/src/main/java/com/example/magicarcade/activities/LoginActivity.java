package com.example.magicarcade.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicarcade.R;
import com.example.magicarcade.mqtt.MqttService;
import com.example.magicarcade.objects.Profile;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPhoneID;
    private Button buttonConnect;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneID = findViewById(R.id.editTextPhoneID);
        buttonConnect = findViewById(R.id.buttonConnect);

        startMqttService();
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToBluetoothDevice();
            }
        });


    }
    private void startMqttService() {
        Log.d(TAG, "Starting MqttService");
        Intent serviceIntent = new Intent(this, MqttService.class);
        startService(serviceIntent);
    }

    // Handle login logic and navigate to MainActivity
    private void onLoginSuccess() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void connectToBluetoothDevice() {
        boolean isConnected = MqttService.getState();
        if (isConnected) {
            String name = editTextName.getText().toString();
            int ID = Integer.parseInt(editTextPhoneID.getText().toString());
            MqttService.subscribe(editTextPhoneID.getText().toString()+"/#");

            // Set profile information
            Profile.setUserID(name);
            Profile.setControllerID(ID);
            Profile.setPoints(200);
            Profile.setHighScore(0);

            // Navigate to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Connection Failed")
                    .setMessage("Try again")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }
}
