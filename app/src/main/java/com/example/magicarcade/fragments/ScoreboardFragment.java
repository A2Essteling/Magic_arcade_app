package com.example.magicarcade.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.magicarcade.objects.PlayerScore;
import com.example.magicarcade.R;
import com.example.magicarcade.adapters.ScoreAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.magicarcade.objects.Profile;

public class ScoreboardFragment extends Fragment {

    private ListView highScoreListView;
    private ScoreAdapter scoreAdapter;
    private List<PlayerScore> scoreList = new ArrayList<>();

    public ScoreboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scoreboard, container, false);
        highScoreListView = view.findViewById(R.id.HighScore);

        scoreAdapter = new ScoreAdapter(getContext(), scoreList);
        highScoreListView.setAdapter(scoreAdapter);

//        addScore("storm", 120); // test data voor highscores
//        addScore("storm2", 100);
//        addScore("storm3", 12);
//        addScore("storm4", 0);
//        addScore("storm4", 20);
//        addScore("storm4", 60);

        addScore(Profile.getUserID(), Profile.getHighScore());

        return view;
    }

    public void addScore(String name, int score) {
        scoreList.removeIf(playerScore -> playerScore.getName().equalsIgnoreCase(name));
        scoreList.add(new PlayerScore(name, score));
        sortScores();
        scoreAdapter.notifyDataSetChanged();
    }

    private void sortScores() {
        Collections.sort(scoreList, new Comparator<PlayerScore>() {
            @Override
            public int compare(PlayerScore o1, PlayerScore o2) {
                return Integer.compare(o2.getScore(), o1.getScore()); // Descending order
            }
        });
    }
}