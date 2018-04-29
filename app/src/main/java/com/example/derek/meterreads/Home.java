package com.example.derek.meterreads;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
/**
 *
 * The Home class creates the functionality of the Home activity screen.
 *
 *
 * @link {@link Final}
 *
 * @author – Derek Aherne
 * @version – 25/04/2018
 */
public class Home extends BaseActivity {
    EditText editTextMPRN;
    private FirebaseAuth mAuth;

    /**
     * The onCreate method hides the action bar, sets the content to activity_home and instantiates an instance of Firebase
     *
     * @author – Derek Aherne
     * @version – 25/04/2018/
     * @param – Bundle savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_home);
        editTextMPRN = findViewById(R.id.editTextMPRN);
        mAuth = FirebaseAuth.getInstance();

    }
    /**
     *
     * A method that starts Ocr.java and passes bundles
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param - View v
     */
    public void openOCR (View v) {
        String mprn = editTextMPRN.getText().toString().trim();
        if (mprn.isEmpty()) {
            editTextMPRN.setError(getString(R.string.mprn_error));
            editTextMPRN.requestFocus();
            return;
        }
        Intent ocrIntent = new Intent(this,Ocr.class);
        mprn = editTextMPRN.getText().toString();
        ocrIntent.putExtra(Constants.MPRN_CON,mprn);
        startActivity(ocrIntent);

    }
    /**
     *
     * A method that starts Manual.java and passes bundles
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param - View v
     */
    public void openMan (View v) {
        String mprn = editTextMPRN.getText().toString().trim();
        if (mprn.isEmpty()) {
            editTextMPRN.setError(getString(R.string.mprn_error));
            editTextMPRN.requestFocus();
            return;
        }
        Intent manIntent = new Intent(this,Manual.class);
        mprn = editTextMPRN.getText().toString();
        manIntent.putExtra(Constants.MPRN_CON,mprn);
        startActivity(manIntent);

    }
    /**
     *
     * A method that logouts out the current local Firebase user from the device
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param - View v
     */
    public void logOut(View v){
        mAuth.signOut(); //End current user session
        finish();
    }
}
