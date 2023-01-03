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

import com.application.MindSet.MainActivity;
import com.application.MindSet.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        final EditText usernameEditText = binding.usernameET;
        final EditText passwordEditText = binding.passwordET;
        final EditText confirmpwordEditText = binding.confirmpwordET;
        final Button registerButton = binding.registerBTN;
        final TextView signin = binding.switchToSignin;

        //Connect to DB
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        signin.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });

        registerButton.setOnClickListener(view -> {
            String email = usernameEditText.getText().toString();
            String pword = passwordEditText.getText().toString();
            String cpword = confirmpwordEditText.getText().toString();

            if(!email.matches(emailPattern)){
                usernameEditText.setError("Invalid email");
            }else if(pword.length() < 6) {
                passwordEditText.setError("Password must be minimum 6 digits");
            }else if(!cpword.equals(pword)) {
                confirmpwordEditText.setError("Passwords do not match");
            }else {
                progressDialog.setMessage("Registering your account...");
                progressDialog.setTitle("Signing Up");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Your account has been created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}