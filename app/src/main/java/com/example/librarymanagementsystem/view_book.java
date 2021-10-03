package com.example.librarymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class view_book extends AppCompatActivity {

    RecyclerView recview;
    bookadapter adapter;
    FirebaseAuth mAuth;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        getSupportActionBar().setTitle("Book info");

        recview = findViewById(R.id.bookviewid);
        recview.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();


        FirebaseRecyclerOptions<book> options =
                new FirebaseRecyclerOptions.Builder<book>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Book").orderByChild("name"), book.class)
                        .build();
        adapter = new bookadapter(options);
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
        searchView.setQueryHint("Book name");

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
        FirebaseRecyclerOptions<book> options =
                new FirebaseRecyclerOptions.Builder<book>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Book").orderByChild("id").startAt(s.toLowerCase(Locale.ROOT)).endAt(s.toLowerCase(Locale.ROOT)+"\uf9ff"), book.class)
                        .build();
        adapter = new bookadapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);
    }


}