package com.example.derek.meterreads;

import android.content.Context;
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
import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Manual extends AppCompatActivity {
    EditText editTextReading, editTextDate;
    public static final String MPRN_CON = "1234";
    public static final String READING = "1234";
    public static final String DATE = "2020-01-01";
    String today = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(new Date());

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
        String mprn = intent.getStringExtra(Home.MPRN);
        String reading = editTextReading.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();

        if (reading.isEmpty()) {
            editTextReading.setError("Reading is required");
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
        conIntent.putExtra(MPRN_CON,mprn);
        conIntent.putExtra(READING,reading);
        conIntent.putExtra(DATE,date);
        startActivity(conIntent);

    }

    public void openOCR (View v) {
        Intent ocrIntent = new Intent(this,Ocr.class);
        startActivity(ocrIntent);
    }
    public void openHome (View v) {
        Intent homeIntent = new Intent(this,Home.class);
        startActivity(homeIntent);
    }

    //https://stackoverflow.com/questions/4275678/how-to-make-a-phone-call-using-intent-in-android
    private void onCallBtnClick(){
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            phoneCall();
        }else {
            Toast.makeText(this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneCall(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:1800000888"));
            this.startActivity(callIntent);
        }else{
            Toast.makeText(this, "You didn't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

}
