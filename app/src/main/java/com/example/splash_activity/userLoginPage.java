package com.example.splash_activity;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class userLoginPage extends AppCompatActivity {
    private EditText email, password;
    private Button login,forgot_password;
    private TextView registerUser;
    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_page);
        email = findViewById(R.id.email);
        email.setHint(Html.fromHtml(getString(R.string.hintsEmail)));
        password = findViewById(R.id.password);
        password.setHint(Html.fromHtml(getString(R.string.hintsPassword)));
     //   String simple = "";
     //   String colored = "*";
      //  SpannableStringBuilder builder = new SpannableStringBuilder();

       // builder.append(simple);
      //  int start = builder.length();
     //   builder.append(colored);
     //   int end = builder.length();

      //  builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
         //       Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

     //   password.setText(builder);
     //   email.setText(builder);
        login = findViewById(R.id.login);
        registerUser = findViewById(R.id.register_user);
        mAuth = FirebaseAuth.getInstance();
        forgot_password=findViewById(R.id.forgot_password);
        pd=new ProgressDialog(this,R.style.MyAlertDialogStyle);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(userLoginPage.this, userRegistrationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Bundle b= ActivityOptions.makeSceneTransitionAnimation(userLoginPage.this).toBundle();
                startActivity(i1,b);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(userLoginPage.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(txt_email, txt_password);
                }
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail=new EditText(userLoginPage.this);
                AlertDialog.Builder passwordResetDialog= new AlertDialog.Builder(userLoginPage.this,R.style.MyAlertDialogStyle);
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
                                Toast.makeText(userLoginPage.this,"Reset Link Sent to your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(userLoginPage.this,"Error! Rest Link is not Send" + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    private void loginUser(String email, String password) {
        pd.setMessage("please wait!");
        pd.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    pd.dismiss();
                    Toast.makeText(userLoginPage.this, "update your Profile for better experience!", Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(userLoginPage.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle b= ActivityOptions.makeSceneTransitionAnimation(userLoginPage.this).toBundle();
                    startActivity(i1,b);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(userLoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}