package com.example.librarymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Intent add_st, add_b, issue_b, return_b, statistics, logout, update_st, delete_st, update_b, delete_b, view_st, view_bk;
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();


        Toolbar toolbar = findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.draw_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addstudentid:
                add_st = new Intent(this, add_student.class);
                startActivity(add_st);
                finish();
                break;
            case R.id.viewstudentid:
                view_st = new Intent(this, view_student.class);
                startActivity(view_st);
                break;
            case R.id.updatestudentid:
                update_st = new Intent(this, update_student.class);
                startActivity(update_st);
                finish();
                break;
            case R.id.deletestudentid:
                delete_st = new Intent(this, delete_student_info.class);
                startActivity(delete_st);
                finish();
                break;

            case R.id.addbookid:
                add_b = new Intent(this, add_book.class);
                startActivity(add_b);
                finish();
                break;
            case R.id.viewbookid:
                view_bk = new Intent(this, view_book.class);
                startActivity(view_bk);
                break;
            case R.id.updatebookid:
                update_b = new Intent(this, update_book.class);
                startActivity(update_b);
                finish();
                break;
            case R.id.deletebooktid:
                delete_b = new Intent(this, delete_book.class);
                startActivity(delete_b);
                finish();
                break;

            case R.id.statisticid:
                statistics = new Intent(this, statistic.class);
                startActivity(statistics);
                break;
            case R.id.logoutid:
                mAuth.signOut();
                logout = new Intent(this, MainActivity.class);
                startActivity(logout);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void issue_book(View view) {

        issue_b = new Intent(this, issue_book.class);
        startActivity(issue_b);
        finish();

    }

    public void return_book(View view) {
        return_b = new Intent(this, return_book.class);
        startActivity(return_b);
        finish();
    }

}