package com.example.mealsplanner.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealsplanner.MainActivity;
import com.example.mealsplanner.R;
import com.example.mealsplanner.auth.register.RegisterActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPswd;
    Button btnSignin;
    ImageView imgGoogle;
    ImageView imgFacebook;
    TextView txtSignup;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;



    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            handleGoogleSignInResult(task);
                            Toast.makeText(this, "Google sign in successful", Toast.LENGTH_SHORT).show();
                        } else {
                            int resultCode = result.getResultCode();
                            String errorMessage = "Google sign in failed with result code: " + resultCode;
                            if (result.getData() == null) {
                                errorMessage += " and data is null";
                            }
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            navigateToMain();
        }
        initGoogleSignIn();
        initUI();
        configureSignInButton();
        configureGoogleSignIn();
        configureFacebookSignIn();
        configureSignUpText();

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

    private void configureFacebookSignIn() {
    }

    private void configureSignUpText() {
        txtSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

    }

    private void configureGoogleSignIn() {
        imgGoogle.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });

    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String idToken = account.getIdToken();
                if (idToken != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                    firebaseAuthWithCredential(credential);
                } else {
                    Toast.makeText(this, "ID Token is null", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (ApiException e) {
            String errorMessage = "Google sign in failed: " + e.getStatusCode() ;
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
    private void firebaseAuthWithCredential(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Save user info
                        FirebaseUser user = mAuth.getCurrentUser();
                        navigateToMain();

                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void configureSignInButton() {
        btnSignin.setOnClickListener(v ->navigateToMain() );

    }

    private void initUI() {
        etEmail = findViewById(R.id.etEmail);
        etPswd = findViewById(R.id.etPswd);
        btnSignin = findViewById(R.id.btnSignup);
        imgGoogle = findViewById(R.id.imgGoogle);
        txtSignup = findViewById(R.id.txtSignin);


    }


}