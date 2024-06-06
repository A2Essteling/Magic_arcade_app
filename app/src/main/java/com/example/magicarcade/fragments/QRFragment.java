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
    import com.google.zxing.integration.android.IntentResult;
    import com.google.zxing.integration.android.IntentIntegrator;
    import com.example.magicarcade.cobra.CobraGameActivity;
import com.example.magicarcade.ZwevendeBelg.ZwevendeBelgGameActivity;

    public class QRFragment extends Fragment {

        public QRFragment() {

        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_qr, container, false);

            Button scanButton = view.findViewById(R.id.QRscanbutton);

            scanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onScanButtonClick();
                }
            });

            return view;
        }

        private void onScanButtonClick() {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(QRFragment.this);
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
                } else {
                    String qrCodeContent = result.getContents();
                    Log.d("qr", "Scanned QR code: " + qrCodeContent);
                    if ("Cobra".equals(qrCodeContent)) {
                        Intent intent = new Intent(getContext(), CobraGameActivity.class);
                        startActivity(intent);
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
