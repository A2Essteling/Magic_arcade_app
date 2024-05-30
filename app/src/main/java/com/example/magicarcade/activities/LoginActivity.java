package com.example.magicarcade.activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicarcade.objects.Profile;
import com.example.magicarcade.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPhoneID;
    private Button buttonConnect;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneID = findViewById(R.id.editTextPhoneID);
        buttonConnect = findViewById(R.id.buttonConnect);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToBluetoothDevice();
            }
        });
    }

    private void connectToBluetoothDevice() {
        boolean isConnected = true;

        if (isConnected) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            String name = editTextName.getText().toString();
            startActivity(intent);
            Profile.setId(name);
            Profile.setPoints(200);
            Profile.setHighScore(0);
        } else {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Connection Failed")
                    .setMessage("Try again")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }
}