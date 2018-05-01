package com.example.derek.meterreads;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 *
 * The Manual class creates the functionality of the Manual Input activity screen.
 *
 *
 * @link {@link MainActivity}
 *
 * @author – Derek Aherne
 * @version – 25/04/2018
 */
public class Manual extends BaseActivity {
    EditText editTextReading, editTextDate;
    public static final String TAG = Manual.class.getSimpleName(); //Log Tag

    String today = getDate();
    /**
     * The onCreate method hides the action bar, sets the content to activity_manual
     *
     * @param savedInstanceState
     *
     */
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

    /**
     * Handles the validation of the user inputs
     * Calls explicit intent to start Confrim.java and passes bundles
     * @param view
     */
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

    /**
     * starts Ocr.java by calling goOCR()
     * @param v
     */
    public void openOCR (View v) {
        goOCR();
    }

    /**
     * starts Home.java by calling goHome()
     * @param v
     */
    public void openHome (View v) {
        goHome();
    }
    /**
     * an implicit intent for a phone app by calling handlePhoneCall()
     * @param v
     */
    public void onCallBtnClick(View v){
        handlePhoneCall();
    }



}
