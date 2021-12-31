package com.example.splash_activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.splash_activity.recycle.carpenterRecycler;
import com.example.splash_activity.recycle.electricianRecycler;
import com.example.splash_activity.recycle.favourites;
import com.example.splash_activity.recycle.painterRecycler;
import com.example.splash_activity.recycle.plumberRecycler;
import com.example.splash_activity.recycle.welderRecycler;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton  imageButtonMenu,editProfile ,fvrt;
    private CardView plumber,electrician,painter,welder,carpenter;
    BottomSheetDialog sheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plumber=findViewById(R.id.plumberCard);
        plumber.setOnClickListener(this);
        electrician=findViewById(R.id.electricianCard);
        electrician.setOnClickListener(this);
        painter=findViewById(R.id.painterCard);
        painter.setOnClickListener(this);
        welder=findViewById(R.id.welderCard);
        welder.setOnClickListener(this);
        carpenter=findViewById(R.id.carpenterCard);
        carpenter.setOnClickListener(this);

        imageButtonMenu=findViewById(R.id.navigationMenu);

    imageButtonMenu.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BottomSheetMenu bottomSheet=new BottomSheetMenu();
            bottomSheet.show(getSupportFragmentManager(), "BottomSheetMenu");
            bottomSheet.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        }
    });

    plumber.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            Intent plumber=new Intent(MainActivity.this,plumberRecycler.class);
            plumber.putExtra("category","Plumber");
            Toast.makeText(MainActivity.this, "data is going from plumber 111", Toast.LENGTH_SHORT).show();
            Bundle b= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
            startActivity(plumber,b);
        }
    });
    electrician.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            Intent electrician=new Intent(MainActivity.this,plumberRecycler.class);
            electrician.putExtra("category","Electrician");
            Bundle b= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
            startActivity(electrician,b);
        }
    });

         painter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent painter= new Intent(MainActivity.this,plumberRecycler.class);
                painter.putExtra("category","Painter");
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                startActivity(painter,b);
            }
        });

        electrician.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent electrician=new Intent(MainActivity.this,plumberRecycler.class);
                electrician.putExtra("category","Electrician");
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                startActivity(electrician,b);
            }
        });
        welder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent welder=new Intent(MainActivity.this,plumberRecycler.class);
                welder.putExtra("category","Welder");
                Toast.makeText(MainActivity.this, "data is going from welder 111", Toast.LENGTH_SHORT).show();
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                startActivity(welder,b);
            }
        });
        carpenter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent carpenter=new Intent(MainActivity.this,plumberRecycler.class);
                carpenter.putExtra("category","Carpenter");
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                startActivity(carpenter,b);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.fvrtList){
            Intent fvrt=new Intent(MainActivity.this, favourites.class);

            startActivity(fvrt);
        }
        if(item.getItemId()==R.id.update_profile){
            Intent editProfile=new Intent(MainActivity.this, editUserProfile.class);
            startActivity(editProfile);
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.plumberSearch:
                Intent i1=new Intent(MainActivity.this, plumberRecycler.class);
                startActivity(i1);
                break;
            case R.id.electricianSearch:
                Intent i2=new Intent(MainActivity.this, electricianRecycler.class);
                startActivity(i2);
                break;
            case R.id.painterSearch:
                Intent i3=new Intent(MainActivity.this, painterRecycler.class);
                startActivity(i3);
                break;
            case R.id.welderSearch:
                Intent i4=new Intent(MainActivity.this, welderRecycler.class);
                startActivity(i4);
                break;
            case R.id.carpenterSearch:
                Intent i5=new Intent(MainActivity.this, carpenterRecycler.class);
                startActivity(i5);
                break;


        }
    }
}