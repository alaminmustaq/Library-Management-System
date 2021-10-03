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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {
    String name, org, email, pass;
    EditText nametext, orgtext, emailtext, passtext;
    Intent myintent;
    ProgressBar rpb;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nametext = findViewById(R.id.editTextTextPersonName);
        orgtext = findViewById(R.id.editTextTextPersonName2);
        emailtext = findViewById(R.id.editTextTextEmailAddress);
        passtext = findViewById(R.id.editTextTextPassword2);

        mAuth = FirebaseAuth.getInstance();
        rpb = findViewById(R.id.regprogressbarid);
    }

    public void register(View view) {
        name = nametext.getText().toString();
        org = orgtext.getText().toString();
        email = emailtext.getText().toString();
        pass = passtext.getText().toString();


        if (name.isEmpty()) {
            nametext.setError("Full name is required");
            nametext.requestFocus();
        } else if (org.isEmpty()) {
            orgtext.setError("Organization name is required");
            orgtext.requestFocus();
        } else if (email.isEmpty()) {
            emailtext.setError("Email address is required");
            emailtext.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailtext.setError("Please provide valid email address");
            emailtext.requestFocus();
        } else if (pass.isEmpty()) {
            passtext.setError("Password is required");
            passtext.requestFocus();
        }
        else if (pass.length()<6) {
            passtext.setError("Password must be minimum 6 characters");
            passtext.requestFocus();
        }
        else
        {
            rpb.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                User user = new User(name,org,email);
                                FirebaseDatabase.getInstance().getReference("User")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User information")
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(Register.this,"Registration is completed",Toast.LENGTH_SHORT).show();
                                            rpb.setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            Toast.makeText(Register.this,"Registration is Failed, try again",Toast.LENGTH_SHORT).show();
                                            rpb.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(Register.this,"Registration is Failed, try again",Toast.LENGTH_SHORT).show();
                                rpb.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    public void cancel(View view) {
        myintent = new Intent(this,MainActivity.class);
        startActivity(myintent);
        finish();
    }
}
