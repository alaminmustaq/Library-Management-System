package com.example.librarymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class delete_student_info extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Intent myintent;
    String[] department;
    Spinner newspinner;

    String name, id, phone, email, gender, Department,userid;
    int radioid;

    EditText nametext, phonetext, emailtext;
    AutoCompleteTextView idtext;


    RadioGroup myradiogroup;

    DatabaseReference ref;
    FirebaseAuth mAuth;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_student_info);

        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        nametext = findViewById(R.id.nameid);
        idtext = findViewById(R.id.idid);
        emailtext = findViewById(R.id.emailid);
        phonetext = findViewById(R.id.phoneid);

        myradiogroup = findViewById(R.id.radiogroupid);

        myintent = new Intent(this, Home.class);

        department = getResources().getStringArray(R.array.Department);

        newspinner = (Spinner) findViewById(R.id.spinerid);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.department_view, R.id.departmentspinnerid, department);
        newspinner.setAdapter(adapter);
        newspinner.setOnItemSelectedListener(this);


        progressBar = findViewById(R.id.progressbarid);

        ref = FirebaseDatabase.getInstance().getReference("User").child(userid).child("Student");
        idsearch();
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
                    idtext.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    public void Delete(View view) {

        id = idtext.getText().toString();
        if(id.isEmpty())
        {
            idtext.setError("Id is required");
            idtext.requestFocus();
        }
        else if(id.contains("."))
        {
            idtext.setError("Id can not contain points('.')");
            idtext.requestFocus();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference idref = database.getReference().child("User").child(userid).child("Student").child(id);
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        idref.removeValue();
                        Toast.makeText(delete_student_info.this, "Student info is removed from database!", Toast.LENGTH_SHORT).show();
                        idtext.setText("");
                        ref = FirebaseDatabase.getInstance().getReference("User").child(userid).child("Student");
                        idsearch();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(delete_student_info.this, "Student id not found!", Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);
                    }
                    nametext.setText("");
                    emailtext.setText("");
                    phonetext.setText("");
                    radioid = R.id.maleid;
                    Department = "CSE";
                    myradiogroup.check(radioid);
                    newspinner.setSelection(getIndex(newspinner, Department));
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            idref.addListenerForSingleValueEvent(eventListener);
        }
    }

    public void show(View view) {

        id = idtext.getText().toString();
        if(id.isEmpty())
        {
            idtext.setError("Id is required");
            idtext.requestFocus();
        }
        else if(id.contains("."))
        {
            idtext.setError("Id can not contain points('.')");
            idtext.requestFocus();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference idref = database.getReference().child("User").child(userid).child("Student").child(id);
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        nametext.setText("");
                        emailtext.setText("");
                        phonetext.setText("");
                        radioid = R.id.maleid;
                        Department = "CSE";
                        Toast.makeText(delete_student_info.this, "Student id not found!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(delete_student_info.this, "Student id found!", Toast.LENGTH_SHORT).show();
                        name = snapshot.child("name").getValue().toString();
                        email = snapshot.child("email").getValue().toString();
                        phone = snapshot.child("phone").getValue().toString();
                        gender = snapshot.child("gender").getValue().toString();
                        Department = snapshot.child("department").getValue().toString();

                        nametext.setText(name);
                        emailtext.setText(email);
                        phonetext.setText(phone);
                        if (gender.equals("Male")) {
                            radioid = R.id.maleid;
                        } else {
                            radioid = R.id.femaleid;
                        }

                    }
                    myradiogroup.check(radioid);
                    newspinner.setSelection(getIndex(newspinner, Department));
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            idref.addListenerForSingleValueEvent(eventListener);
        }
    }

    private int getIndex(Spinner newspinner, String department) {
        for (int i = 0; i < newspinner.getCount(); i++) {
            if (newspinner.getItemAtPosition(i).toString().equalsIgnoreCase(department)) {
                return i;
            }
        }
        return 0;
    }

    public void Cancel(View view) {
        startActivity(myintent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Department = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}