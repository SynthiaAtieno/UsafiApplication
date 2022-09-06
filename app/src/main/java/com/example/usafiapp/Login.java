package com.example.usafiapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private TextView registertv, forgotpassword;
    AppCompatButton login;
    private ProgressDialog loader;
    TextInputEditText loginemail, loginpassword;

    private FirebaseAuth.AuthStateListener authStateListener;


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null)
                {
                    startActivity(new Intent(Login.this,MainActivity.class));
                    finish();
                }
            }
        };
        setContentView(R.layout.activity_login);
        registertv = findViewById(R.id.back_btn);
        login = findViewById(R.id.Login_btn);
        mAuth = FirebaseAuth.getInstance();
        loginemail = findViewById(R.id.loginEmail);
        loginpassword = findViewById(R.id.loginPassword);
        forgotpassword = findViewById(R.id.fogortPassword);
        loader = new ProgressDialog(this);

        registertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Select_Registration.class));
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordReset = new AlertDialog.Builder(view.getContext());
                passwordReset.setTitle("Reset Password");
                passwordReset.setMessage("Enter Your Email To Receive Reset Link.");
                passwordReset.setView(resetMail);

                passwordReset.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetMail.getText().toString();
                        if (mail.isEmpty()){
                            resetMail.setError("Please enter your email address");
                            resetMail.requestFocus();
                        }else
                            mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Login.this, "Reset Link Has Been Sent to Your Email", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Login.this, "Error, Reset Link Has Nt Been Sent "+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                    }
                });
                passwordReset.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passwordReset.show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginemail.getText().toString().trim();
                String password = loginpassword.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                {
                    loginemail.setError("Email is Required");
                    loginemail.requestFocus();
                }
                if (TextUtils.isEmpty(password))
                {
                    loginpassword.setError("Email is Required");
                    loginpassword.requestFocus();
                }
                else {
                    loader.setCanceledOnTouchOutside(false);
                    loader.setMessage("Signing In");
                    loader.show();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                if (mAuth.getCurrentUser().isEmailVerified()){
                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                }
                                else {
                                    Toast.makeText(Login.this, "Email is not yet verified", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(Login.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}