package com.example.splash_activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.splash_activity.recycle.workerSideTab;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends Activity {
    private EditText email,password;
    private Button login,forgot_password;
    private TextView registerUser;
    private FirebaseAuth mAuth;
    ProgressDialog pd;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       // setTheme(R.style.Theme_AppCompat_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        email=findViewById(R.id.email);
        swipeRefreshLayout=findViewById(R.id.refresh);
        email.setHint(Html.fromHtml(getString(R.string.hintsEmail)));
        password=findViewById(R.id.password);
        password.setHint(Html.fromHtml(getString(R.string.hintsPassword)));
        login=findViewById(R.id.login);
        registerUser=findViewById(R.id.register_worker);
        mAuth=FirebaseAuth.getInstance();
        forgot_password=findViewById(R.id.forgot_password);
        pd=new ProgressDialog(this,R.style.MyAlertDialogStyle);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                Intent i1=new Intent(LoginPage.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(LoginPage.this).toBundle();
                startActivity(i1,b);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                email.setText("");
                password.setText("");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginPage.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                }else {
                    loginWorker(txt_email,txt_password);
                }
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail=new EditText(LoginPage.this);
                AlertDialog.Builder passwordResetDialog= new AlertDialog.Builder(LoginPage.this,R.style.MyAlertDialogStyle);
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your E-Mail to receive the Reset Link");
                passwordResetDialog.setView(resetMail);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //extract the email and send the rest link
                        String mail=resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(LoginPage.this,"Reset Link Sent to your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginPage.this,"Error! Rest Link is not Send" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //close the dialog
                    }
                });
                passwordResetDialog.create().show();

            }
        });
    }

    private void loginWorker(String email, String password) {
        pd.setMessage("please wait!");
        pd.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    pd.dismiss();
                    Toast.makeText(LoginPage.this,"update your Profile for better experience!",Toast.LENGTH_SHORT).show();
                    Intent i1=new Intent(LoginPage.this, workerSideTab.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle b= ActivityOptions.makeSceneTransitionAnimation(LoginPage.this).toBundle();
                    startActivity(i1,b);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginPage.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}