package com.homework.ahhstatistic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText inputedt1, inputedt2;
    Button btnRegister;
    TextView toLogin;

    FirebaseAuth mAuth;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        inputedt1 = findViewById(R.id.register_edt_email);
        inputedt2 = findViewById(R.id.register_edt_password);
        btnRegister = findViewById(R.id.btn_register);
        toLogin = findViewById(R.id.to_login);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFirebase();
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private boolean validateEmail() {
        email = inputedt1.getText().toString();

        if (email.isEmpty()) {
            inputedt1.setError("Email cannot be empty !");
            return false;
        } else {
            inputedt1.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        password = inputedt2.getText().toString();

        if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Password needed", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            inputedt2.setError(null);
            return true;
        }
    }

    private void RegisterFirebase() {
        if (!validateEmail() | !validatePassword()) {
            return;
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registration completed", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                }
            });
        }
    }
}