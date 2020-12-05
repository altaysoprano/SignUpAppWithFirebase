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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    LinearLayout loginTextLinear;
    EditText editTextEmail, editTextPassword, editTextPasswordAgain;
    private FirebaseAuth mAuth;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginTextLinear = findViewById(R.id.login_text_linear);
        editTextEmail = findViewById(R.id.sign_up_email);
        editTextPassword = findViewById(R.id.sign_up_password);
        editTextPasswordAgain = findViewById(R.id.sign_up_password_again);
        signUpButton = findViewById(R.id.sign_up_button);
        mAuth = FirebaseAuth.getInstance();

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
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordAgain = editTextPasswordAgain.getText().toString().trim();

        if(email.isEmpty()) {
            editTextEmail.setError("E-posta alanını boş geçemezsiniz");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Geçersiz bir e-posta adresi girdiniz");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError("Şifre alanını boş geçemezsiniz");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6) {
            editTextPassword.setError("Şifreniz minimum 6 karakterden oluşmalı");
            editTextPassword.requestFocus();
            return;
        }

        if(!password.equals(passwordAgain)) {
            editTextPasswordAgain.setError("Şifreleriniz birbiriyle eşleşmiyor");
            editTextPasswordAgain.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Kaydınız başarıyla gerçekleştirildi", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Kaydınız gerçekleştirilemedi", Toast.LENGTH_SHORT).show();
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
