package com.application.MindSet.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GestureDetectorCompat;

import com.application.MindSet.MainActivity;
import com.application.MindSet.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        final EditText usernameEditText = binding.usernameET;
        final EditText passwordEditText = binding.passwordET;
        final Button loginButton = binding.loginBTN;
        final TextView signup = binding.switchToSignup;

        //Connect to DB
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        signup.setOnClickListener(view -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });

        loginButton.setOnClickListener(view -> {
            String email = usernameEditText.getText().toString();
            String pword = passwordEditText.getText().toString();

            if(!email.matches(emailPattern)){
                usernameEditText.setError("Invalid email");
            }else if(pword.length() < 6) {
                passwordEditText.setError("Password must be minimum 6 digits");
            }else {
                progressDialog.setMessage("Logging into your account...");
                progressDialog.setTitle("Signing In");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}