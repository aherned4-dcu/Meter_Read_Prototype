package com.example.derek.meterreads;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/**
 *
 * The MainActivity class creates the functionality of the Main activity screen.
 *
 *
 * @link {@link MainActivity}
 *
 * @author – Derek Aherne
 * @version – 25/04/2018
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /* Citation: Class contains code adapted from
     * URL: https://github.com/firebase/quickstart-android/tree/master/auth
     * Permission: MIT Licence Retrieved on:02th April 2018  */

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    public static final String TAG = SignUpActivity.class.getSimpleName(); //Log Tag

    /**
     * The onCreate method hides the action bar, sets the content to activity_main and instantiates an instance of Firebase.
     *
     * @param  savedInstanceState
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
    }
    /**
     *
     * A method that validates a user login details. The details are then passed to Firebase to create the user
     *
     *
     */
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.email_required));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.enter_valid_email));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.pass_required));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.password_error));
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, getString(R.string.signin_success));
                            finish();
                            startActivity(new Intent(getApplicationContext(),Home.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, getString(R.string.signin_fail), task.getException());
                            Toast.makeText(MainActivity.this, R.string.auth_fail,
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    /**
     *
     * The onStart starts Home.java if there is a current user on the device
     *
     */
        @Override
        protected void onStart() {
            super.onStart();

            if (mAuth.getCurrentUser() != null) {
                finish();
                startActivity(new Intent(this, Home.class));
            }
        }
    /**
     *
     * The onClick starts SignUpActivity.java if the sign up option is chosen.
     * Else the userLogin() method is called.
     *
     * @param view
     */
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.textViewSignup:
                    finish();
                    startActivity(new Intent(this, SignUpActivity.class));
                    break;

                case R.id.buttonLogin:
                    userLogin();
                    break;
            }
        }


    }
