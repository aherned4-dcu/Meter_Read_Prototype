package com.example.derek.meterreads;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

/**
 * The SignUpActivity Class creates the functionality for the registration process using Firebase
 *
 * @link {@link SignUpActivity}
 *
 * @author – Derek Aherne
 * @version – 25/04/2018
 *
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    /* Citation: Class contains code adapted from
     * URL: https://github.com/firebase/quickstart-android/tree/master/auth
     * Permission: MIT Licence Retrieved on:15th April 2018  */
    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword;
    public static final String TAG = SignUpActivity.class.getSimpleName(); //Log Tag
    private FirebaseAuth mAuth;

    @Override
    /**
     * The onCreate method set the content to activity_signup.
     * Instantiates an instance of Firebase
     * Sets on Click Listeners for buttons
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAction();
        setContentView(R.layout.activity_signup);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
    }

    /**
     * A method to valid user input and register a user through Firebase
     */
    private void registerUser() {
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
        final Intent intent=new Intent(this, Home.class);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, R.string.reg_success,
                                    Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, getString(R.string.create_user_fail), task.getException());
                            Toast.makeText(SignUpActivity.this, R.string.auth_fail,
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp: //if signup button is pressed then register
                registerUser();
                break;

            case R.id.textViewLogin: //if login textView is pressed then start login activity

                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}