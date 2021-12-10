package com.example.capstoneproject.fragments.portfolio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.example.capstoneproject.R;

import java.util.ArrayList;

public class achievementactivity extends AppCompatActivity {
    databaseforachievements myAchievementDB;
    portfoliostockrecycleradapter portfoliostockadapter;
    private RecyclerView recyclerview;
    ArrayList<String> achievements,completed;
    achievementrecycleradapter achievementadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievementactivity);
        myAchievementDB = new databaseforachievements(achievementactivity.this);
        achievements = new ArrayList<>();
        completed = new ArrayList<>();
        recyclerview = findViewById(R.id.achievementrecyclerresource);
        storeDatainArrays();
        achievementadapter = new achievementrecycleradapter(achievementactivity.this,achievementactivity.this,completed,achievements);
        recyclerview.setAdapter(achievementadapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(achievementactivity.this));


    }


    void storeDatainArrays() {

        Cursor cursor = myAchievementDB.readAllData();

        if (cursor.getCount() == 0) {
            //Toast.makeText(achievementactivity.this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                achievements.add(cursor.getString(1));
                completed.add(cursor.getString(3));
            }

        }

    }
}