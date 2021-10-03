package com.example.librarymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText username;
    TextView forgot_password;
    TextInputLayout password;

    ProgressBar lpb;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.editTextTextEmailAddress3);
        password = findViewById(R.id.editTextTextPassword);

        mAuth = FirebaseAuth.getInstance();
        lpb = findViewById(R.id.loginprogressbarid);
        forgot_password = findViewById(R.id.textView5);

    }

    public void login(View view) {

        String email = username.getText().toString();
        String pass = password.getEditText().getText().toString();

        if (email.isEmpty()) {
            username.setError("Email address is required");
            username.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            username.setError("Please provide valid email address");
            username.requestFocus();
        } else if (pass.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
        } else if (pass.length() < 6) {
            password.setError("Password must be minimum 6 characters");
            password.requestFocus();
        }
        else {
            lpb.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        lpb.setVisibility(View.GONE);
                        Intent home = new Intent(MainActivity.this, Home.class);
                        startActivity(home);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Failed to log in, please check your email id and password!", Toast.LENGTH_SHORT).show();
                        lpb.setVisibility(View.GONE);
                    }
                }
            });

        }

        }

        public void register (View view){
            Intent register = new Intent(this, Register.class);
            startActivity(register);
            finish();
        }

    public void forgot_password(View view) {
        Intent myintent = new Intent(this, com.example.librarymanagementsystem.forgot_password.class);
        startActivity(myintent);
        finish();
    }
}