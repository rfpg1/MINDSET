package com.application.MindSet;

import static com.application.MindSet.MainActivity.getContext;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.text.method.PasswordTransformationMethod;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        final EditText usernameET = findViewById(R.id.usernameET);
        final EditText passwordET = findViewById(R.id.passwordET);
        final Button loginBTN = findViewById(R.id.loginBTN);
        final TextView signupTV = findViewById(R.id.switchToSignup);


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
                    showHideIcon.setImageResource(R.drawable.ic_baseline_password_22);
                }
                passwordET.setSelection(passwordET.getText().length());
            }
        });


        signupTV.setOnClickListener(view -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });

        loginBTN.setOnClickListener(view -> {
            String email = usernameET.getText().toString();
            String pword = passwordET.getText().toString();

            if(!email.matches(emailPattern)){
                usernameET.setError("Invalid email");
            }else if(pword.length() < 6) {
                passwordET.setError("Password must be minimum 6 digits");
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