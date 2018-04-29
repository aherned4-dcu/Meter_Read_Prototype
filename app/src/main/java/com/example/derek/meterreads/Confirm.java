package com.example.derek.meterreads;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
/**
 *
 * The Confirm class contains the methods to create the functionality of the confirmation screen
 *
 *
 * @link {@link Confirm}
 *
 * @author – Derek Aherne
 * @version – 25/04/2018
 */
public class Confirm extends BaseActivity {
    TextView editTextReading, editTextDate, editTextMPRN;
    public static final String TAG = Confirm.class.getSimpleName(); //Log Tag

    DataBaseBuild myDb;
    /**
     *
     * The onCreate method hides the action bar, creates bundles and an creates an instance of the database
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param – Bundle savedInstanceState
     */
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


    /**
     *
     * A method that starts Vis.java, passes bundles and inserts data to meter_read table
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param - View v
     */
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

    /**
     *
     * A method that calls goHome()
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param - View v
     */
    public void openHome (View v) {
       goHome();

    }

    }