package com.example.librarymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import java.util.regex.Pattern;

public class update_student extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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
        setContentView(R.layout.activity_update_student);

        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        nametext = findViewById(R.id.nameid);
        idtext = findViewById(R.id.idid);
        emailtext = findViewById(R.id.emailid);
        phonetext = findViewById(R.id.phoneid);

        myradiogroup = findViewById(R.id.radiogroupid);

        myintent = new Intent(this, Home.class);

        department = getResources().getStringArray(R.array.Department);

        newspinner = findViewById(R.id.spinerid);
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

    public void update(View view) {
        userid = mAuth.getCurrentUser().getUid();
        int radiobuttonid = myradiogroup.getCheckedRadioButtonId();
        if (radiobuttonid == R.id.maleid) {
            gender = "Male";
        } else if (radiobuttonid == R.id.femaleid) {
            gender = "Female";
        }
        name = nametext.getText().toString();
        id = idtext.getText().toString();
        email = emailtext.getText().toString();
        phone = phonetext.getText().toString();

        if (id.isEmpty()) {
            idtext.setError("Id is required");
            idtext.requestFocus();
        } else if (id.contains(".")) {
            idtext.setError("Id can not contain points('.')");
            idtext.requestFocus();
        } else if (name.isEmpty()) {
            nametext.setError("Name is required");
            nametext.requestFocus();
        } else if (email.isEmpty()) {
            emailtext.setError("Email id is required");
            emailtext.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailtext.setError("Please provide valid email address");
            emailtext.requestFocus();
        } else if (phone.isEmpty()) {
            phonetext.setError("Phone number is required");
            phonetext.requestFocus();
        } else if (!Pattern.compile("[0][1][3-9][0-9]{8}").matcher(phone).matches()) {
            phonetext.setError("Please provide valid phone number");
            phonetext.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            student student_data = new student(id, name, phone, email, gender, Department);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference idref = database.getReference().child("User").child(userid).child("Student").child(id);
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Toast.makeText(update_student.this, "Student id not found!", Toast.LENGTH_SHORT).show();

                    } else {
                        DatabaseReference myRef = database.getReference("User");
                        myRef.child(userid).child("Student").child(id).setValue(student_data);
                        Toast.makeText(update_student.this, "Student info is updated successfully!", Toast.LENGTH_SHORT).show();
                        idtext.setText("");
                        nametext.setText("");
                        emailtext.setText("");
                        phonetext.setText("");
                        radioid = R.id.maleid;
                        Department = "CSE";

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

    public void Cancel(View view) {
        startActivity(myintent);
        finish();
    }

    public void show(View view) {

        userid = mAuth.getCurrentUser().getUid();
        id = idtext.getText().toString();
        if (id.isEmpty()) {
            idtext.setError("Id is required");
            idtext.requestFocus();
        } else if (id.contains(".")) {
            idtext.setError("Id can not contain points('.')");
            idtext.requestFocus();
        } else {
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
                        Toast.makeText(update_student.this, "Student id not found!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(update_student.this, "Student id found!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Department = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}