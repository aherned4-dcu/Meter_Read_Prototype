package com.example.derek.meterreads;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    EditText editTextMPRN;
    private FirebaseAuth mAuth;
    public static final String MPRN_CON = "1234";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_home);
        editTextMPRN = (EditText) findViewById(R.id.editTextMPRN);
        mAuth = FirebaseAuth.getInstance();

    }

    public void openOCR (View v) {
        String mprn = editTextMPRN.getText().toString().trim();
        if (mprn.isEmpty()) {
            editTextMPRN.setError("MPRN is required");
            editTextMPRN.requestFocus();
            return;
        }
        Intent ocrIntent = new Intent(this,Ocr.class);
        mprn = editTextMPRN.getText().toString();
        ocrIntent.putExtra(MPRN_CON,mprn);
        startActivity(ocrIntent);
        Toast.makeText(this, mprn,
                Toast.LENGTH_SHORT).show();
    }

    public void openMan (View v) {
        String mprn = editTextMPRN.getText().toString().trim();
        if (mprn.isEmpty()) {
            editTextMPRN.setError("MPRN is required");
            editTextMPRN.requestFocus();
            return;
        }
        Intent manIntent = new Intent(this,Manual.class);
        mprn = editTextMPRN.getText().toString();
        manIntent.putExtra(MPRN_CON,mprn);
        startActivity(manIntent);
        Toast.makeText(this, mprn,
                Toast.LENGTH_SHORT).show();
    }

    public void logOut(View v){
        mAuth.signOut(); //End user session
        finish();
    }
}
