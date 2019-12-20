package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.quizapp.Model.Score;
import com.example.quizapp.ViewHolder.ScoreDetailViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScoreDetail extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference ques_score;

    RecyclerView scoreList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Score, ScoreDetailViewHolder> adapter;

    String viewUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        //Firebase
        db = FirebaseDatabase.getInstance();
        ques_score = db.getReference("Question_Score");

        //View
        scoreList = (RecyclerView)findViewById(R.id.scoreList);
        scoreList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(layoutManager);

        if (getIntent() != null)
        viewUser = getIntent().getStringExtra("viewUser");
        if (!viewUser.isEmpty())
        loadScoreDetail(viewUser);
    }

    private void loadScoreDetail(final String viewUser) {
        adapter = new FirebaseRecyclerAdapter<Score, ScoreDetailViewHolder>(
                Score.class,
                R.layout.score_detail_layout,
                ScoreDetailViewHolder.class,
                ques_score.orderByChild("user").equalTo(viewUser)
        ) {
            @Override
            protected void populateViewHolder(ScoreDetailViewHolder scoreDetailViewHolder, Score score, int i) {
                scoreDetailViewHolder.txt_name.setText(score.getCategoryName());
                scoreDetailViewHolder.txt_score.setText(score.getScore());
            }
        };
        adapter.notifyDataSetChanged();
        scoreList.setAdapter(adapter);
    }
}