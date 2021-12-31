package com.example.splash_activity.recycle;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash_activity.R;
import com.example.splash_activity.checkDetailsWorker;
import com.example.splash_activity.member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
//import java.util.logging.Handler;

public class myOwnAdapter extends  RecyclerView.Adapter<myOwnAdapter.myOwnHolder> {



    String Type,workAS,currentName,userToken;
    Context ctx;
    ArrayList<member> members;
    member mem;
    ConstraintLayout l2;
    DatabaseReference databaseReference,databaseReference2, fvrtref,fvrt_listRef;
    FirebaseUser user;
    private DatabaseReference userRef;
    Boolean fvrtChecker= false;

    public myOwnAdapter(Context con, ArrayList<member> w){
        members =w;
        ctx=con;
    }
    @NonNull
    @Override
    public myOwnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(ctx);
        View myOwnView= layoutInflater.inflate(R.layout.my_row,parent,false);

        return new myOwnHolder(myOwnView);
    }

    @Override
    public void onBindViewHolder(@NonNull myOwnHolder holder, int position) {

        user= FirebaseAuth.getInstance() .getCurrentUser();
        String currentUserId=user.getUid();
        mem = new member();
        databaseReference2.child(currentUserId).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentName=snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final  String postKey=members.get(position).getId();
        holder.favouriteChecker(postKey);

        Toast.makeText(ctx, "data is inside the adapter onbindHolder", Toast.LENGTH_SHORT).show();
        databaseReference.child(members.get(position).getId()).child("LocationInformation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Double latitude=snapshot.child("latitude").getValue(Double.class);
                    Double longitude=snapshot.child("longitude").getValue(Double.class);
                    holder.locationAddress.setText(getCompleteAddress(latitude,longitude));
                }
            }

            private String getCompleteAddress(Double latitude, Double longitude) {
                String address="";
                Geocoder geocoder=new Geocoder(ctx, Locale.getDefault());
                try{
                    List<Address> addresses=geocoder.getFromLocation(latitude,longitude,1);
                    if(address!=null){
                        Address returnAddress=addresses.get(0);
                        StringBuilder stringBuilderReturnAddress= new StringBuilder("");
                        for(int i=0 ; i<=returnAddress.getMaxAddressLineIndex();i++){
                            stringBuilderReturnAddress.append(returnAddress.getAddressLine(i)).append("\n");
                        }
                        address=stringBuilderReturnAddress.toString();
                    }else {
                        Toast.makeText(ctx, "Address not Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(ctx, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                return address;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(members.get(position).getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    if(snapshot.hasChild("workerState")){
                        Type=snapshot.child("workerState").child("type").getValue().toString();
                        if(Type.equals("online")){
                            holder.onLineStatusView.setVisibility(View.VISIBLE);

                        }else{
                            holder.onLineStatusView.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.hireWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog= new AlertDialog.Builder(ctx,R.style.MyAlertDialogStyle);
                dialog.setTitle("Are You Sure?");
                dialog.setMessage("you want to hire "+ members.get(position).getName()+ "?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phone=members.get(position).getPhone().toString();
                        String name=members.get(position).getName().toString();
                       // sendNotification(members.get(position).getId(),members.get(position).getName());
                        sendMessage(name,phone);

                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog=dialog.create();
                alertDialog.show();
            }




        });
        holder.checkDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, checkDetailsWorker.class);
                intent.putExtra("workerKey",members.get(position).getId() );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });

        holder.l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, checkDetailsWorker.class);
                intent.putExtra("workerKey",members.get(position).getId() );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });
        holder.fvrt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fvrtChecker=true;
                 fvrtref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(fvrtChecker.equals(true)){
                            if(snapshot.child(postKey).hasChild(currentUserId)){
                                fvrtref.child(postKey).child(currentUserId).removeValue();
                                String name=members.get(position).getName();
                                delete(name);
                                fvrtChecker=false;

                            }else{
                                fvrtref.child(postKey).child(currentUserId).setValue(true);
                                mem.setUrl(members.get(position).getUrl());
                                mem.setWage(members.get(position).getWage());
                                mem.setName(members.get(position).getName());
                                mem.setCategory(members.get(position).getCategory());
                                mem.setDetails(members.get(position).getDetails());
                                String id= fvrt_listRef.push().getKey();
                                fvrt_listRef.child(id).setValue(mem);
                                fvrtChecker=false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

/*        holder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                holder.swipeRefreshLayout.setRefreshing(false);
            }
        });*/


        Toast.makeText(ctx, "data is inside the setting text view", Toast.LENGTH_SHORT).show();
        holder.name.setText(members.get(position).getName());
        holder.profession.setText(members.get(position).getCategory());
        holder.description.setText(members.get(position).getDetails());
        Picasso.get().load(members.get(position).getUrl()).into(holder.img1);
        holder.wage.setText(members.get(position).getWage());

    }

    private void delete(String name) {
        Query query=fvrt_listRef.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    snapshot1.getRef().removeValue();
                    Toast.makeText(ctx, "Favourites deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String name,String phone) {
        PermissionListener permissionListener=new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                String msg= name+"  Hired you";
                SmsManager smsManager=SmsManager.getDefault();
                smsManager.sendTextMessage(phone,null,msg,null,null);
                Toast.makeText(ctx, "Message Send", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ctx, "Please Aloow it", Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(ctx)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.SEND_SMS)
                .check();
    }



    @Override
    public long getItemId(int position) {
        //getRef(position).getKey();

        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class myOwnHolder extends RecyclerView.ViewHolder {
        TextView name,profession,description,fvrtText,locationAddress,wage;
        ImageView img1,onLineStatusView;
        Button btn,hireWorker,checkDetails;
        Button viewFvrts;
        ImageButton fvrt_btn;
        ConstraintLayout l2;

        public myOwnHolder(@NonNull View itemView) {

            super(itemView);
            user= FirebaseAuth.getInstance().getCurrentUser();
            String currentUserId=user.getUid();
            fvrtref=FirebaseDatabase.getInstance().getReference().child("favourite");
            fvrt_listRef=FirebaseDatabase.getInstance().getReference().child("favouriteList").child(currentUserId);
            userRef= FirebaseDatabase.getInstance().getReference().child("worker");
           // viewFvrts=(Button)itemView.findViewById(R.id.viewFavorites);
            locationAddress=(TextView)itemView.findViewById(R.id.locationAddress);
            wage=(TextView)itemView.findViewById(R.id.wage);
            name=(TextView)itemView.findViewById(R.id.name);
            profession=(TextView)itemView.findViewById(R.id.profession);
            description=(TextView)itemView.findViewById(R.id.details);
            img1=(ImageView)itemView.findViewById(R.id.profile_image);
            hireWorker=(Button)itemView.findViewById(R.id.hire_worker);
            checkDetails=(Button)itemView.findViewById(R.id.checkDetails);
            databaseReference= FirebaseDatabase.getInstance().getReference().child("worker");
            databaseReference2= FirebaseDatabase.getInstance().getReference().child("users");
            l2=(ConstraintLayout)itemView.findViewById(R.id.l1);
            onLineStatusView=(ImageView)itemView.findViewById(R.id.all_users_online_status);
            fvrt_btn=(ImageButton)itemView.findViewById(R.id.fvrt);
            fvrtText=(TextView)itemView.findViewById(R.id.fvrtText);
            btn=(Button)itemView.findViewById(R.id.workerDetailsClicked);

        }
        public void onClick(int Position) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx, Position + "is clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
        public void favouriteChecker(String postKey) {
            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
            String id=user.getUid();
            fvrtref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(postKey).hasChild(id)){
                        fvrt_btn.setImageResource(R.drawable.ic_baseline_turned_in_24);
                        fvrtText.setText("Remove from favourites");
                    }else{
                        fvrt_btn.setImageResource(R.drawable.ic_baseline_turnedinnot);
                        fvrtText.setText("Add to favourites");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}
