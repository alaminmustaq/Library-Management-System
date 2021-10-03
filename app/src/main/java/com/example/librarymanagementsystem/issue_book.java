package com.example.librarymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class issue_book extends AppCompatActivity {

    Intent myintent;

    String student_id, book_name, author_name, issue_date, due_date, return_date = "___", status = "Issued", key, no, Id,userid,idstatus;

    int number;

    EditText issuedatetext, duedatetext;
    AutoCompleteTextView studentidtext, booknametext, author_name_text;

    DatabaseReference bookref,studentref;
    FirebaseAuth mAuth;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_book);

        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        studentidtext = findViewById(R.id.studentid);
        booknametext = findViewById(R.id.booknameid);
        author_name_text = findViewById(R.id.authornameid);
        issuedatetext = findViewById(R.id.issuedateid);
        duedatetext = findViewById(R.id.duedateid);


        myintent = new Intent(this, Home.class);

        progressBar = findViewById(R.id.progressbarid);

        bookref = FirebaseDatabase.getInstance().getReference("User").child(userid).child("Book");
        studentref = FirebaseDatabase.getInstance().getReference("User").child(userid).child("Student");
        idsearch();
        booksearch();
        authorsearch();

    }
    private void idsearch() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ArrayList<String> id = new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        String n = ds.child("id").getValue(String.class);
                        id.add(n);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),R.layout.sugessition_view,id);
                    studentidtext.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        studentref.addListenerForSingleValueEvent(valueEventListener);
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
                    booknametext.setAdapter(adapter);
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
                    author_name_text.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        bookref.addListenerForSingleValueEvent(valueEventListener);
    }


    public void cancel(View view) {
        startActivity(myintent);
        finish();
    }

    public void submit(View view) {
        student_id = studentidtext.getText().toString();
        book_name = booknametext.getText().toString();
        author_name = author_name_text.getText().toString();
        issue_date = issuedatetext.getText().toString();
        due_date = duedatetext.getText().toString();
        idstatus = student_id+"-"+status;

        if(student_id.isEmpty())
        {
            studentidtext.setError("Id is required");
            studentidtext.requestFocus();
        }
        else if(student_id.contains("."))
        {
            studentidtext.setError("Id can not contain points('.')");
            studentidtext.requestFocus();
        }
        else if(book_name.isEmpty())
        {
            booknametext.setError("Book name is required");
            booknametext.requestFocus();
        }
        else if(author_name.isEmpty())
        {
            author_name_text.setError("Author name is required");
            author_name_text.requestFocus();
        }
        else if(issue_date.isEmpty())
        {
            issuedatetext.setError("Book name is required");
            issuedatetext.requestFocus();
        }
        else if(due_date.isEmpty())
        {
            duedatetext.setError("Author name is required");
            duedatetext.requestFocus();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            Id = (book_name + "-" + author_name).toLowerCase(Locale.ROOT);

            StringBuffer buffer = new StringBuffer(issue_date);
            buffer.reverse();

            key = student_id + "-" + book_name + "-" + author_name + "-" + "Issued";

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference stid = database.getReference().child("User").child(userid).child("Student").child(student_id);
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Toast.makeText(issue_book.this, "Student id doesn't exist!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Book");

                        databaseReference.orderByChild("id").equalTo(Id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        no = childSnapshot.child("no").getValue().toString();
                                        number = Integer.parseInt(no) - 1;
                                        if (number < 0) {
                                            Toast.makeText(issue_book.this, "insufficient amount of books", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            no = String.valueOf(Integer.parseInt(no) - 1);
                                            childSnapshot.getRef().child("no").setValue(no);
                                            issue issue = new issue(student_id, book_name, author_name, issue_date, due_date, return_date, status, key,  status+ "-" +buffer+ "-" +student_id,idstatus);
                                            DatabaseReference myRef = database.getReference("User").child(userid).child("issue book").push();
                                            myRef.setValue(issue);
                                            Toast.makeText(issue_book.this, "Book is issued successfully!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                } else {
                                    Toast.makeText(issue_book.this, "Book doesn't exist!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            stid.addListenerForSingleValueEvent(eventListener);
        }
    }
}