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

public class UserRegistration extends AppCompatActivity {

    private Button registerButton;
    private TextView logo;
    private EditText nameET, emailET, passwordET;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogo();
            }
        });

        nameET = findViewById(R.id.userName);
        emailET = findViewById(R.id.emailReg);
        passwordET = findViewById(R.id.passwordReg);

        progressBar = findViewById(R.id.progressBar);

    }

    private void registerUser() {
        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (name.isEmpty()) {
            nameET.setError("Full name required");
            nameET.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailET.setError("E-mail required");
            emailET.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Provide valid E-mail");
            emailET.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordET.setError("Password is required");
            passwordET.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordET.setError("Minimum password length is 6 characters");
            passwordET.requestFocus();
            return;
        }

        progressBar.setVisibility(VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            Toast.makeText(UserRegistration.this, "User has been registered successfully!", Toast.LENGTH_LONG);
                            startActivity(new Intent(UserRegistration.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(UserRegistration.this, "Failed to register user!", Toast.LENGTH_LONG);
                        }
                        progressBar.setVisibility(View.GONE);
                    };

                });

    }


    public void onClickLogo() {
        startActivity(new Intent(UserRegistration.this, Login.class));
        finish();
    }
}