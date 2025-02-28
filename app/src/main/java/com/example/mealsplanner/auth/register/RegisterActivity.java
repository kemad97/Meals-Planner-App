package com.example.mealsplanner.auth.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealsplanner.MainActivity;
import com.example.mealsplanner.R;
import com.example.mealsplanner.auth.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPswd;
    private EditText etConfirmPswd;
    private Button btnSignup;
    private TextView txtSignin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        initUI();
        configureSignUpButton();
        configureSignInText();


    }
    private void initUI() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPswd = findViewById(R.id.etPswd);
        btnSignup = findViewById(R.id.btnSignup);
        txtSignin = findViewById(R.id.txtSignin);
        progressBar = findViewById(R.id.progressBar);
        etConfirmPswd = findViewById(R.id.etConfirmPswd);


    }

    private void configureSignInText() {
        txtSignin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void configureSignUpButton() {
        btnSignup.setOnClickListener(v -> registerUser());
    }

    private void  registerUser()
    {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPswd.getText().toString().trim();
        String confirmPassword = etConfirmPswd.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please provide valid email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPswd.setError("Password is required");
            etPswd.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPswd.setError("Password should be at least 6 characters");
            etPswd.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPswd.setError("Passwords don't match");
            etConfirmPswd.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Update profile
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(profileTask -> {
                                    progressBar.setVisibility(View.GONE);
                                    if (profileTask.isSuccessful()) {
                                        saveUserToSharedPreferences(email, password, name);
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();
                                    }
                                });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void saveUserToSharedPreferences(String email, String password, String name) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("name", name);
        editor.apply();
    }


}