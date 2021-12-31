package com.example.splash_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class introductoryActivity extends AppCompatActivity {

    ImageView img;
    LottieAnimationView lottieAnimationView;
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_introductory);

       // img=findViewById(R.id.appName);
        lottieAnimationView=findViewById(R.id.lottie);
//        img.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
     /*   Intent splash = new Intent(introductoryActivity.this,Splash.class);
        startActivity(splash);
        finish();*/
        FirebaseApp.initializeApp(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference worker = FirebaseDatabase.getInstance().getReference().child("worker");
        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("users");

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(introductoryActivity.this,Splash.class);
                introductoryActivity.this.startActivity(mainIntent);
                introductoryActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);


     /*   if (user != null) {
            worker.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Intent i = new Intent(introductoryActivity.this, workerSideTab.class);
                        startActivity(i);
                    } else {
                        Intent intent = new Intent(introductoryActivity.this, MainActivity.class);
                        startActivity(intent);

                    }

                }
                @Override
                public void onCancelled (DatabaseError databaseError){

                }

            });
        }else{
            Intent i = new Intent(introductoryActivity.this, WorkerAndUser.class);
            startActivity(i);
        }
        finish();*/
    }
}