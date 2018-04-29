package com.example.derek.meterreads;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;

public class Manual extends BaseActivity {
    EditText editTextReading, editTextDate;
    public static final String TAG = Manual.class.getSimpleName(); //Log Tag

    String today = getDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAction();
        setContentView(R.layout.activity_manual);

        editTextReading = findViewById(R.id.editTextReading);
        editTextDate = findViewById(R.id.editTextDate);
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
            editTextDate.setError(getString(R.string.date_required));
            editTextDate.requestFocus();
            return;
        }

        if (editTextDate.length() != 10) {
            editTextDate.setError(getString(R.string.enter_date));
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
        goOCR();
    }
    public void openHome (View v) {
        goHome();
    }

    public void onCallBtnClick(View v){
        handlePhoneCall();
    }



}
