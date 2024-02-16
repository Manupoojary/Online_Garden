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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aexpress.Prevalent.Prevalent;
import com.example.aexpress.R;
import com.example.aexpress.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.aexpress.Prevalent.Getname;


import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText Inputphone,Inputpassword;
    TextView notadmin,admin;
    private Button Loginbutton;
    private ProgressDialog loadingbar;
    private String parentDbName ="Users";
    private CheckBox chkBoxRememberMe;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Loginbutton =(Button) findViewById(R.id.loginbutton);
        Inputphone =(EditText) findViewById(R.id.loginphone);
        Inputpassword =(EditText) findViewById(R.id.loginpass);
        loadingbar = new ProgressDialog(this);
        chkBoxRememberMe = (CheckBox)findViewById(R.id.chkboxRememberme);
        Paper.init(this);


        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginuser();
            }
        });
    }

    private void loginuser()
    {
        String phone = Inputphone.getText().toString();
        String password = Inputpassword.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please enter phone number....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter password....",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setMessage("Please wait when we are checking credentials");
            loadingbar.setCancelable(false);
            loadingbar.show();

            ValidateUser(phone,password);

        }
    }

    private void ValidateUser(String phone, String password)
    {
        if(chkBoxRememberMe.isChecked())
        {

            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);

        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = snapshot.child(parentDbName).child(phone).getValue(Users.class);


                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Users"))
                            {
                                Getname.UserName= usersData.getName();
                                Getname.UserPhone = phone;
                                Toast.makeText(LoginActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("admin"))
                            {
                                Toast.makeText(LoginActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Wrong password...", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this "+phone+" number does not exists", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

}