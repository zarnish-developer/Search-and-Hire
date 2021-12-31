package com.example.splash_activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class WorkerAndUser extends Activity {

    CardView user,worker;
    private DatabaseReference mRootRef;
    Button userButton,workerButton;
    private FirebaseAuth mAuth;
    SwipeRefreshLayout swipeRefreshLayout;


    private Runnable mTicker;
    private Handler mHandler;

    private boolean mTickerStopped = false;

    String mFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_and_user);


     user=findViewById(R.id.userId);
     worker=findViewById(R.id.workerId);
     userButton=findViewById(R.id.userButton);
     workerButton=findViewById(R.id.workerButton);
     swipeRefreshLayout=findViewById(R.id.refresh);

        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();
        final boolean isDarkModeOn= sharedPreferences.getBoolean("isDarkModeOn",false);
        if(isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent i1=new Intent (WorkerAndUser.this,UserStartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle b=ActivityOptions.makeSceneTransitionAnimation(WorkerAndUser.this).toBundle();
                startActivity(i1,b);
            }
        });
        worker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent i2=new Intent (WorkerAndUser.this,StartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle b=ActivityOptions.makeSceneTransitionAnimation(WorkerAndUser.this).toBundle();
                startActivity(i2,b);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void buttonClicked(View view) {
            if (view.getId() == R.id.workerButton)
            {
                Intent i2=new Intent (WorkerAndUser.this,StartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(WorkerAndUser.this).toBundle();
                startActivity(i2,b);

              //  finish();
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void buttonClicked1(View view) {

        if (view.getId() == R.id.userButton)
        {

            Intent i1=new Intent (WorkerAndUser.this,UserStartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle b= ActivityOptions.makeSceneTransitionAnimation(WorkerAndUser.this).toBundle();
            startActivity(i1,b);
            //finish();
        }
    }



}