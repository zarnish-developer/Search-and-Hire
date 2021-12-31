package com.example.splash_activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.splash_activity.recycle.workerSideTab;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class  Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference worker = FirebaseDatabase.getInstance().getReference().child("worker");
        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("users");



        if (user != null) {
            worker.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Intent i = new Intent(Splash.this, workerSideTab.class);
                        startActivity(i);
                    } else {
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);

                    }

                }
                @Override
                public void onCancelled (DatabaseError databaseError){

                }

            });
        }else{
            Intent i = new Intent(Splash.this, WorkerAndUser.class);
            startActivity(i);
        }
        finish();



    }
    //thread.start();

//}

}