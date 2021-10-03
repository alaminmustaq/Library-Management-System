package com.example.librarymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class view_student extends AppCompatActivity {

    RecyclerView recview;
    myadapter adapter;
    FirebaseAuth mAuth;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        getSupportActionBar().setTitle("Student info");
        recview = findViewById(R.id.recviewid);
        recview.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        FirebaseRecyclerOptions<student> options =
                new FirebaseRecyclerOptions.Builder<student>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Student").orderByChild("id"), student.class)
                        .build();
        adapter = new myadapter(options);
        recview.setAdapter(adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.searchmenuid);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setQueryHint("Student id");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s) {
        FirebaseRecyclerOptions<student> options =
                new FirebaseRecyclerOptions.Builder<student>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Student").orderByChild("id").startAt(s).endAt(s+"\uf8ff"), student.class)
                        .build();
        adapter = new myadapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);
    }
}

