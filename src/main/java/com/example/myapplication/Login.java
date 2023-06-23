package com.example.myapplication;import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Fragments.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText EditInputEmail, EditInputPassword;
    private ProgressBar pBar;
    private Button loginB;
    TextView GoToSignup;

    private static final String TAG = "login";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditInputEmail = findViewById(R.id.login_username);
        EditInputPassword = findViewById(R.id.login_password);
        pBar = findViewById(R.id.ProgressBar);
        GoToSignup = findViewById(R.id.signuplink);
        GoToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        loginB = findViewById(R.id.login_button);
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = EditInputEmail.getText().toString();
                String pass = EditInputPassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Please enter your email ", Toast.LENGTH_LONG).show();
                    EditInputEmail.setError("Email is necessary ");
                    EditInputEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Login.this, "Please re-enter your email ", Toast.LENGTH_LONG).show();
                    EditInputEmail.setError("Inappropriate email ");
                    EditInputEmail.requestFocus();
                } else if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(Login.this, "Please enter your password ", Toast.LENGTH_LONG).show();
                    EditInputPassword.setError("Password is required   ");
                    EditInputPassword.requestFocus();
                } else {
                    //make progress bar visible
                    pBar.setVisibility(View.VISIBLE);
                    login(email, pass);
                }
            }
        });
    }

    private void login(String email, String pass) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //log in the user
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //check if the user has verified their email
                    if (auth.getCurrentUser().isEmailVerified()) {
                        Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                        //open user profile after successful login
                        Intent intent = new Intent(Login.this, Profile.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Confirm your email first", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                pBar.setVisibility(View.GONE);
            }
        });
    }
}
