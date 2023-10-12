package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText phoneNumberEditText;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setViews();

        onRegisterButtonClicked();
    }

    private void onRegisterButtonClicked(){
        btnRegister.setOnClickListener(view -> {
            String name = nameEditText.toString();
            String email = emailEditText.toString();
            String password = passwordEditText.toString();

            String phoneNumber = phoneNumberEditText.toString();

            if (authentication(name, email, phoneNumber, password)){
                Toast.makeText(this, "Congrats", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Something's wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean authentication(String name, String email, String phoneNumber, String password){
        String regex = "^[a-zA-Z]*$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        if (name.length() < 3 || matcher.matches()){
            return false;
        }
        else if (email.isEmpty()){
            return false;
        }
        else if (password.length() < 5){
            return false;
        }
        else {
            return true;
        }
    }

    private void setViews() {
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        btnRegister = findViewById(R.id.btnRegister);
    }
}