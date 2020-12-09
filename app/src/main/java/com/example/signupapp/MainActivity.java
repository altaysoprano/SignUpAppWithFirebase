package com.example.signupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity {

    LinearLayout signUpTextLinear;
    Button loginButton;
    FirebaseAuth mAuth;
    EditText logInEmail, logInPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpTextLinear = findViewById(R.id.sign_up_text_linear);
        loginButton = findViewById(R.id.login_button);
        logInEmail = findViewById(R.id.login_email);
        logInPassword = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.progress_bar_login);
        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.GONE);

        signUpTextLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

    }

    private void logIn() {

        String email = logInEmail.getText().toString().trim();
        String password = logInPassword.getText().toString().trim();

        if(email.isEmpty()) {
            logInEmail.setError("E-posta alanını boş geçemezsiniz");
            logInEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            logInPassword.setError("Şifre alanını boş geçemezsiniz");
            logInPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);

                if(task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Hata! Giriş yapılamadı", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}
