package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.capstoneproject.fragments.alerts.AlarmHandler;
import com.example.capstoneproject.fragments.alerts.AlertsFragment;
import com.example.capstoneproject.fragments.NewsFragment;
import com.example.capstoneproject.fragments.portfolio.portfolio;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlarmHandler alarmHandler = new AlarmHandler(this);
        //cancel any existing alarm managers if there are any
        alarmHandler.cancelAlarmManager();
        //create and set new alarm managers for notifications
        alarmHandler.setAlarmManager();


        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_alerts:
                        //Toast.makeText(MainActivity.this, "Alerts!", Toast.LENGTH_SHORT).show();
                        fragment = new AlertsFragment();
                        break;
                    case R.id.action_portfolio:
                        //Toast.makeText(MainActivity.this, "Portfolio!", Toast.LENGTH_SHORT).show();

                        //change this to your fragment, for example:
                        fragment = new portfolio();


                        break;
                    case R.id.action_news:
                    default:
                        //Toast.makeText(MainActivity.this, "News!", Toast.LENGTH_SHORT).show();

                        fragment = new NewsFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();



                return true;
            }

        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_alerts);
    }


    //pop/remove stack once we have successfully gone back to previous fragment
    public void onBackPressed()
    {

        FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        fm.popBackStack();
    }
}