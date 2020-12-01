package com.example.focus2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.VISIBLE;

public class Login extends AppCompatActivity {

    EditText emailEt, passwordEt;
    Button loginButton;
    TextView forgot, register;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.emailLogin);
        passwordEt = findViewById(R.id.passwordLogin);

        loginButton = findViewById(R.id.registerButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        forgot = findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserReg();
            }
        });

        progressBar = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
    }

    public void goToUserReg() {
        startActivity(new Intent(this, UserRegistration.class));
        finish();
    }

    public void loginUser() {
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        if (email.isEmpty()) {
            emailEt.setError("E-mail required");
            emailEt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Provide valid E-mail");
            emailEt.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEt.setError("Password is required");
            passwordEt.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEt.setError("Minimum password length is 6 characters");
            passwordEt.requestFocus();
            return;
        }

        progressBar.setVisibility(VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(Login.this, "Failed to login in, check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}