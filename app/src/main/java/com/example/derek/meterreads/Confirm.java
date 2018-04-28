package com.example.derek.meterreads;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Confirm extends BaseActivity {
    TextView editTextReading, editTextDate, editTextMPRN;


    DataBaseBuild myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAction();
        setContentView(R.layout.activity_confirm);

        myDb = new DataBaseBuild(this);
        editTextReading = findViewById(R.id.textReading);
        editTextDate =  findViewById(R.id.textDateCon);
        editTextMPRN = findViewById(R.id.textMprnCon);


        Intent intent = getIntent();
        String mprn = intent.getStringExtra(Constants.MPRN_CON);
        String reading = intent.getStringExtra(Constants.READING);
        String date = intent.getStringExtra(Constants.DATE);

        editTextReading.setText(reading);
        editTextDate.setText(date);
        editTextMPRN.setText(mprn);

    }



    public void openVis (View v) {
        Intent intent = getIntent();
        String mprn = intent.getStringExtra("MPRN_CON");
        String reading = intent.getStringExtra("READING");
        String date = intent.getStringExtra("DATE");
        boolean isInserted = myDb.insertData(mprn,reading,date);

        if (isInserted == true)
            Toast.makeText(this, R.string.data_inserted, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, R.string.data_not_inserted, Toast.LENGTH_LONG).show();

        Intent visIntent = new Intent(this, Vis.class);
        visIntent.putExtra(Constants.MPRN_CON,mprn);
        visIntent.putExtra(Constants.READING,reading);
        visIntent.putExtra(Constants.DATE,date);
        startActivity(visIntent);
    }

    public void openHome (View v) {
       goHome();

    }

    }