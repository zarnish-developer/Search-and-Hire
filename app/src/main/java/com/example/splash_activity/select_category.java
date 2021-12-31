package com.example.splash_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class select_category extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Handler sliderHandler=new Handler();
    private ViewPager2 viewPager2;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    Spinner workerSpinner;
    member mem;
    Button next;
    String[] workerNames={"Plumber","Carpenter","Welder","Electrician","Painter"};
    int[] workerIcon={R.drawable.plumber1,R.drawable.carpenter,R.drawable.welder1,R.drawable.elec,R.drawable.painter};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mem = new member();
        setContentView(R.layout.activity_select_category);

         workerSpinner=findViewById(R.id.spinner);
         next=findViewById(R.id.next);
        viewPager2=findViewById(R.id.viewPagerImageSlider);
        mRootRef= FirebaseDatabase.getInstance().getReference("worker");
        mAuth= FirebaseAuth.getInstance();
        workerSpinner.setOnItemSelectedListener(this);
        categoryAdapter adp=new categoryAdapter(this,workerNames,workerIcon);
        workerSpinner.setAdapter(adp);
        // here i'm preparing list of images from drawable,
        //you can also get it from  api as well.
        List<sliderItem> sliderItems=new ArrayList<>();
        sliderItems.add(new sliderItem(R.drawable.logo));
        sliderItems.add(new sliderItem(R.drawable.image1));
        sliderItems.add(new sliderItem(R.drawable.image2));
        sliderItems.add(new sliderItem(R.drawable.image3));
        sliderItems.add(new sliderItem(R.drawable.image4));
      //  sliderItems.add(new sliderItem(R.drawable.image5));
        viewPager2.setAdapter(new sliderAdapter(sliderItems,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {

                float r=1- Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,2000);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next=new Intent(select_category.this,addDetails.class);
                startActivity(next);
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(this,"if you will not select any category ,default will be selected",Toast.LENGTH_SHORT).show();
        mem.setCategory(workerNames[position]);

        Toast.makeText(this,"category going inside member class", Toast.LENGTH_SHORT).show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("category", workerNames[position]);
       // String uid=getIntent().getStringExtra(RegisterActivity.USER_KEY);
        mRootRef.child(mAuth.getCurrentUser().getUid()).updateChildren(map);

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private Runnable sliderRunnable=new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,2000);
    }
}