package com.example.derek.meterreads;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Confirm extends AppCompatActivity {
    TextView editTextReading, editTextDate, editTextMPRN;
    public static final String MPRN_CON = "1234";
    public static final String READING = "1234";
    public static final String DATE = "2020-01-01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_confirm);


        editTextReading = findViewById(R.id.textReading);
        editTextDate =  findViewById(R.id.textDateCon);
        editTextMPRN = findViewById(R.id.textMprnCon);

        Intent intent = getIntent();
        String mprn = intent.getStringExtra(MPRN_CON);
        String reading = intent.getStringExtra(READING);
        String date = intent.getStringExtra(DATE);

        editTextReading.setText(reading);
        editTextDate.setText(date);
        editTextMPRN.setText(mprn);

    }

    public void openHome (View v) {
        Intent homeIntent = new Intent(this,Home.class);
        startActivity(homeIntent);
    }

    public void openVis (View v) {
        Intent visIntent = new Intent(this,Vis.class);
        startActivity(visIntent);
    }
}