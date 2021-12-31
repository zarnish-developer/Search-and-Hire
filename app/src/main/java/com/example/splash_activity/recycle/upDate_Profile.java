package com.example.splash_activity.recycle;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splash_activity.R;
import com.example.splash_activity.member;
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

public class upDate_Profile extends AppCompatActivity {

    EditText name,phone,profession,details,mail;
    Button btn;
    private CircleImageView profileImageView;
    private static final int PICK_IMAGE = 1;
    private FirebaseAuth mAuth,firebaseAuth;
    private DatabaseReference mRootRef,databaseProfile;
    ProgressDialog pd;
    DocumentReference DR;
    private int KeyDel;

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
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Picture");
        setContentView(R.layout.activity_up_date__profile);
        profileImageView=(CircleImageView)findViewById(R.id.profile_image);
        name=findViewById(R.id.user_name);
        phone=findViewById(R.id.phone);
        profession=findViewById(R.id.Profession_details);
        details=findViewById(R.id.add_details);
        mail=findViewById(R.id.email);
        btn=findViewById(R.id.save_updates);
        mAuth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(this,R.style.MyAlertDialogStyle);
        mRootRef= FirebaseDatabase.getInstance().getReference("worker");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        databaseProfile = FirebaseDatabase.getInstance().getReference("worker").child(uid);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        getUserInfo();
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                phone.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_DEL)
                            KeyDel = 1;
                        return false;
                    }
                });

                if (KeyDel == 0) {
                    int len = phone.getText().length();
                    if(len == 4) {
                        phone.setText(phone.getText() + "-");
                        phone.setSelection(phone.getText().length());
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
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == PICK_IMAGE || data != null || data.getData() != null)
                imageuri = data.getData();

            Picasso.get().load(imageuri).into(profileImageView);
        } catch (Exception e) {
            Toast.makeText(upDate_Profile.this, "Error" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = upDate_Profile.this.getContentResolver();
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
                String name1=snapshot.child("name").getValue().toString();
                String phone1=snapshot.child("phone").getValue().toString();
                String profession1=snapshot.child("category").getValue().toString();
                String mail1=snapshot.child("email").getValue().toString();

                name.setText(name1);
                phone.setText(phone1);
                profession.setText(profession1);
                mail.setText(mail1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(upDate_Profile.this, "Profile does not Exist"+error.getDetails(),Toast.LENGTH_SHORT).show();

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
                        Picasso.get().load(image).into(profileImageView);
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
        String profession2=profession.getText().toString();
        String phone2=phone.getText().toString();
        String mail2=mail.getText().toString();
        String details2=details.getText().toString();

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
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    mem.setName(name2);
                    mem.setPhone(phone2);
                    mem.setEmail(mail2);
                    mem.setCategory(profession2);
                    mem.setDetails(details2);
                    mem.setId(mAuth.getCurrentUser().getUid());
                    mem.setUrl(downloadUri.toString());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("url", downloadUri.toString());
                    map.put("id", mAuth.getCurrentUser().getUid());
                    mRootRef.child(mAuth.getCurrentUser().getUid()).updateChildren(map);
                }
            }
        });

        HashMap<String,Object>map1=new HashMap<>();
        map1.put("name", name2);
        map1.put("email", mail2);
        map1.put("details", details2);
        map1.put("phone", phone2);
        map1.put("category",profession2);
        mRootRef.child(mAuth.getCurrentUser().getUid()).updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(upDate_Profile.this, "Refresh your App to upDate your data", Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(upDate_Profile.this, workerSideTab.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i1.putExtra(USER_KEY,mAuth.getCurrentUser().getUid());
                    Bundle b= ActivityOptions.makeSceneTransitionAnimation(upDate_Profile.this).toBundle();
                    startActivity(i1,b);
                    finish();
                }
            }
        });

    }
}