package com.example.splash_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Context.MODE_PRIVATE;


public class BottomSheetMenu extends BottomSheetDialogFragment {

    TextView txt1;
    LinearLayout l1,l2,l3,l4,l5;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v=inflater.inflate(R.layout.bottom_sheet_menu,container,false);
       l1=v.findViewById(R.id.delete_user_account);
        l2=v.findViewById(R.id.verify_user_account);
        l3=v.findViewById(R.id.reset_password_user_account);
        l4=v.findViewById(R.id.enable_dark_mode);
        l5=v.findViewById(R.id.logout_user_account);
        txt1=v.findViewById(R.id.enableDarkMode);
        mAuth=FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();
        final boolean isDarkModeOn= sharedPreferences.getBoolean("isDarkModeOn",false);
        if(isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
           txt1.setText("Disable Dark Mode ");
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            txt1.setText("Able Dark Mode ");
        }
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDarkModeOn){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isDarkModeOn",false);
                    editor.apply();
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();
                }
            }
        });

        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
                builder.setTitle("Are you sure to LogOut?")
                        .setPositiveButton("LogOut", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                Intent loginIntent=new Intent(getActivity() , WorkerAndUser.class);
                                startActivity(loginIntent);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userVerificationEmail();
            }
        });
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
        return v;
    }
    private void deleteAccount() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AlertDialog.Builder dialog= new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
        dialog.setTitle("Are You Sure?");
        dialog.setMessage("Deleting this account will result in completely removing your account from system and you won't be able to access the app");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(),"Account Deleted", Toast.LENGTH_SHORT).show();
                                    Intent deleteAccount=new Intent(getActivity(),WorkerAndUser.class);
                                    startActivity(deleteAccount);
                                   // finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"error occurred" + e, Toast.LENGTH_SHORT).show();
                    }
                });
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
    private void userVerificationEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Mail Sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void resetPassword() {

        EditText resetMail=new EditText(getActivity());
        AlertDialog.Builder passwordResetDialog= new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
        passwordResetDialog.setTitle("Reset Password");
        passwordResetDialog.setMessage("Enter your Mail to reset!");
        passwordResetDialog.setView(resetMail);
        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //extract the email and send the rest link
                String mail=resetMail.getText().toString();
                if(TextUtils.isEmpty(mail)){
                    Toast.makeText(getActivity(),"Empty credentials! please enter a correct Email!", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getActivity(),"Reset Link Sent to your Email.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"Error! Rest Link is not Send" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

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


}
