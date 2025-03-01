package com.example.mealsplanner.auth.register.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealsplanner.MainActivity;
import com.example.mealsplanner.R;
import com.example.mealsplanner.auth.login.View.LoginActivity;
import com.example.mealsplanner.auth.register.Presenter.RegisterPresenter;
import com.example.mealsplanner.auth.register.Presenter.RegisterPresenterImpl;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements Registerview {

    private EditText etName;
    private EditText etEmail;
    private EditText etPswd;
    private EditText etConfirmPswd;
    private Button btnSignup;
    private TextView txtSignin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private ImageView imgGoogle;
    private GoogleSignInClient mGoogleSignInClient;

    private RegisterPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        initUI();
        initPresenter();
        initGoogleSignIn();


    }

    private void initGoogleSignIn() {
        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        } catch (Exception e) {
            Toast.makeText(this, "Google Sign-In initialization failed: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initPresenter() {
        presenter = new RegisterPresenterImpl(this, FirebaseAuth.getInstance());
    }

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            presenter.handleGoogleSignInResult(task);
                        }
                    }
            );


    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setInputError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void saveUserData(String email, String password, String name) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("name", name);
        editor.apply();
    }

    @Override
    public void startGoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }



    private void initUI() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPswd = findViewById(R.id.etPswd);
        btnSignup = findViewById(R.id.btnSignup);
        txtSignin = findViewById(R.id.txtSignin);
        progressBar = findViewById(R.id.progressBar);
        etConfirmPswd = findViewById(R.id.etConfirmPswd);
        imgGoogle = findViewById(R.id.imgGoogle);

        btnSignup.setOnClickListener(v -> presenter.attemptRegister(
                etName.getText().toString().trim(),
                etEmail.getText().toString().trim(),
                etPswd.getText().toString().trim(),
                etConfirmPswd.getText().toString().trim()
        ));
        txtSignin.setOnClickListener(v -> presenter.onSignInClick());
        imgGoogle.setOnClickListener(v -> presenter.onGoogleSignInClick());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

}


