package com.example.derek.meterreads;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends BaseActivity {
    EditText editTextMPRN;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_home);
        editTextMPRN = findViewById(R.id.editTextMPRN);
        mAuth = FirebaseAuth.getInstance();

    }

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

    public void logOut(View v){
        mAuth.signOut(); //End current user session
        finish();
    }
}
