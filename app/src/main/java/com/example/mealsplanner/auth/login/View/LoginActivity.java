package com.example.mealsplanner.auth.login.View;

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
import com.example.mealsplanner.auth.login.Presenter.LoginPresenter;
import com.example.mealsplanner.auth.login.Presenter.LoginPresenterImpl;
import com.example.mealsplanner.auth.register.View.RegisterActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private EditText etEmail;
    private EditText etPswd;
    private Button btnSignin;
    private ImageView imgGoogle;
    private TextView tvSignup;
    private TextView tvSkip;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences sharedPreferences;
    private LoginPresenter presenter;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        initDependencies();
        initUI();
        initPresenter();
    }

    private void initDependencies() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
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
            showError("Google Sign-In initialization failed: " + e.getMessage());
        }
    }

    private void initUI() {
        etEmail = findViewById(R.id.etEmail);
        etPswd = findViewById(R.id.etPswd);
        btnSignin = findViewById(R.id.btnSignup);
        imgGoogle = findViewById(R.id.imgGoogle);
        tvSignup = findViewById(R.id.txtSignin);
        tvSkip = findViewById(R.id.tvSkip);
        progressBar = findViewById(R.id.progressBar);

        btnSignin.setOnClickListener(v -> presenter.attemptLogin(
        etEmail.getText().toString().trim(),
        etPswd.getText().toString().trim()));
        imgGoogle.setOnClickListener(v -> presenter.onGoogleSignInClick());
        tvSignup.setOnClickListener(v -> presenter.onSignUpClick());
        tvSkip.setOnClickListener(v -> presenter.onSkipClick());
    }

    private void initPresenter() {
        presenter = new LoginPresenterImpl(this, mAuth);
    }



    @Override
    public void showLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEmailError(String error) {
        etEmail.setError(error);
    }

    @Override
    public void setPasswordError(String error) {
        etPswd.setError(error);
    }

    @Override
    public void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void navigateToRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    @Override
    public void navigateToGuestFragment() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.IS_GUEST, true);
        startActivity(intent);
        finish();
    }

    @Override
    public void saveLoginState(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public void startGoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}