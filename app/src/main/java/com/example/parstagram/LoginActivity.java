package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
    
    public static final String TAG = "LoginActivity";
    
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // if already signed in go to main page immediately
        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }
        
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick Sign up button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signUpUser(username, password);
            }
        });
        
        
    }

    private void signUpUser(String username, String password) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null){
                    //TODO: better error handling
                    Toast.makeText(LoginActivity.this, "Issue w/ Signup!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with signup", e);
                    return;
                }
                // navigate to main activity if login successful
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Signup success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    //TODO: better error handling
                    Toast.makeText(LoginActivity.this, "Issue w/ login!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with login", e);
                    return;
                }
                // navigate to main activity if login successful
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        // remove activity from backstack if login activity successful
        finish();
    }
}