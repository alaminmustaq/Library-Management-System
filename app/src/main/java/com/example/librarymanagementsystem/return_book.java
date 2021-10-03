package com.example.librarymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class return_book extends AppCompatActivity {

    Intent myintent;

    Button return_button;

    String student_id, book_name, author_name, issue_date, due_date, return_date, status = "Returned", key, no,idstatus;
    String Id,userid;

    EditText returndatetext;
    AutoCompleteTextView studentidtext, booknametext, authornametext;
    TextView issuedatetext, duedatetext;

    DatabaseReference bookref,studentref;
    FirebaseAuth mAuth;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);

        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        studentidtext = findViewById(R.id.studentid);
        booknametext = findViewById(R.id.booknameid);
        authornametext = findViewById(R.id.authornameid);
        returndatetext = findViewById(R.id.returndateid);

        issuedatetext = findViewById(R.id.issuedateid);
        duedatetext = findViewById(R.id.duedateid);

        returndatetext.setFocusable(true);
        return_button = findViewById(R.id.button4);
        return_button.setVisibility(View.GONE);

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
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.sugessition_view,id);
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
                    authornametext.setAdapter(adapter);
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

    public void submit(View view) {
        progressBar.setVisibility(View.VISIBLE);
        idstatus=student_id+"-"+status;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("issue book").child(Id);

        databaseReference.child("return_date").setValue(return_date);
        databaseReference.child("status").setValue(status);
        StringBuffer buffer = new StringBuffer(issue_date);
        buffer.reverse();
        databaseReference.child("id").setValue(status+ "-" +buffer+ "-" +student_id);
        databaseReference.child("key").setValue(student_id + "-" + book_name + "-" + author_name + "-" + status);
        databaseReference.child("idstatus").setValue(idstatus);



        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Book");

        databaseReference.orderByChild("id").equalTo((book_name + "-" + author_name).toLowerCase(Locale.ROOT)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        no = childSnapshot.child("no").getValue().toString();

                        no = String.valueOf(Integer.parseInt(no) + 1);
                        childSnapshot.getRef().child("no").setValue(no);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Toast.makeText(this, "Book Returned successfully!", Toast.LENGTH_SHORT).show();
        return_button.setVisibility(View.GONE);
        studentidtext.setFocusableInTouchMode(true);
        booknametext.setFocusableInTouchMode(true);
        authornametext.setFocusableInTouchMode(true);
        returndatetext.setFocusableInTouchMode(true);
        studentidtext.setText("");
        booknametext.setText("");
        authornametext.setText("");
        returndatetext.setText("");
        issuedatetext.setText("");
        duedatetext.setText("");
        progressBar.setVisibility(View.GONE);

    }

    public void show(View view) {
        student_id = studentidtext.getText().toString();
        book_name = booknametext.getText().toString();
        author_name = authornametext.getText().toString();
        return_date = returndatetext.getText().toString();

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
            authornametext.setError("Author name is required");
            authornametext.requestFocus();
        }
        else if(return_date.isEmpty())
        {
            returndatetext.setError("Author name is required");
            returndatetext.requestFocus();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            key = student_id + "-" + book_name + "-" + author_name + "-" + "Issued";


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("issue book");

            databaseReference.orderByChild("key").equalTo(key).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            issue_date = childSnapshot.child("issue_date").getValue().toString();
                            due_date = childSnapshot.child("due_date").getValue().toString();
                            issuedatetext.setText("Issue date: "+issue_date);
                            duedatetext.setText("Due date: "+due_date);

                            key = student_id + "-" + book_name + "-" + author_name + "-" + status;
                            Id = childSnapshot.getKey();

                            studentidtext.setFocusable(false);
                            booknametext.setFocusable(false);
                            authornametext.setFocusable(false);
                            returndatetext.setFocusable(false);
                            Toast.makeText(return_book.this, "Record found!", Toast.LENGTH_SHORT).show();
                            return_button.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);



                        }
                    } else {
                        Toast.makeText(return_book.this, "Record not found!", Toast.LENGTH_SHORT).show();
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