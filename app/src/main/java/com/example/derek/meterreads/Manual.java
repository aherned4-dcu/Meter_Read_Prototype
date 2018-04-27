package com.example.derek.meterreads;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Manual extends BaseActivity {
    EditText editTextReading, editTextDate;


    String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_manual);

        editTextReading = (EditText) findViewById(R.id.editTextReading);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextDate.setText(today);
        editTextReading.requestFocus();

    }

    public void submitMan(View view) {
        Intent intent=getIntent();
        String mprn = intent.getStringExtra(Constants.MPRN_CON);
        String reading = editTextReading.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();

        if (reading.isEmpty()) {
            editTextReading.setError(getString(R.string.read_warning));
            editTextReading.requestFocus();
            return;
        }


        if (date.isEmpty()) {
            editTextDate.setError("date is required");
            editTextDate.requestFocus();
            return;
        }

        if (editTextDate.length() != 10) {
            editTextDate.setError("Enter a correct date");
            editTextDate.requestFocus();
            return;
        }

        Intent conIntent = new Intent (this,Confirm.class);
        conIntent.putExtra(Constants.MPRN_CON,mprn);
        conIntent.putExtra(Constants.READING,reading);
        conIntent.putExtra(Constants.DATE,date);
        startActivity(conIntent);


    }

    public void openOCR (View v) {
        finish();
        Intent ocrIntent = new Intent(this,Ocr.class);
        startActivity(ocrIntent);
    }
    public void openHome (View v) {
        finish();
        Intent homeIntent = new Intent(this,Home.class);
        startActivity(homeIntent);
    }

    public void onCallBtnClick(View v){
        handlePhoneCall();
    }



}
