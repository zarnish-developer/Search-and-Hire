package com.example.splash_activity.recycle;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.splash_activity.R;
import com.example.splash_activity.member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class plumberRecycler extends AppCompatActivity {

    DatabaseReference databaseReference,databaseReference2,fvrtReference;
    FirebaseAuth mAuth;
    ConstraintLayout l1;
    RecyclerView r1;
    myOwnAdapter adp;
    ArrayList<member>list;
    member mem;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView onLineStatusView;
    ValueEventListener plumberQueryListener,queryListener,painterQueryListener,carpenterQueryListener,
            welderQueryListener,electricianQueryListener;
    ImageButton fvrt_btn;
    String Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plumber_recycler);
        mAuth=FirebaseAuth.getInstance();
        l1=findViewById(R.id.l1);
        fvrt_btn=findViewById(R.id.fvrt);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("worker");
        databaseReference2= FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());
        r1= (RecyclerView)findViewById(R.id.myRecycler);
        onLineStatusView=findViewById(R.id.all_users_online_status);
        list=new ArrayList<member>();
        Type = getIntent().getStringExtra("category");

        //design horizontal layout
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(plumberRecycler.this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        r1.setLayoutManager(linearLayoutManager);
        r1.setItemAnimator(new DefaultItemAnimator());
        adp=new myOwnAdapter(plumberRecycler.this,list);
        r1.setAdapter(adp);
        plumberQueryListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    mem=snapshot1.getValue(member.class);
                    Toast.makeText(plumberRecycler.this, "data is inside the for loop", Toast.LENGTH_SHORT).show();
                        list.add(mem);
                }
                adp.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        RunCode();
    }
    private void RunCode() {
     //   databaseReference.addValueEventListener(mQueryListener);
            Query query=databaseReference.orderByChild("category").equalTo(Type);
            query.addValueEventListener(plumberQueryListener);
    }
    public static class FriendsViewHolder extends RecyclerView.ViewHolder{
        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}


