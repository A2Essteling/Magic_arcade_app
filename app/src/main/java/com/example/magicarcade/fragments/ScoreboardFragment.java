package com.example.magicarcade.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.magicarcade.R;
import com.example.magicarcade.adapters.ScoreAdapter;
import com.example.magicarcade.objects.PlayerScore;
import com.example.magicarcade.objects.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ScoreboardFragment extends Fragment {

    private static final List<PlayerScore> scoreList = new ArrayList<>();
    private ListView highScoreListView;
    private static ScoreAdapter scoreAdapter;

    private Handler scoreUpdateHandler;

    public ScoreboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scoreboard, container, false);
        highScoreListView = view.findViewById(R.id.highScoreListView);

        scoreAdapter = new ScoreAdapter(requireContext(), scoreList);
        highScoreListView.setAdapter(scoreAdapter);

        scoreUpdateHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    String name = msg.getData().getString("name");
                    int score = msg.getData().getInt("score");
                    addScore(name, score);
                }
            }
        };

//        addScore("storm", 120); // test data voor highscores
//        addScore("storm2", 100);
//        addScore("storm3", 12);
//        addScore("storm4", 0);
//        addScore("storm4", 20);
//        addScore("storm4", 60);

        addScore(Profile.getUserID(), Profile.getHighScore());

        return view;
    }

    public static void addScore(String name, int score) {
        scoreList.removeIf(playerScore -> playerScore.getName().equalsIgnoreCase(name));
        scoreList.add(new PlayerScore(name, score));
        sortScores();
        scoreAdapter.notifyDataSetChanged();
    }

    private static void sortScores() {
        Collections.sort(scoreList, new Comparator<PlayerScore>() {
            @Override
            public int compare(PlayerScore o1, PlayerScore o2) {
                return Integer.compare(o2.getScore(), o1.getScore()); // Descending order
            }
        });
    }
    public Handler getScoreUpdateHandler() {
        return scoreUpdateHandler;
    }
}