package com.example.mealsplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    EditText etName;
    EditText etEmail;
    EditText etPswd;
    Button btnSignup;
    TextView txtSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        initUI();
        configureSignUpButton();
        configureSignInText();


    }

    private void configureSignInText() {
        txtSignin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void configureSignUpButton() {
        btnSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }

    private void initUI() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPswd = findViewById(R.id.etPswd);
        btnSignup = findViewById(R.id.btnSignup);
        txtSignin = findViewById(R.id.txtSignin);

    }
}