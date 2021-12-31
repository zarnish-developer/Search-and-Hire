package com.example.splash_activity.recycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.splash_activity.R;
import com.example.splash_activity.messaging.HolderComments;
import com.example.splash_activity.messaging.ModelComments;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class reviews extends Fragment {
    View v;
    private DatabaseReference referenceUIDWorkers,  referenceUsers;
    private FirebaseAuth mAuth, firebaseAuth;
    private String uid;
    RecyclerView workerNotificationRv;
    SpotsDialog progressDialog;
    DatabaseReference refs;
    TextView notanyreviewyetworkertv;
    SwipeRefreshLayout refreshLayout;
  //  ArrayList<member> members;
    ModelComments mem;
    ArrayList<ModelComments> comments;
    FirebaseRecyclerOptions<ModelComments> options,list;
    FirebaseRecyclerAdapter<ModelComments, HolderComments> adapter;

    public reviews() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_reviews, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            uid = user.getUid();
        }else{
            Toast.makeText(getActivity(), "Current user is null", Toast.LENGTH_SHORT).show();
        }
        referenceUIDWorkers = FirebaseDatabase.getInstance().getReference("worker").child(uid);
        referenceUsers=FirebaseDatabase.getInstance().getReference("users");
        refreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
        comments=new ArrayList<ModelComments>();
        mem=new ModelComments();
        progressDialog = new SpotsDialog(getActivity(), R.style.CustomPleaseWait);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

     //   refs = new DatabaseRefs();
        notanyreviewyetworkertv = v.findViewById(R.id.notanyreviewyetworkertvid);
        workerNotificationRv = v.findViewById(R.id.workerNotificationRvId);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        options = new FirebaseRecyclerOptions.Builder<ModelComments>().setQuery(referenceUIDWorkers
                .child("Reviews"), ModelComments.class).build();

        adapter = new FirebaseRecyclerAdapter<ModelComments, HolderComments>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                notanyreviewyetworkertv.setVisibility(View.GONE);
                progressDialog.dismiss();
            }

            @Override
            protected void onBindViewHolder(@NonNull HolderComments holder, int position, @NonNull ModelComments model) {
                holder.commentsTextUserTv.setText(model.getComment());
            /*    holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key=;
                        referenceUIDWorkers.child("Reviews").getKey();
                    //    referenceUIDWorkers.child(key).removeValue();
                        Toast.makeText(getContext(), "deltedddd", Toast.LENGTH_SHORT).show();
                    }
                });*/
                referenceUsers
                        .child(model.getCommenterUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name = snapshot.child("username").getValue().toString();
                                String image = snapshot.child("url").getValue().toString();
                                    comments.add(snapshot.getValue(ModelComments.class));
                                    //comments.get(position).getComment();
                                Picasso.get().load(image).into(holder.commenterUserImageIv);
                                holder.commenterUserNameTv.setText(name);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }

            @NonNull
            @Override
            public HolderComments onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_comments_design2, parent, false);
                return new HolderComments(view);
            }
        };
        //adapter=new HolderComments(getContext(),comments);
        workerNotificationRv.setAdapter(adapter);
       // adapter=new HolderComments();
        adapter.startListening();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        workerNotificationRv.setLayoutManager(linearLayoutManager);
        workerNotificationRv.setHasFixedSize(true);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(workerNotificationRv);

    }
    ModelComments deletedcomment=null;
    List<String >delete=new ArrayList<>();
    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position =viewHolder.getAdapterPosition();
            deletedcomment= comments.get(position);
            comments.remove(position);
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            adapter.notifyItemRemoved(position);
            Snackbar.make(workerNotificationRv, (CharSequence) deletedcomment, BaseTransientBottomBar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          comments.add(position,deletedcomment);
                          adapter.notifyItemInserted(position);
                        }
                    }).show();

        }
    };


}