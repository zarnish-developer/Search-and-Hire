package com.example.splash_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash_activity.animation.SetAnimatedScroll;
import com.example.splash_activity.messaging.HolderComments;
import com.example.splash_activity.messaging.ModelComments;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import at.markushi.ui.CircleButton;
import dmax.dialog.SpotsDialog;

public class checkDetailsWorker extends AppCompatActivity {

    String workerKEY;
    FirebaseAuth mAUth;
    DatabaseReference refs,referenceWorkers,referenceUsers;
    RecyclerView workerDetailsRv;
    ImageView workerDetailsImageIv;
    TextView workerDetailsNameTv, workerDetailsProfessionTv, workerDetailsContactTv,
            workerDetailsSpecificationTv,details, noCommentsYetTv;
    TextInputEditText workerCommentsDetailEt;
    NestedScrollView workerDetailsScrollViewSv;
    SetAnimatedScroll setAnimatedScroll;
    SpotsDialog progressDialog;
    CircleButton workerDetailSendBtn;
    FirebaseRecyclerOptions<ModelComments> options;
    FirebaseRecyclerAdapter<ModelComments, HolderComments> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_details_worker);
       progressDialog = new SpotsDialog(checkDetailsWorker.this, R.style.CustomPleaseWait);
       progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        getIds();
        mAUth = FirebaseAuth.getInstance();
        referenceWorkers= FirebaseDatabase.getInstance().getReference().child("worker");
        referenceUsers= FirebaseDatabase.getInstance().getReference().child("users");
        workerKEY = getIntent().getStringExtra("workerKey");
        refs = FirebaseDatabase.getInstance().getReference().child("worker").child(workerKEY);
       setAnimatedScroll = new SetAnimatedScroll(getApplicationContext(), workerDetailsScrollViewSv, workerDetailsImageIv);
        setAnimatedScroll.ListenScroll();

       refs.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   try {
                       new Handler().postDelayed(() -> progressDialog.dismiss(), 2000);
                       String imgW = snapshot.child("url").getValue().toString();
                       String nameW = snapshot.child("name").getValue().toString();
                       String professionW = snapshot.child("category").getValue().toString();
                       String contactW = snapshot.child("phone").getValue().toString();
                       String specificationW = snapshot.child("details").getValue().toString();


                       Picasso.get().load(imgW).into(workerDetailsImageIv);
                       workerDetailsNameTv.setText(nameW);
                       workerDetailsProfessionTv.setText(professionW);
                       workerDetailsContactTv.setText(contactW);
                       workerDetailsContactTv.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Intent intent=new Intent(Intent.ACTION_DIAL);
                               intent.setData(Uri.parse("tel:" + Uri.encode(contactW)));
                               startActivity(intent);
                           }
                       });
                       details.setText(specificationW);

                       workerDetailSendBtn.setOnClickListener(v -> {
                           String comment = workerCommentsDetailEt.getText().toString();

                           if (!comment.isEmpty()) {
                               Map<String, Object> map = new HashMap<>();
                               map.put("comment", comment);
                               map.put("commenterUid", mAUth.getCurrentUser().getUid());

                               referenceWorkers.child(workerKEY)
                                       .child("Reviews")
                                       .push()
                                       .setValue(map)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               workerCommentsDetailEt.setText("");
                                           }
                                       })
                                       .addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               workerCommentsDetailEt.setText("");
                                               Toast.makeText(checkDetailsWorker.this, "Failed to comment", Toast.LENGTH_SHORT).show();
                                           }
                                       });
                           }
                       });


                   } catch (Exception e) {
                       Toast.makeText(checkDetailsWorker.this, e.getMessage(), Toast.LENGTH_LONG).show();
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       }); ;

    }
    private void getIds() {
        workerDetailsScrollViewSv = findViewById(R.id.workerDetailsScrollViewSvId);
        workerDetailsImageIv = findViewById(R.id.workerDetailsImageIvId);
        workerDetailsRv = findViewById(R.id.workerDetailsRvId);
        workerDetailsNameTv = findViewById(R.id.workerDetailsNameTvId);
        workerDetailsProfessionTv = findViewById(R.id.workerDetailsProfessionTvId);
        workerDetailsContactTv = findViewById(R.id.workerDetailsContactTvId);
        workerCommentsDetailEt = findViewById(R.id.workerCommentsDetailEtId);
        details=findViewById(R.id.details);
        workerDetailSendBtn = findViewById(R.id.workerDetailSendBtnId);
        noCommentsYetTv = findViewById(R.id.noCommentsYetTvId);

    }

    @Override
    protected void onStart() {
        super.onStart();

        options = new FirebaseRecyclerOptions.Builder<ModelComments>().setQuery(referenceWorkers
                .child(workerKEY)
                .child("Reviews"), ModelComments.class).build();

        adapter = new FirebaseRecyclerAdapter<ModelComments, HolderComments>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();

                noCommentsYetTv.setVisibility(View.GONE);
            }

            @Override
            protected void onBindViewHolder(@NonNull HolderComments holder, int position, @NonNull ModelComments model) {
                holder.commentsTextUserTv.setText(model.getComment());
                referenceUsers
                        .child(model.getCommenterUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name = snapshot.child("username").getValue().toString();
                                String image = snapshot.child("url").getValue().toString();
                                Picasso.get().load(image).into(holder.commenterUserImageIv);
                               // Glide.with(getApplicationContext()).load(image).into(holder.commenterUserImageIv);
                                holder.commenterUserNameTv.setText(name);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }

            @NonNull
            @Override
            public HolderComments onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_comments_designs, parent, false);
                return new HolderComments(view);
            }
        };

        workerDetailsRv.setAdapter(adapter);
        adapter.startListening();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        workerDetailsRv.setLayoutManager(linearLayoutManager);
        workerDetailsRv.setHasFixedSize(true);
    }

}
