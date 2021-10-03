package com.example.librarymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;


import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class statistic extends AppCompatActivity {

    RecyclerView recview;
    statistic_adapter adapter;
    FirebaseAuth mAuth;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        getSupportActionBar().setTitle("Statistics");

        recview = findViewById(R.id.statviewid);
        recview.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        FirebaseRecyclerOptions<issue> options =
                new FirebaseRecyclerOptions.Builder<issue>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("issue book").orderByChild("id"), issue.class)
                        .build();
        adapter = new statistic_adapter(options);
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
        if(s.isEmpty())
        {
            FirebaseRecyclerOptions<issue> options =
                    new FirebaseRecyclerOptions.Builder<issue>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("issue book").orderByChild("id"), issue.class)
                            .build();
            adapter = new statistic_adapter(options);
            adapter.startListening();
            recview.setAdapter(adapter);
        }
        else {
            FirebaseRecyclerOptions<issue> options =
                    new FirebaseRecyclerOptions.Builder<issue>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("issue book").orderByChild("idstatus").startAt(s).endAt(s + "\uf8ff"), issue.class)
                            .build();

            adapter = new statistic_adapter(options);
            adapter.startListening();
            recview.setAdapter(adapter);
        }
    }
}
