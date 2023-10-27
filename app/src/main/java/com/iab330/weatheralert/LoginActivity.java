package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setViews();
        onLoginButtonClicked();
    }
/* Testing */
    private void onLoginButtonClicked(){
        btnLogin.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // REMOVE THIS LATER. THIS CHECKS IF THE EMAIL IS CORRECT
            Log.d("LoginActivity", "Email: " + email);

            if(validateUser(email, password)){
                Toast.makeText(LoginActivity.this, "Validated Credentials", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                emailEditText.setError("Maybe this is wrong?");
                passwordEditText.setError("Or maybe this is wrong?");
            }

        });
    }

    private boolean validateUser(String email, String password){
        return (email.equals("ex@ex.com") && password.equals("pass123") || (email.equals("ad@ad.com") && password.equals("ad123")));
    }
    private void setViews(){
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        btnLogin = findViewById(R.id.btnLogin);
    }
}