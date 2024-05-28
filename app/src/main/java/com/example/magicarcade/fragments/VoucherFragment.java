package com.example.magicarcade.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.magicarcade.R;

public class VoucherFragment extends Fragment {

    private int playerPoints = 200;
    private TextView selectedVoucher = null;
    private int selectedVoucherCost = 0;

    public VoucherFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voucher, container, false);
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

        voucher1.setOnClickListener(v -> selectVoucher(voucher1, 100));
        voucher2.setOnClickListener(v -> selectVoucher(voucher2, 150));
        voucher3.setOnClickListener(v -> selectVoucher(voucher3, 200));
        voucher4.setOnClickListener(v -> selectVoucher(voucher4, 120));
        voucher5.setOnClickListener(v -> selectVoucher(voucher5, 250));

        redeemButton.setOnClickListener(v -> redeemVoucher());
    }

    private void selectVoucher(TextView voucher, int cost) {
        if (selectedVoucher != null) {
            selectedVoucher.setBackgroundColor(0x00000000);
        }
        selectedVoucher = voucher;
        selectedVoucherCost = cost;
        selectedVoucher.setBackgroundColor(0xFFD3D3D3);
    }

    private void redeemVoucher() {
        if (selectedVoucher == null) {
            Toast.makeText(getContext(), "Selecteer eerst een voucher.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (playerPoints >= selectedVoucherCost) {
            playerPoints -= selectedVoucherCost;
            Toast.makeText(getContext(), "Voucher aangeschaft!", Toast.LENGTH_SHORT).show();
            selectedVoucher.setBackgroundColor(0x00000000);
            selectedVoucher = null;
            selectedVoucherCost = 0;
        } else {
            Toast.makeText(getContext(), "Aankoop mislukt. Niet voldoende punten.", Toast.LENGTH_SHORT).show();
        }
    }
}
