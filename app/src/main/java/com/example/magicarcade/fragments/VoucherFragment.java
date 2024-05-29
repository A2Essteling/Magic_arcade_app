package com.example.magicarcade.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.magicarcade.Profile;
import com.example.magicarcade.R;

import java.util.ArrayList;
import java.util.List;

public class VoucherFragment extends Fragment {

    private TextView selectedVoucher = null;
    private int selectedVoucherCost = 0;
    private String selectedVoucherName = null;
    private TextView pointsTextView;
    private LinearLayout boughtVouchersLayout;

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

        TextView voucher1 = view.findViewById(R.id.voucher1);
        TextView voucher2 = view.findViewById(R.id.voucher2);
        TextView voucher3 = view.findViewById(R.id.voucher3);
        TextView voucher4 = view.findViewById(R.id.voucher4);
        TextView voucher5 = view.findViewById(R.id.voucher5);
        Button redeemButton = view.findViewById(R.id.redeemButton);

        voucher1.setOnClickListener(v -> selectVoucher(voucher1, "Gratis cola", 100));
        voucher2.setOnClickListener(v -> selectVoucher(voucher2, "Gratis ijsje",120));
        voucher3.setOnClickListener(v -> selectVoucher(voucher3, "Gratis friet",150));
        voucher4.setOnClickListener(v -> selectVoucher(voucher4, "50% korting op een speeltje",200));
        voucher5.setOnClickListener(v -> selectVoucher(voucher5, "Gratis fastpass voor 1 attractie",250));

        redeemButton.setOnClickListener(v -> redeemVoucher());
    }

    private void selectVoucher(TextView voucher, String voucherName, int cost) {
        if (selectedVoucher != null) {
            selectedVoucher.setBackgroundColor(0x00000000);
        }
        selectedVoucherName = voucherName;
        selectedVoucher = voucher;
        selectedVoucherCost = cost;
        selectedVoucher.setBackgroundColor(0xFFD3D3D3);
    }

    private void redeemVoucher() {
        if (selectedVoucher == null) {
            Toast.makeText(getContext(), "Selecteer eerst een voucher.", Toast.LENGTH_SHORT).show();
            return;
        }

        int playerPoints = Profile.getPoints();
        if (playerPoints >= selectedVoucherCost) {
            Profile.setPoints(playerPoints - selectedVoucherCost);
            Profile.addVoucher(selectedVoucherName);
            Toast.makeText(getContext(), "Voucher aangeschaft!", Toast.LENGTH_SHORT).show();
            updateBoughtVouchersDisplay();
            selectedVoucher.setBackgroundColor(0x00000000);
            selectedVoucher = null;
            selectedVoucherName = null;
            selectedVoucherCost = 0;
        } else {
            Toast.makeText(getContext(), "Aankoop mislukt. Niet voldoende punten.", Toast.LENGTH_SHORT).show();
        }
        updatePointsText();
    }

    private void updatePointsText() {
        if (pointsTextView != null) {
            pointsTextView.setText(getString(R.string.points_on_account) + ": " + Profile.getPoints());
        }
    }

    private void updateBoughtVouchersDisplay() {
        boughtVouchersLayout.removeAllViews();
        List<String> boughtVouchers = Profile.getVouchers();
        for (String voucher : boughtVouchers) {
            TextView textView = new TextView(requireContext());
            textView.setText(voucher);
            boughtVouchersLayout.addView(textView);
        }
    }
}
