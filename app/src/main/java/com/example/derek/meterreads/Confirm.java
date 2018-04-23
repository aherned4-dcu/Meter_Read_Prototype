package com.example.derek.meterreads;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Confirm extends AppCompatActivity {
    TextView editTextReading, editTextDate, editTextMPRN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_confirm);


        editTextReading = findViewById(R.id.textReading);
        editTextDate =  findViewById(R.id.textDateCon);
        editTextMPRN = findViewById(R.id.textMprnCon);

        Bundle bundle = getIntent().getExtras();
        String mprn = bundle.getString("MPRN_CON");
        String reading = bundle.getString("READING");
        String date = bundle.getString("DATE");

        editTextReading.setText("123456789");
        editTextDate.setText("2018-04-22");
        editTextMPRN.setText("987654321");
    }

    public void openHome (View v) {
        Intent homeIntent = new Intent(this,Home.class);
        startActivity(homeIntent);
    }
}