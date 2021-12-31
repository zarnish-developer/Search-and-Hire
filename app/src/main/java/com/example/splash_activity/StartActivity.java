package com.example.splash_activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private ImageView iconImage;
    private LinearLayout linearLayout;
    private Button login,register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        iconImage= findViewById(R.id.icon_image);
        linearLayout = findViewById(R.id.linear_layout);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        linearLayout.animate().alpha(0f).setDuration(1);

       TranslateAnimation animation=new TranslateAnimation(0,0,0,-1500);
       animation.setDuration(1000);
       animation.setFillAfter(false);
       animation.setAnimationListener(new myOwnAnimationListener());
       iconImage.setAnimation(animation);
       register.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
           @Override
           public void onClick(View v) {
               Intent i1= new Intent(StartActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
               Bundle b= ActivityOptions.makeSceneTransitionAnimation(StartActivity.this).toBundle();
               startActivity(i1,b);
               finish();
           }
       });
        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent i2= new Intent(StartActivity.this, LoginPage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(StartActivity.this).toBundle();
                startActivity(i2,b);
               // finish();
            }
        });

    }

    private class myOwnAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            iconImage.clearAnimation();
            iconImage.setVisibility(View.INVISIBLE);
            linearLayout.animate().alpha(1f).setDuration(1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }



}