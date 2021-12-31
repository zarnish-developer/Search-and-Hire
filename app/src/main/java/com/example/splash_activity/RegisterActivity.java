package com.example.splash_activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    public static final String USER_KEY ="user_key" ;

    private EditText name,password,email,contact;
    private ImageView imageView;
    private Uri imageuri;
    private String myuri = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private   Button Register,LoginWorker;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    ProgressDialog pd;
    DocumentReference DR;
    String id;
    FirebaseFirestore db1;
    member mem;
    private int KeyDel;
    SwipeRefreshLayout swipeRefreshLayout;
    private static final int PICK_IMAGE = 1;

    String[] workerNames={"Plumber","carpenter","Welder","Electrician","painter"};
    int[] workerIcon={R.drawable.plumber1,R.drawable.carpenter,R.drawable.welder1,R.drawable.elec,R.drawable.painter};

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mem = new member();
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Picture");

        setContentView(R.layout.activity_register);
      //   workerSpinner=findViewById(R.id.spinner);
       // username=findViewById(R.id.userName);

        name=findViewById(R.id.name);
        name.setHint(Html.fromHtml(getString(R.string.username)));
        password=findViewById(R.id.password);
        password.setHint(Html.fromHtml(getString(R.string.hintsPassword)));
        contact=findViewById(R.id.contact);
        contact.setHint(Html.fromHtml(getString(R.string.phone)));
        email=findViewById(R.id.email);
        email.setHint(Html.fromHtml(getString(R.string.hintsEmail)));
        LoginWorker=findViewById(R.id.login_user);
        imageView = findViewById(R.id.profile_image);
        Register=findViewById(R.id.register);
        mRootRef= FirebaseDatabase.getInstance().getReference("worker");
        mAuth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(this,R.style.MyAlertDialogStyle);
        swipeRefreshLayout=findViewById(R.id.refresh);
     //  category1=new category();

        //workerSpinner.setOnItemSelectedListener(RegisterActivity.this);
        //categoryAdapter adp=new categoryAdapter(this,workerNames,workerIcon);
        //workerSpinner.setAdapter(adp);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        try {
            id = user.getUid();
            DR = db1.collection("worker").document(id);
        } catch (Exception exception) {

        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                name.setText("");
                password.setText("");
                contact.setText("");
                email.setText("");

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        LoginWorker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(RegisterActivity.this,LoginPage.class);
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle();
                startActivity(i1,b);
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  String username1=username.getText().toString();
                String name1=name.getText().toString();
                String password1=password.getText().toString();
                String email1=email.getText().toString();
                String contact1=contact.getText().toString();
               // .setSpinner(workerSpinner.getSelectedItemPosition());
            //   Integer item=workerSpinner.getSelectedItemPosition();

              //  Toast.makeText(RegisterActivity.this, "selected item is" + item ,Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(name1) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(email1))
                {
                    Toast.makeText(RegisterActivity.this, "Empty credentials!",Toast.LENGTH_SHORT).show();
                }else if(password1.length() < 6)
                {
                    Toast.makeText(RegisterActivity.this , "password too short", Toast.LENGTH_SHORT).show();
                }else if (contact1.length() < 11){
                   // Toast.makeText(RegisterActivity.this , "please enter a contact number", Toast.LENGTH_SHORT).show();
                }else if(imageuri==null)
                {
                    Toast.makeText(RegisterActivity.this, "Please select some profile picture!",Toast.LENGTH_SHORT).show();
                } else
                    {

                        RegisterUser(name1,email1,password1,contact1,imageuri);
                    }

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        contact.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                contact.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_DEL)
                            KeyDel = 1;
                        return false;
                    }
                });

                if (KeyDel == 0) {
                    int len = contact.getText().length();
                    if(len == 4) {
                        contact.setText(contact.getText() + "-");
                        contact.setSelection(contact.getText().length());
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

            Picasso.get().load(imageuri).into(imageView);
        } catch (Exception e) {
            Toast.makeText(RegisterActivity.this, "Error" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = RegisterActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void RegisterUser( String name2, String email2, String password2,String contact2,Uri imageuri) {

        pd.setMessage("please wait!");
        pd.show();
        //category1.setSpinner(item);
        mAuth.createUserWithEmailAndPassword(email2,password2).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
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
                            mem.setId(id);
                            Toast.makeText(RegisterActivity.this, "id is going inside member", Toast.LENGTH_SHORT).show();
                            mem.setEmail(email2);
                            mem.setPhone(contact2);
                            mem.setUrl(downloadUri.toString());
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("name", name2);
                            map.put("email", email2);
                            map.put("password", password2);
                            map.put("phone", contact2);
                            map.put("url", downloadUri.toString());
                            map.put("id", mAuth.getCurrentUser().getUid());
                            mRootRef.child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        FirebaseMessaging.getInstance().getToken()
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(RegisterActivity.this, "Token not existing", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }

                                                        // Get new FCM registration token
                                                        String token = task.getResult();
                                                        FirebaseDatabase.getInstance().getReference().child("worker").child(mAuth.getCurrentUser().getUid()).child("token").setValue(token);
                                                    }
                                                });
                                        Intent i1 = new Intent(RegisterActivity.this, select_category.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i1.putExtra(USER_KEY,mAuth.getCurrentUser().getUid());
                                        Bundle b= ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle();
                                        startActivity(i1,b);
                                       // finish();
                                    }
                                }
                            });
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}