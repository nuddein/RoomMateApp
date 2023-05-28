package com.nuddein.evarkadasiprojesi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import Adapter.MatchAdapter;

public class EslesmeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;
    private List<String> matchList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eslesme);

        progressBar = findViewById(R.id.match_progress_bar);
        recyclerView = findViewById(R.id.match_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        matchList = new ArrayList<>();

        matchAdapter = new MatchAdapter(this, matchList, new MatchAdapter.MatchAdapterListener() {
            @Override
            public void onDataChanged() {
                readMatches();
            }
        });
        recyclerView.setAdapter(matchAdapter);

        readMatches();
    }

    private void readMatches() {
        DatabaseReference matchReference = FirebaseDatabase.getInstance().getReference("matchRequests")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        matchReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matchList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String match = snapshot.getKey();
                    matchList.add(match);
                }
                Log.d("MatchDebug", "Number of matches: " + matchList.size());  // Log the size of the matchList
                matchAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }



}
