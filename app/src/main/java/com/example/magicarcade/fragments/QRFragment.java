package com.example.magicarcade.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.magicarcade.R;
import com.example.magicarcade.ZwevendeBelg.ZwevendeBelgGameActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRFragment extends Fragment {

    public QRFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr, container, false);

        // Find the button in the inflated view
        Button scanButton = view.findViewById(R.id.QRscanbutton);

        // Set an OnClickListener on the button
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanButtonClick();
            }
        });

        return view;
    }

    // Method to be called when the button is clicked
    private void onScanButtonClick() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(QRFragment.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR Code");
        integrator.setCameraId(0);  // Use a specific camera of the device
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
                // Handle the case where no QR code was found
                Log.d("qr","Cancelled scan");
            } else {
                // Handle the scanned QR code result
                String qrCodeContent = result.getContents();
                Log.d("qr","Scanned QR code: " + qrCodeContent);
                // You can now use the scanned QR code content
                if ("Cobra".equalsIgnoreCase(qrCodeContent)) {
                    // Start the GameActivity to open the game
                    Intent intent = new Intent(getActivity(), ZwevendeBelgGameActivity.class);
                    startActivity(intent);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
