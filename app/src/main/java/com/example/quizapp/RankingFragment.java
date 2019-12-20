package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.quizapp.Common.Common;
import com.example.quizapp.Interface.ItemClickListener;
import com.example.quizapp.Interface.RankingCallback;
import com.example.quizapp.Model.Ranking;
import com.example.quizapp.Model.Score;
import com.example.quizapp.ViewHolder.RankingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RankingFragment extends Fragment {

    View myFragment;

    RecyclerView rankinList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questionScore, rankingTbl;

    int sum = 0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_ranking, container,false);

        //Init View
        rankinList = (RecyclerView)myFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankinList.setHasFixedSize(true);


        //Because OrderByChild method of firebase will sort list with ascending
        //So we need to reverse our Recycler Data by LayoutManager
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankinList.setLayoutManager(layoutManager);

        //Now, we need to implement callback
        updateScore(Common.currentUser.getUsername(), new RankingCallback<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {

                //Update to Ranking Table
                rankingTbl.child(ranking.getUsername())
                        .setValue(ranking);
              //showRanking(); // After Upload, We Will Sort Ranking Table and Show Result
            }
        });

        //Set Adapter
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,
                R.layout.layout_ranking,
                RankingViewHolder.class,
                rankingTbl.orderByChild("score")
        ) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int i) {

                viewHolder.txt_name.setText(model.getUsername());
                viewHolder.txt_score.setText(String.valueOf(model.getScore()));

                //Fixed crash when click to item
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(),ScoreDetail.class);
                        scoreDetail.putExtra("viewUser", model.getUsername());
                        startActivity(scoreDetail);
                    }
                });

            }
        };

        adapter.notifyDataSetChanged();
        rankinList.setAdapter(adapter);

        return  myFragment;
    }

    private void updateScore(final String username, final RankingCallback<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for(DataSnapshot data:snapshot.getChildren())
                        {
                            Score score = data.getValue(Score.class);
                            sum+= Integer.parseInt(score.getScore());
                        }

                        // After Summary all Score, we need to process sum Variable here
                        //Because firebase is async db, so if the process is outside,
                        //Our 'sum' will be reset to 0
                        Ranking ranking = new Ranking(username, sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(DatabaseError dbError) {

                    }
                });
    }
}