package com.example.splash_activity.recycle;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.splash_activity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Availability extends Fragment {
  private DatabaseReference mRootRef, databaseProfile;
  private FirebaseAuth mAuth, firebaseAuth;
  private TextView name;
  private TextView category;
  private TextView email;
  private TextView details;
  private TextView phone;
  private Switch aSwitch;
  private CircleImageView profileImageView;
  private Uri imageUri ;
  private String myUri="";
  private String uid;
  private StorageTask uploadTask;
  private StorageReference storageReference;

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public Availability() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment Availability.
   */
  // TODO: Rename and change types and number of parameters
  public static Availability newInstance(String param1, String param2) {
    Availability fragment = new Availability();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }

  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_availability, container, false);
    profileImageView=(CircleImageView)view.findViewById(R.id.profile_image);
    aSwitch = (Switch) view.findViewById(R.id.switchCase);
    name = (TextView) view.findViewById(R.id.nameDetails);
    category =(TextView) view.findViewById(R.id.Profession);
    phone = (TextView) view.findViewById(R.id.phone);
    details=(TextView)view.findViewById(R.id.Details);
    email = (TextView) view.findViewById(R.id.email);
    mRootRef = FirebaseDatabase.getInstance().getReference("worker");
    //  mAuth = FirebaseAuth.getInstance();
    firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    if(user!=null){
      uid = user.getUid();
    }else{
      Toast.makeText(getActivity(), "Current user is null", Toast.LENGTH_SHORT).show();
    }
    databaseProfile = FirebaseDatabase.getInstance().getReference("worker").child(uid);
    storageReference = FirebaseStorage.getInstance().getReference().child("Profile Picture");
    databaseProfile.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        String name1 = (String) dataSnapshot.child("name").getValue();
        String category1 = (String) dataSnapshot.child("category").getValue();
        String contact1 = (String) dataSnapshot.child("phone").getValue();
        String email1 = (String) dataSnapshot.child("email").getValue();
        String detail1=(String)dataSnapshot.child("details").getValue();
        name.setText(name1);
        category.setText(category1);
        phone.setText(contact1);
        email.setText(email1);
        details.setText(detail1);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });


    getUserInfo();

    aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

          aSwitch.setText("Online");
          upDateWorkerStatus("online");
          Toast.makeText(getContext(), "Online", Toast.LENGTH_SHORT).show();

        } else {
          aSwitch.setText("Offline");
          upDateWorkerStatus("offline");
          Toast.makeText(getContext(), "Offline", Toast.LENGTH_SHORT).show();
        }
      }
    });

    return view;
  }

  private void getUserInfo() {

    databaseProfile.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists() && snapshot.getChildrenCount()>0)
        {
          if(snapshot.hasChild("url"))
          {
            String image= snapshot.child("url").getValue().toString();
            Picasso.get().load(image).into(profileImageView);
          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

  }

  private void upDateWorkerStatus(String state) {
    String saveCurrentTime,saveCurrentDate;
    Calendar calForDate=Calendar.getInstance();
    SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyy");
    saveCurrentDate=currentDate.format(calForDate.getTime());

    Calendar calForTime=Calendar.getInstance();
    SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
    saveCurrentTime=currentTime.format(calForTime.getTime());

    firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String uid = user.getUid();
    Map currentState= new HashMap();
    currentState.put("time", saveCurrentTime);
    currentState.put("date", saveCurrentDate);
    currentState.put("type", state);
    mRootRef.child(uid).child("workerState").updateChildren(currentState);
  }

}