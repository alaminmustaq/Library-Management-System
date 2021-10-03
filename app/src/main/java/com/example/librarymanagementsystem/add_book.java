package com.example.librarymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class add_book extends AppCompatActivity {
    Intent myintent;

    EditText pubtext,notext,loctext;
    AutoCompleteTextView nametext, authortext;

    String Name,Id,Author_name,Publisher,no,loc,userid;

    DatabaseReference bookref;
    FirebaseAuth mAuth;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        nametext = findViewById(R.id.booknameid);
        authortext = findViewById(R.id.authorid);
        pubtext = findViewById(R.id.publisherid);
        notext = findViewById(R.id.noofbookid);
        loctext = findViewById(R.id.locationid);

        myintent = new Intent(this,Home.class);

        progressBar = findViewById(R.id.progressbarid);

        bookref = FirebaseDatabase.getInstance().getReference("User").child(userid).child("Book");

        booksearch();
        authorsearch();
    }
    private void booksearch() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ArrayList<String> id = new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        String n = ds.child("name").getValue(String.class);
                        id.add(n);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.sugessition_view,id);
                    nametext.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        bookref.addListenerForSingleValueEvent(valueEventListener);
    }
    private void authorsearch() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ArrayList<String> id = new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        String n = ds.child("author_name").getValue(String.class);
                        id.add(n);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.sugessition_view,id);
                    authortext.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        bookref.addListenerForSingleValueEvent(valueEventListener);
    }

    public void Cancel(View view) {
        startActivity(myintent);
        finish();
    }
    public void Submit(View view) {
        Name = nametext.getText().toString();
        Author_name = authortext.getText().toString();
        Publisher = pubtext.getText().toString();
        no = notext.getText().toString();
        loc = loctext.getText().toString();
        Id = (Name+"-"+Author_name).toLowerCase(Locale.ROOT);


        if(Name.isEmpty())
        {
            nametext.setError("Book name is required");
            nametext.requestFocus();
        }
        else if(Author_name.isEmpty())
        {
            authortext.setError("Author name is required");
            authortext.requestFocus();
        }
        else if(Publisher.isEmpty())
        {
            pubtext.setError("Publisher name is required");
            pubtext.requestFocus();
        }

        else if(no.isEmpty())
        {
            notext.setError("Amount of books is required");
            notext.requestFocus();
        }
        else if(loc.isEmpty())
        {
            loctext.setError("Location of the books is required");
            loctext.requestFocus();
        }

        else {
            progressBar.setVisibility(View.VISIBLE);
            book book_data = new book(Id,Name, Author_name, Publisher, no, loc);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Book");

            databaseReference.orderByChild("id").equalTo(Id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        Toast.makeText(add_book.this, "Book Already exist!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    else
                    {
                        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Book").push();
                        myref.setValue(book_data);
                        Toast.makeText(add_book.this, "Book info stored successfully!", Toast.LENGTH_SHORT).show();
                        nametext.setText("");
                        authortext.setText("");
                        pubtext.setText("");
                        notext.setText("");
                        loctext.setText("");
                        progressBar.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}