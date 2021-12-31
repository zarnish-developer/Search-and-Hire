package com.example.splash_activity.recycle;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.splash_activity.R;
import com.example.splash_activity.RegisterActivity;
import com.example.splash_activity.WorkerAndUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class workerSideTab extends AppCompatActivity {

    TabLayout tabLayout;
   // TabLayout tabLayout;
    ViewPager viewPager;
    private Toolbar mToolBar;
    private FirebaseAuth mAuth;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_side_tab);
        mAuth=FirebaseAuth.getInstance();
        tabLayout =(TabLayout)findViewById(R.id.mytabview);
       // tabLayout.setCustomView(R.layout.badged_tab);
        mToolBar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        this.getSupportActionBar().setTitle("Search and Hire");
        viewPager=(ViewPager)findViewById(R.id.mypager);
        viewPager.setAdapter(new MyOwnPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
//        int count= Integer.parseInt(getIntent().getStringExtra("Count"));
     //   tabLayout.setCustomView(R.layout.badged_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tabLayout.getTabAt(tab.getPosition()).removeBadge();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).removeBadge();
            }
        });

      BadgeDrawable badgeDrawable=tabLayout.getTabAt(1).getOrCreateBadge();
      badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
      badgeDrawable.setVisible(true);
      badgeDrawable.clearNumber();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();
        final boolean isDarkModeOn= sharedPreferences.getBoolean("isDarkModeOn",false);
        if(isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if(item.getItemId() == R.id.main_update_profile){

            Intent Update_Profile= new Intent(workerSideTab.this, upDate_Profile.class);
            startActivity(Update_Profile);
        }
        if(item.getItemId()==R.id.DisableDarkMode){
            AlertDialog.Builder builder1,anotherBulder ;
            builder1=new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
            builder1.setTitle("Disable Dark Mode?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(isDarkModeOn){
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                editor.putBoolean("isDarkModeOn",false);
                                editor.apply();
                               // item.setEnabled(false);

                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setIcon(R.drawable.ic_baseline_warning_24);
            //  builder1.setTitle("Disable Dark mode?");
            builder1.create();
            builder1.show();
        }
        if(item.getItemId()==R.id.enableDarkMode){

            AlertDialog.Builder builder1,anotherBulder ;
            anotherBulder=new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
             builder1=new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
            builder1.setTitle("Enable Dark Mode?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(!(isDarkModeOn)){
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                editor.putBoolean("isDarkModeOn",true);
                                anotherBulder.setTitle("Disable Dark Mode? ");

                                editor.apply();

                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setIcon(R.drawable.ic_baseline_warning_24);
          //  builder1.setTitle("Disable Dark mode?");
            builder1.create();
            builder1.show();
        }
        if(item.getItemId() == R.id.main_reset_password){

            resetPassword();

           // Intent resetPassword=new Intent(workerSideTab.this , workerSideTab.class);
           // startActivity(resetPassword);
        }
        if(item.getItemId() == R.id.main_logOut){
            AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
            builder.setTitle("Are you sure to LogOut?")
                    .setPositiveButton("LogOut", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            Intent loginIntent=new Intent(workerSideTab.this , WorkerAndUser.class);
                            startActivity(loginIntent);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

        builder.create();
            builder.show();
           // builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlack));

        }
        if(item.getItemId()==R.id.main_delete_account){
            deleteAccount();
        }
        if(item.getItemId()==R.id.main_Authenticate){
            userVerificationEmail();

        }
        return true;
    }

    private void userVerificationEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(workerSideTab.this,"Mail Sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void deleteAccount() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AlertDialog.Builder dialog= new AlertDialog.Builder(workerSideTab.this,R.style.MyAlertDialogStyle);
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
                                    Toast.makeText(workerSideTab.this,"Account Deleted", Toast.LENGTH_SHORT).show();
                                    Intent deleteAccount=new Intent(workerSideTab.this, RegisterActivity.class);
                                    startActivity(deleteAccount);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(workerSideTab.this,"error occurred" + e, Toast.LENGTH_SHORT).show();
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

    private void resetPassword() {

        EditText resetMail=new EditText(this);
        AlertDialog.Builder passwordResetDialog= new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
        passwordResetDialog.setTitle("Reset Password");
        passwordResetDialog.setMessage("Enter your Mail to reset!");
        passwordResetDialog.setView(resetMail);
        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //extract the email and send the rest link
                String mail=resetMail.getText().toString();
                if(TextUtils.isEmpty(mail)){
                    Toast.makeText(workerSideTab.this,"Empty credentials! please enter a correct Email!", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(workerSideTab.this,"Reset Link Sent to your Email.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(workerSideTab.this,"Error! Rest Link is not Send" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
class MyOwnPagerAdapter extends FragmentStatePagerAdapter {

    public MyOwnPagerAdapter( FragmentManager fm) {

        super(fm);
    }

    String data[]={"Availability","Reviews"};
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new Availability();
        }
        if(position==1){
            return new reviews();
        }

        return null;
    }
    @Override
    public int getCount()
    {
        return data.length;
    }
    @Override
    public CharSequence getPageTitle(int position)
    {
        return data[position];
    }
}