package com.example.splash_activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.splash_activity.RegisterActivity.USER_KEY;

public class editUserProfile extends AppCompatActivity {
    EditText name,phone,mail;
    Button btn;
    private CircleImageView userprofileImageView;
    private static final int PICK_IMAGE = 1;
    private FirebaseAuth mAuth,firebaseAuth;
    private DatabaseReference mRootRef,databaseProfile;
    ProgressDialog pd;
    DocumentReference DR;
    Toolbar mToolBar;
    FirebaseFirestore db1;
    member mem;
    private Uri imageuri;
    private String myuri = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mem = new member();
        //To have the back button!!
     //   ActionBar actionBar = getSupportActionBar();
      //  actionBar.setDisplayHomeAsUpEnabled(true);

        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Picture");
        setContentView(R.layout.activity_edit_user_profile);
        userprofileImageView=(CircleImageView)findViewById(R.id.profile_image1);
        name=findViewById(R.id.userName);
        phone=findViewById(R.id.userPhone);
        mail=findViewById(R.id.userEmail);
        btn=findViewById(R.id.save_user_updates);
        mAuth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(this,R.style.MyAlertDialogStyle);
        mRootRef= FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        //mToolBar=(Toolbar)findViewById(R.id.toolbar2);
//        setSupportActionBar(mToolBar);
        this.getSupportActionBar().setTitle("Edit Profile");
       // this.getSupportActionBar().setDisplayShowHomeEnabled(true);
       this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // This callback will only be called when MyFragment is at least Started.
     //   OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
       //     @Override
         //   public void handleOnBackPressed() {
                // Handle the back button event
           //     onBackPressed();
            //}
        //};
       // this.getOnBackPressedDispatcher().addCallback(this, callback);


        databaseProfile = FirebaseDatabase.getInstance().getReference("users").child(uid);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        getUserInfo();
        userprofileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

    }


  //  @Override
    //public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      //  switch (item.getItemId()) {

        //    case android.R.id.home:

          //      Toast.makeText(this,"Back button pressed!",Toast.LENGTH_SHORT).show();
            //    this.finish();
              //  onBackPressed();
                //return true;
     //   }
       // return super.onOptionsItemSelected(item);
  //  }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == PICK_IMAGE || data != null || data.getData() != null)
                imageuri = data.getData();

            Picasso.get().load(imageuri).into(userprofileImageView);
        } catch (Exception e) {
            Toast.makeText(editUserProfile.this, "Error" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = editUserProfile.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // FirebaseUser worker=FirebaseAuth.getInstance().getCurrentUser();
        // String  currentWorker=worker.getUid();
        mRootRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String name1=snapshot.child("username").getValue().toString();
                String phone1=snapshot.child("phoneNumber").getValue().toString();
                String mail1=snapshot.child("email").getValue().toString();

                name.setText(name1);
                phone.setText(phone1);
                mail.setText(mail1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(editUserProfile.this, "Profile does not Exist"+error.getDetails(),Toast.LENGTH_SHORT).show();

            }
        });

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
                        Picasso.get().load(image).into(userprofileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void updateProfile() {
        pd.setMessage("please wait!");
        pd.show();
        String name2=name.getText().toString();
        String phone2=phone.getText().toString();
        String mail2=mail.getText().toString();

        if(imageuri !=null){

            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(imageuri));
            uploadTask = reference.putFile(imageuri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return reference.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        mem.setName(name2);
                        mem.setId(mAuth.getCurrentUser().getUid());
                        mem.setUrl(downloadUri.toString());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("url", downloadUri.toString());
                        map.put("username", name2);
                        map.put("phoneNumber", phone2);
                        map.put("email", mail2);
                        map.put("id", mAuth.getCurrentUser().getUid());
                        mRootRef.child(mAuth.getCurrentUser().getUid()).updateChildren(map);
                        pd.dismiss();
                        Intent i1 = new Intent(editUserProfile.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i1.putExtra(USER_KEY,mAuth.getCurrentUser().getUid());
                        Bundle b= ActivityOptions.makeSceneTransitionAnimation(editUserProfile.this).toBundle();
                        startActivity(i1,b);
                        finish();
                    }
                }
            });

        }else{
            HashMap<String,Object>map1=new HashMap<>();
            map1.put("username", name2);
            map1.put("email", mail2);
            map1.put("phoneNumber", phone2);

            mRootRef.child(mAuth.getCurrentUser().getUid()).updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        pd.dismiss();
                        Toast.makeText(editUserProfile.this, "Refresh your App to upDate your data", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(editUserProfile.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i1.putExtra(USER_KEY,mAuth.getCurrentUser().getUid());
                        Bundle b= ActivityOptions.makeSceneTransitionAnimation(editUserProfile.this).toBundle();
                        startActivity(i1,b);
                        finish();
                    }
                }
            });
        }



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}