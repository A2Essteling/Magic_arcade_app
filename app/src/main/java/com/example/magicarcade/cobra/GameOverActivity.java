package com.example.magicarcade.cobra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicarcade.R;
import com.example.magicarcade.activities.MainActivity;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        int score = getIntent().getIntExtra("SCORE", 0);

        TextView scoreText = findViewById(R.id.score_text);
        scoreText.setText("Score: " + score);

        Button restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                intent.putExtra("SHOW_HOME_FRAGMENT", true);
                startActivity(intent);
                finish();
            }
        });
    }
}