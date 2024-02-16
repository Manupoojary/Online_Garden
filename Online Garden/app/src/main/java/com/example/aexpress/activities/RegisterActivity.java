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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aexpress.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.example.aexpress.Prevalent.Getname;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.example.aexpress.R;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button createacct;
    private EditText ipname,ipphone,ippass;
    TextView logindirect;
    private ProgressDialog loadingbar;
    String regexPatternphone = "^[0-9]{10}$";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createacct =(Button) findViewById(R.id.register_btn);
        ipname =(EditText) findViewById(R.id.inputname);
        ipphone =(EditText) findViewById(R.id.inputphone);
        ippass =(EditText) findViewById(R.id.inputpassword);
        loadingbar = new ProgressDialog(this);
        logindirect = (TextView) findViewById(R.id.loginRedirectText);

        logindirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        createacct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CreateAccount();
                }
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateAccount()
    {
        String name = ipname.getText().toString();
        String phone = ipphone.getText().toString();
        String password = ippass.getText().toString();
        Getname.UserName= name;
        Getname.UserPhone = phone;

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please enter your name....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please enter phone number....",Toast.LENGTH_SHORT).show();
        }
        else if (!(phone).matches(regexPatternphone))
        {
            Toast.makeText(this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter password....",Toast.LENGTH_SHORT).show();
        }
        else {
            loadingbar.setMessage("Please wait when we are checking credentials");
            loadingbar.setCancelable(false);
            loadingbar.show();

            ValidatephoneNumber(name,phone,password);
        }
    }

    private void ValidatephoneNumber(String name, String phone, String password)
    {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(!(snapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    Rootref.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again ", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "This "+ phone + " already exists", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}