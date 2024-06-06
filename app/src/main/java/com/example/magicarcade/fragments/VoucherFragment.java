package com.example.magicarcade.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.magicarcade.objects.Profile;
import com.example.magicarcade.R;
import com.example.magicarcade.Voucher;

import java.util.List;

public class VoucherFragment extends Fragment {

    private Voucher selectedVoucher = null;
    private TextView pointsTextView;
    private LinearLayout boughtVouchersLayout;
    private PopupWindow popupWindow;

    public VoucherFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voucher, container, false);
        pointsTextView = view.findViewById(R.id.textViewPoints);
        updatePointsText();
        boughtVouchersLayout = view.findViewById(R.id.boughtVouchersLayout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Voucher voucher1 = new Voucher("Gratis cola", 100, R.drawable.barcode);
        Voucher voucher2 = new Voucher("Gratis ijsje", 120, R.drawable.barcode);
        Voucher voucher3 = new Voucher("Gratis friet", 150, R.drawable.barcode);
        Voucher voucher4 = new Voucher("50% korting op een speeltje", 200, R.drawable.barcode);
        Voucher voucher5 = new Voucher("Gratis fastpass voor 1 attractie", 250, R.drawable.barcode);


        setupVoucher(view, R.id.voucher1, voucher1);
        setupVoucher(view, R.id.voucher2, voucher2);
        setupVoucher(view, R.id.voucher3, voucher3);
        setupVoucher(view, R.id.voucher4, voucher4);
        setupVoucher(view, R.id.voucher5, voucher5);

        Button redeemButton = view.findViewById(R.id.redeemButton);
        redeemButton.setOnClickListener(v -> redeemVoucher());
    }

    private void setupVoucher(View parentView, int textViewId, Voucher voucher) {
        TextView voucherTextView = parentView.findViewById(textViewId);
        voucherTextView.setText(voucher.getName() + " - " + voucher.getCost() + " points");

        boolean isBought = Profile.getVouchers().contains(voucher);

        if (isBought) {
            voucherTextView.setOnClickListener(v -> showBarcodePopup(voucher.getName(), voucher.getImageResId()));
        } else {
            voucherTextView.setOnClickListener(v -> selectVoucher(voucherTextView, voucher));
        }
    }


    private void selectVoucher(TextView voucherTextView, Voucher voucher) {
        if (selectedVoucher != null && selectedVoucher != voucher) {
            selectedVoucher = null;
            voucherTextView.setBackgroundColor(0x00000000);
        }
        if (selectedVoucher == voucher) {
            selectedVoucher = null;
            voucherTextView.setBackgroundColor(0x00000000);
        } else {
            selectedVoucher = voucher;
            voucherTextView.setBackgroundColor(0xFFD3D3D3);

        }
    }

    private void showBarcodePopup(String voucherName, int imageResId) {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_barcode, null);

        ImageView imageView = popupView.findViewById(R.id.barcodeImageView);
        imageView.setImageResource(imageResId);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        popupWindow.showAtLocation(popupView, android.view.Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    private void redeemVoucher() {
        if (selectedVoucher == null) {
            Toast.makeText(getContext(), "Selecteer eerst een voucher.", Toast.LENGTH_SHORT).show();
            return;
        }

        int playerPoints = Profile.getPoints();
        if (playerPoints >= selectedVoucher.getCost()) {
            Profile.setPoints(playerPoints - selectedVoucher.getCost());
            Profile.addVoucher(selectedVoucher);
            Toast.makeText(getContext(), "Voucher aangeschaft!", Toast.LENGTH_SHORT).show();
            updateBoughtVouchersDisplay();
            selectedVoucher = null;
        } else {
            Toast.makeText(getContext(), "Aankoop mislukt. Niet voldoende punten.", Toast.LENGTH_SHORT).show();
        }
        updatePointsText();
    }

    private void updatePointsText() {
        if (pointsTextView != null) {
            pointsTextView.setText(getString(R.string.points_on_account) + " " + Profile.getPoints());
        }
    }

    private void updateBoughtVouchersDisplay() {
        boughtVouchersLayout.removeAllViews();
        List<Voucher> boughtVouchers = Profile.getVouchers();
        for (Voucher voucher : boughtVouchers) {
            View voucherView = LayoutInflater.from(getContext()).inflate(R.layout.item_voucher, boughtVouchersLayout, false);
            TextView textView = voucherView.findViewById(R.id.voucherNameTextView);
            ImageView imageView = voucherView.findViewById(R.id.voucherImageView);
            textView.setText(voucher.getName());
            imageView.setImageResource(voucher.getImageResId());

            voucherView.setOnClickListener(v -> showBarcodePopup(voucher.getName(), voucher.getImageResId()));

            boughtVouchersLayout.addView(voucherView);
        }
    }
}
