package com.example.chess;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chess.ChessBoardView;

public class GameActivity extends AppCompatActivity {

    private ChessBoardView chessBoardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        chessBoardView = findViewById(R.id.chessBoardView);
    }
}
