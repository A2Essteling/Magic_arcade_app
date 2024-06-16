package com.example.magicarcade.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.magicarcade.R;
import com.example.magicarcade.mqtt.MqttService;
import com.example.magicarcade.objects.Profile;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button buttonConnect;
    private Button buttonScan;

    private static final String TAG = "LoginActivity";
    private String IDString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        editTextName = findViewById(R.id.editTextName);
        buttonConnect = findViewById(R.id.buttonConnect);
        buttonScan = findViewById(R.id.buttonScan);

        startMqttService();
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanButtonClick();
            }
        });
    }

    private void startMqttService() {
        Log.d(TAG, "Starting MqttService");
        Intent serviceIntent = new Intent(this, MqttService.class);
        startService(serviceIntent);
    }





    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void onScanButtonClick() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR Code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("qr", "Cancelled scan");
                showErrorDialog("Scan Failed", "QR code scan was cancelled.");
            } else {
                String qrCodeContent = result.getContents();
                Log.d("qr", "Scanned QR code: " + Arrays.toString(qrCodeContent.split(":")));
                if ("MagicArcade_Controller_ID".equalsIgnoreCase(qrCodeContent.split(":")[0])) {
                    // QR code is valid, proceed to MainActivity
                    IDString = qrCodeContent.split(":")[1];
                    Log.d(TAG,"ID: <"+IDString+">");
                    showErrorDialog("QR code scanned","Controller scanned: "+IDString);
                } else {
                    showErrorDialog("Invalid QR Code", "The scanned QR code is not valid.");
                }
            }
        }
    }

    private boolean validateInput(String name, String phoneIDStr) {
        if (name.isEmpty() || phoneIDStr.isEmpty()) {
            Log.d(TAG,"empty");
            return false;
        }

        try {
            int ID = Integer.parseInt(phoneIDStr);
            // Additional validation can be added here if needed
            return ID > 0; // Assuming ID should be a positive integer
        } catch (NumberFormatException e) {
            Log.d(TAG,"parse fault"+e);
            return false;
        }
    }

    private void login() {
        boolean isConnected = MqttService.getState();
        if (isConnected) {
            String name = editTextName.getText().toString();

            if (validateInput(name, IDString)) {
                // Input is valid, initiate QR code scan
                onLoginSuccess();
            } else {
                showErrorDialog("Invalid input", "Please enter a valid name and ID.");
            }
        } else {
            showErrorDialog("Connection Failed", "Try again");
        }
    }

    private void onLoginSuccess() {
        // Set profile information
        String name = editTextName.getText().toString();
        int ID = Integer.parseInt(IDString);
        Profile.setUserID(name);
        Profile.setControllerID(ID);
        Profile.setPoints(200);
        Profile.setHighScore(0);

        // Navigate to MainActivity
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
