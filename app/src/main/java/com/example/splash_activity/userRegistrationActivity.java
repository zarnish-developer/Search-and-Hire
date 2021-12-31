package com.example.splash_activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class userRegistrationActivity extends AppCompatActivity {

    private EditText username,password,email,phoneNumber;
    private   Button Register,LoginUser;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    ProgressDialog pd;
    private int KeyDel;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        username=findViewById(R.id.userName);
        username.setHint(Html.fromHtml(getString(R.string.username)));
        phoneNumber=findViewById(R.id.phoneNumber);
        phoneNumber.setHint(Html.fromHtml(getString(R.string.phone)));
        password=findViewById(R.id.password);
        password.setHint(Html.fromHtml(getString(R.string.hintsPassword)));
        email=findViewById(R.id.email);
        email.setHint(Html.fromHtml(getString(R.string.hintsEmail)));
        LoginUser=findViewById(R.id.login_user);
        Register=findViewById(R.id.register);
        mRootRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(this,R.style.MyAlertDialogStyle);

        LoginUser.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(userRegistrationActivity.this,userLoginPage.class);
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(userRegistrationActivity.this).toBundle();
                startActivity(i1,b);
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username1=username.getText().toString();

                String password1=password.getText().toString();
                String email1=email.getText().toString();
                String phn=phoneNumber.getText().toString();
                if(TextUtils.isEmpty(username1) ||
                         TextUtils.isEmpty(password1) || TextUtils.isEmpty(email1) || TextUtils.isEmpty(phn))
                {
                    Toast.makeText(userRegistrationActivity.this, "Empty credentials!",Toast.LENGTH_SHORT).show();
                }else if(password1.length() < 6)
                {
                    Toast.makeText(userRegistrationActivity.this , "password too short", Toast.LENGTH_SHORT).show();
                }else if(phn.length() < 10)
                {
                    Toast.makeText(userRegistrationActivity.this , "enter a correct Phone Number", Toast.LENGTH_SHORT).show();
                }else
                {
                    RegisterUser(username1,email1,password1,phn);
                }

            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                phoneNumber.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_DEL)
                            KeyDel = 1;
                        return false;
                    }
                });

                if (KeyDel == 0) {
                    int len = phoneNumber.getText().length();
                    if(len == 4) {
                        phoneNumber.setText(phoneNumber.getText() + "-");
                        phoneNumber.setSelection(phoneNumber.getText().length());
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

    private void RegisterUser(String username2,  String email2, String password2,String phn2) {

        pd.setMessage("please wait!");
        pd.show();
        mAuth.createUserWithEmailAndPassword(email2,password2).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("username",username2);
                map.put("email",email2);
                map.put("phoneNumber",phn2);
                map.put("id",mAuth.getCurrentUser().getUid());
                mRootRef.child("users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            pd.dismiss();
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(userRegistrationActivity.this, "Token not existing", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            // Get new FCM registration token
                                            String token = task.getResult();
                                            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("token").setValue(token);
                                        }
                                    });
                            Toast.makeText(userRegistrationActivity.this,"update your Profile for better experience!",Toast.LENGTH_SHORT).show();
                            Intent i1=new Intent(userRegistrationActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Bundle b= ActivityOptions.makeSceneTransitionAnimation(userRegistrationActivity.this).toBundle();
                            startActivity(i1,b);
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(userRegistrationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}