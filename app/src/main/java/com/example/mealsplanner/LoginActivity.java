package com.example.mealsplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPswd;
    Button btnSignin;
    ImageView imgGoogle;
    ImageView imgFacebook;
    TextView txtSignup;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        initUI();
        configureSignInButton();
        configureGoogleSignIn();
        configureFacebookSignIn();
        configureSignUpText();

    }

    private void configureFacebookSignIn() {
    }

    private void configureSignUpText() {
        txtSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

    }

    private void configureGoogleSignIn() {

    }

    private void configureSignInButton() {
        btnSignin.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }

    private void initUI() {
        etEmail = findViewById(R.id.etEmail);
        etPswd = findViewById(R.id.etPswd);
        btnSignin = findViewById(R.id.btnSignup);
        imgGoogle = findViewById(R.id.imgGoogle);
        imgFacebook = findViewById(R.id.imgFacebook);
        txtSignup = findViewById(R.id.txtSignin);


    }


}