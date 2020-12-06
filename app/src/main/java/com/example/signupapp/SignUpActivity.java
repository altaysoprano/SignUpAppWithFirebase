package com.example.signupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity {

    LinearLayout loginTextLinear;
    EditText signUpEmail, signUpPassword, signUpPasswordAgain;
    private FirebaseAuth mAuth;
    Button signUpButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginTextLinear = findViewById(R.id.login_text_linear);
        signUpEmail = findViewById(R.id.sign_up_email);
        signUpPassword = findViewById(R.id.sign_up_password);
        signUpPasswordAgain = findViewById(R.id.sign_up_password_again);
        signUpButton = findViewById(R.id.sign_up_button);
        progressBar = findViewById(R.id.progress_bar_signup);
        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.GONE);

        loginTextLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });


    }


    private void register() {
        String email = signUpEmail.getText().toString().trim();
        String password = signUpPassword.getText().toString().trim();
        String passwordAgain = signUpPasswordAgain.getText().toString().trim();

        if(email.isEmpty()) {
            signUpEmail.setError("E-posta alanını boş geçemezsiniz");
            signUpEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmail.setError("Geçersiz bir e-posta adresi girdiniz");
            signUpEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            signUpPassword.setError("Şifre alanını boş geçemezsiniz");
            signUpPassword.requestFocus();
            return;
        }

        if(password.length()<6) {
            signUpPassword.setError("Şifreniz minimum 6 karakterden oluşmalı");
            signUpPassword.requestFocus();
            return;
        }

        if(!password.equals(passwordAgain)) {
            signUpPasswordAgain.setError("Şifreleriniz birbiriyle eşleşmiyor");
            signUpPasswordAgain.requestFocus();
            return;
        }
        signUpButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                signUpButton.setVisibility(View.VISIBLE);

                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Kaydınız başarıyla gerçekleştirildi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(SignUpActivity.this, "Zaten kayıtlı kullanıcımızsınız!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Hata! Kaydınız gerçekleştirilemedi.", Toast.LENGTH_SHORT).show();
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
