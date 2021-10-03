package com.example.librarymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class update_book extends AppCompatActivity {
    Intent myintent;
    Button updatebutton, showbutton;

    EditText pubtext, notext, loctext;
    AutoCompleteTextView nametext, authortext;

    String Name, Id, Author_name, Publisher, no, loc,userid;

    DatabaseReference bookref;
    FirebaseAuth mAuth;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        nametext = findViewById(R.id.booknameid);
        authortext = findViewById(R.id.authorid);
        pubtext = findViewById(R.id.publisherid);
        notext = findViewById(R.id.noofbookid);
        loctext = findViewById(R.id.locationid);

        myintent = new Intent(this, Home.class);

        updatebutton = findViewById(R.id.button4);
        showbutton = findViewById(R.id.button6);
        updatebutton.setVisibility(View.GONE);

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

    public void update(View view) {
        if (Name.isEmpty()) {
            nametext.setError("Book name is required");
            nametext.requestFocus();
        } else if (Author_name.isEmpty()) {
            authortext.setError("Author name is required");
            authortext.requestFocus();
        } else if (Publisher.isEmpty()) {
            pubtext.setError("Publisher name is required");
            pubtext.requestFocus();
        } else if (no.isEmpty()) {
            notext.setError("Amount of books is required");
            notext.requestFocus();
        } else if (loc.isEmpty()) {
            loctext.setError("Location of the books is required");
            loctext.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Book");

            databaseReference.orderByChild("id").equalTo(Id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                        Name = nametext.getText().toString();
                        Author_name = authortext.getText().toString();
                        Publisher = pubtext.getText().toString();
                        no = notext.getText().toString();
                        loc = loctext.getText().toString();
                        Id = (Name + "-" + Author_name).toLowerCase(Locale.ROOT);

                        childSnapshot.getRef().child("name").setValue(Name);
                        childSnapshot.getRef().child("author_name").setValue(Author_name);
                        childSnapshot.getRef().child("publisher").setValue(Publisher);
                        childSnapshot.getRef().child("no").setValue(no);
                        childSnapshot.getRef().child("loc").setValue(loc);
                        childSnapshot.getRef().child("id").setValue(Id);


                        Toast.makeText(update_book.this, "Book info updated successfully!", Toast.LENGTH_SHORT).show();
                        nametext.setText("");
                        authortext.setText("");
                        pubtext.setText("");
                        notext.setText("");
                        loctext.setText("");

                        updatebutton.setVisibility(View.GONE);
                        bookref = FirebaseDatabase.getInstance().getReference("User").child(userid).child("Book");

                        booksearch();
                        authorsearch();
                        progressBar.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
    }

    public void Cancel(View view) {
        startActivity(myintent);
        finish();
    }

    public void show(View view) {
        Name = nametext.getText().toString();
        Author_name = authortext.getText().toString();

        Id = (Name + "-" + Author_name).toLowerCase(Locale.ROOT);
        if (Name.isEmpty()) {
            nametext.setError("Book name is required");
            nametext.requestFocus();
        } else if (Author_name.isEmpty()) {
            authortext.setError("Author name is required");
            authortext.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Book");

            databaseReference.orderByChild("id").equalTo(Id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            Publisher = childSnapshot.child("publisher").getValue().toString();
                            no = childSnapshot.child("no").getValue().toString();
                            loc = childSnapshot.child("loc").getValue().toString();

                            pubtext.setText(Publisher);
                            notext.setText(no);
                            loctext.setText(loc);

                            Toast.makeText(update_book.this, "Book found!", Toast.LENGTH_SHORT).show();
                            updatebutton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        }
                    } else {
                        Toast.makeText(update_book.this, "No book found!", Toast.LENGTH_SHORT).show();
                        pubtext.setText("");
                        notext.setText("");
                        loctext.setText("");
                        updatebutton.setVisibility(View.GONE);
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