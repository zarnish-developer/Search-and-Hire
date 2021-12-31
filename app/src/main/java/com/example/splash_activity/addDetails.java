package com.example.splash_activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.splash_activity.recycle.workerSideTab;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class addDetails extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    String details,wage;
    int KeyDel;
    Button next;
    TextInputEditText e1,e2;
    member mem;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mem = new member();
        setContentView(R.layout.activity_add_details);

        e1=findViewById(R.id.additionalDetails);
        e2=findViewById(R.id.wage);

        next=findViewById(R.id.next1);
        e1.setHint(Html.fromHtml(getString(R.string.details)));
        e2.setHint(Html.fromHtml(getString(R.string.wage)));
        details=e1.getText().toString();
        mRootRef= FirebaseDatabase.getInstance().getReference("worker");
        mAuth= FirebaseAuth.getInstance();
        swipeRefreshLayout=findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                e1.setText("");
                e2.setText("");
            swipeRefreshLayout.setRefreshing(false);
            }
        });
        e2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                e2.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_DEL)
                            KeyDel = 1;
                        return false;
                    }
                });

                if (KeyDel == 0) {
                    int len = e2.getText().length();
                    if(len == 4) {
                        e2.setText(e2.getText() + "-");
                        e2.setSelection(e2.getText().length());
                    }
                } else { KeyDel = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                wage=e2.getText().toString();
                details=e1.getText().toString();
                if((TextUtils.isEmpty(details)) || (TextUtils.isEmpty(wage))){
                    Toast.makeText(addDetails.this, "First enter the credentials", Toast.LENGTH_SHORT).show();
                }else{
                    mem.setDetails(details);
                    mem.setWage(wage);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("details",details);
                    map.put("wage",wage);
                    // String uid=getIntent().getStringExtra(RegisterActivity.USER_KEY);
                    mRootRef.child(mAuth.getCurrentUser().getUid()).updateChildren(map);
                    Intent i1=new Intent(addDetails.this, workerSideTab.class);
                    Bundle b= ActivityOptions.makeSceneTransitionAnimation(addDetails.this).toBundle();
                    startActivity(i1,b);
                    finish();

                }

            }
        });
    }

    public void btnCurrentLocation(View view) {
        Intent map=new Intent(addDetails.this, MapsActivity.class);
        startActivity(map);
    }
}