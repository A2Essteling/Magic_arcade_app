package com.example.magicarcade.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.magicarcade.R;

public class HomeFragment extends Fragment {

    private int playerPoints = 200;
    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setupAttractionListeners(rootView);
        displayUserPoints(rootView);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("WrongViewCast")
    private void setupAttractionListeners(View rootView) {
        FrameLayout attraction1Layout = rootView.findViewById(R.id.attraction1);
        FrameLayout attraction2Layout = rootView.findViewById(R.id.attraction2);
        FrameLayout attraction3Layout = rootView.findViewById(R.id.attraction3);
        FrameLayout attraction4Layout = rootView.findViewById(R.id.attraction4);
        FrameLayout attraction5Layout = rootView.findViewById(R.id.attraction5);
        FrameLayout attraction6Layout = rootView.findViewById(R.id.attraction6);

        attraction1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 1 clicked");
            }
        });

        attraction2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 2 clicked");
            }
        });

        attraction3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 3 clicked");
            }
        });

        attraction4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 4 clicked");
            }
        });

        attraction5Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 5 clicked");
            }
        });

        attraction6Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 6 clicked");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void displayUserPoints(View rootView) {
        TextView pointsTextView = rootView.findViewById(R.id.textViewPoints);
        pointsTextView.setText(getString(R.string.points_on_account) + ": " + playerPoints);
    }
}
