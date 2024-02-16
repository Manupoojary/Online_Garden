package com.example.aexpress.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aexpress.R;

public class SpinnerActivity extends AppCompatActivity {

    Spinner spinner;

    String[] loction ={"Manglore","Udupi","Kundapura"};

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        spinner =findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SpinnerActivity.this, R.layout.item_file,loction);
        adapter.setDropDownViewResource(R.layout.item_file);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String value=adapterView.getItemAtPosition(position).toString();
//                Toast.makeText(SpinnerActivity.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}