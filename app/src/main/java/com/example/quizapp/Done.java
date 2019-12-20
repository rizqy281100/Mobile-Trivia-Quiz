package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizapp.Common.Common;
import com.example.quizapp.Model.Question;
import com.example.quizapp.Model.Score;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Done extends AppCompatActivity {

    Button btnDontSMT, btnSubmit;
    TextView txtResultScore, getTxtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Question_Score");

        txtResultScore = (TextView)findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = (TextView)findViewById(R.id.txtTotalQuestion);
        progressBar = (ProgressBar)findViewById(R.id.doneProgressBar);
        btnDontSMT = (Button)findViewById(R.id.btnDontSubmit);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        btnDontSMT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        //Get Data from Bundle and Set to View
        final Bundle extra = getIntent().getExtras();
        if(extra != null)
        {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtResultScore.setText(String.format("SCORE : %d", score));
            getTxtResultQuestion.setText(String.format("PASSED : %d / %d", correctAnswer, totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int score = extra.getInt("SCORE");
                reference.child(String.format("%s_%s", Common.currentUser.getUsername(),
                        Common.categoryId))
                        .setValue(new Score(String.format("%s_%s", Common.currentUser.getUsername(),
                                Common.categoryId),
                                Common.currentUser.getUsername(),
                                String.valueOf(score),
                                Common.categoryId,
                                Common.categoryName));

                Intent intent = new Intent(Done.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
    }
}