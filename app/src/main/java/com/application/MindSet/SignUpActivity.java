package com.application.MindSet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.application.MindSet.databinding.ActivitySignUpBinding;
import com.application.MindSet.ui.profile.ProfileActivity;
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
        setContentView(R.layout.activity_sign_up);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        final EditText usernameET = findViewById(R.id.usernameET);
        final EditText passwordET = findViewById(R.id.passwordET);
        final EditText confirmpwordET = findViewById(R.id.confirmpwordET);
        final Button registerBTN = findViewById(R.id.registerBTN);
        final TextView signinTV = findViewById(R.id.switchToSignin);

        //Connect to DB
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        final ImageView showHideIcon = findViewById(R.id.show_hide_icon);

        showHideIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordET.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    passwordET.setTransformationMethod(null);
                    showHideIcon.setImageResource(R.drawable.ic_launcher_eye);
                } else {
                    passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showHideIcon.setImageResource(R.drawable.ic_baseline_password_24);
                }
                passwordET.setSelection(passwordET.getText().length());
            }
        });

        final ImageView showHideIcon2 = findViewById(R.id.show_hide_icon2);

        showHideIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmpwordET.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    confirmpwordET.setTransformationMethod(null);
                    showHideIcon2.setImageResource(R.drawable.ic_launcher_eye);
                } else {
                    confirmpwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showHideIcon2.setImageResource(R.drawable.ic_baseline_password_22);
                }
                confirmpwordET.setSelection(confirmpwordET.getText().length());
            }
        });

        signinTV.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });

        registerBTN.setOnClickListener(view -> {
            String email = usernameET.getText().toString();
            String pword = passwordET.getText().toString();
            String cpword = confirmpwordET.getText().toString();

            if(!email.matches(emailPattern)){
                usernameET.setError("Invalid email");
            }else if(pword.length() < 6) {
                passwordET.setError("Password must be minimum 6 digits");
            }else if(!cpword.equals(pword)) {
                confirmpwordET.setError("Passwords do not match");
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
                            startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
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