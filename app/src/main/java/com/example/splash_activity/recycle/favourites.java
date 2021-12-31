package com.example.splash_activity.recycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash_activity.R;
import com.example.splash_activity.member;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class favourites extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<member> mDataList;
    member mem;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView fvrt;
    private DatabaseReference fvrtRef, fvrt_listRef;
    private FirebaseAuth firebaseAuth;
    private String uid;
    SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mem = new member();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        recyclerView = findViewById(R.id.fvrt_recycle);
        fvrt=findViewById(R.id.fvrt);
        mDataList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            uid = user.getUid();
        }else{
            Toast.makeText(this, "Current user is null", Toast.LENGTH_SHORT).show();
        }
        fvrt_listRef=FirebaseDatabase.getInstance().getReference().child("favouriteList").child(currentUserId);
       String id= fvrt_listRef.push().getKey();

        progressDialog = new SpotsDialog(this, R.style.CustomPleaseWait);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // favouritesAdapter=new favouritesAdapter(favourites.this,mDataList);
        // recyclerView.setAdapter(favouritesAdapter);
        mAuth = FirebaseAuth.getInstance();
     // String  id = FirebaseDatabase.getInstance().getReference().child("favouriteList").child(currentUserId).push().getKey();


        FirebaseRecyclerOptions<member> options = new FirebaseRecyclerOptions.Builder<member>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("favouriteList").child(currentUserId), member.class).build();
        FirebaseRecyclerAdapter<member, viewHolder_favourites> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<member, viewHolder_favourites>(options)
                {
                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();
                       fvrt.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }
                    @Override
                    protected void onBindViewHolder(@NonNull viewHolder_favourites holder, int position, @NonNull member model) {
                     final String postKey=getRef(position).getKey();
                     holder.name.setText(model.getName());
                     holder.details.setText(model.getDetails());
                     holder.wage.setText(model.getWage());
                     holder.category.setText(model.getCategory());
                     Picasso.get().load(model.getUrl()).into(holder.urlImage);


                       // firebaseRecyclerAdapter.notifyDataSetChanged();
                    }


                    @NonNull
                    @Override
                    public viewHolder_favourites onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favoourite_list, parent, false);
                        return new viewHolder_favourites(view);

                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
}