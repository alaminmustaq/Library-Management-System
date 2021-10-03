package com.example.librarymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {

    EditText emailtext;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailtext = findViewById(R.id.editTextTextEmailAddress3);
        progressBar = findViewById(R.id.loginprogressbarid);

        mAuth = FirebaseAuth.getInstance();
    }

    public void reset(View view) {
        email = emailtext.getText().toString().trim();
        if(email.isEmpty())
        {
            emailtext.setError("Email id is required");
            emailtext.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailtext.setError("Please provide valid email address");
            emailtext.requestFocus();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(forgot_password.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(forgot_password.this, "Something went wrong, try again!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });

        }
    }

    public void Cancel(View view) {
        Intent myintent = new Intent(this,MainActivity.class);
        startActivity(myintent);
        finish();
    }
}
