package com.example.aexpress.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aexpress.Prevalent.Getname;
import com.example.aexpress.Prevalent.Prevalent;
import com.example.aexpress.R;
import com.example.aexpress.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainpageActivity extends AppCompatActivity {
    Button login;
    TextView register;
    private ProgressDialog loadingbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);


        login = (Button) findViewById(R.id.mainlogin_btn);
        register = (TextView)findViewById(R.id.mainregister_btn);
        loadingbar =new ProgressDialog(this);

        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
                String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);



                if(UserPhoneKey!=null && UserPasswordKey!=null)
                {

                    if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                        AllowAccess(UserPhoneKey, UserPasswordKey);


                        loadingbar.setMessage("Please wait...");
                        loadingbar.setCancelable(false);
                        loadingbar.show();

                    }
                }
                else
                {
                    Intent intent =new Intent(MainpageActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }

            private void AllowAccess(final String phone, final String password)
            {
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();

                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.child("Users").child(phone).exists())
                        {
                            Users usersData = snapshot.child("Users").child(phone).getValue(Users.class);
                            if(usersData.getPhone().equals(phone))
                            {
                                if(usersData.getPassword().equals(password))
                                {
                                    Getname.UserName= usersData.getName();
                                    Getname.UserPhone = phone;
                                    Toast.makeText(MainpageActivity.this, "You are already logged in...", Toast.LENGTH_SHORT).show();
                                    loadingbar.dismiss();
                                    Intent intent = new Intent(MainpageActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(MainpageActivity.this, "Wrong password...", Toast.LENGTH_SHORT).show();
                                    loadingbar.dismiss();
                                }
                            }

                        }
                        else
                        {
                            Toast.makeText(MainpageActivity.this, "Account with this "+phone+" number does not exists", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
                Intent intent = new Intent(MainpageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainpageActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });



    }
}