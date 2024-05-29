package com.example.magicarcade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ScoreAdapter extends ArrayAdapter<PlayerScore> {
    public ScoreAdapter(@NonNull Context context, @NonNull List<PlayerScore> scores) {
        super(context, 0, scores);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_score, parent, false);
        }

        PlayerScore score = getItem(position);

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView scoreTextView = convertView.findViewById(R.id.scoreTextView);

        nameTextView.setText(score.getName());
        scoreTextView.setText(String.valueOf(score.getScore()));

        return convertView;
    }
}
