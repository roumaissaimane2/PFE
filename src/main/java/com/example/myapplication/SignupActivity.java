package com.example.myapplication;
import androidx.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PrivateKey;

public class SignupActivity extends AppCompatActivity {
    private EditText   EditInputFullname,EditInputEmail,EditInputPassword,EditInputRpass;
    private ProgressBar pBar;
    private Button signupB;
    TextView GoToLogin;
    DatabaseReference referenceprofile;

    private static final String TAG="signup";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EditInputFullname=findViewById(R.id.fullname);
        EditInputEmail=findViewById(R.id.email);
        EditInputPassword=findViewById(R.id.password);

        EditInputRpass =findViewById(R.id.Resetpassword);
        pBar=findViewById(R.id.ProgressBar);
        GoToLogin=findViewById(R.id.signlink);
        GoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SignupActivity.this,Login.class);
                startActivity(intent);

            }
        });

        signupB=findViewById(R.id.signup_button);
        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=EditInputFullname.getText().toString();
                String email=EditInputEmail.getText().toString();
                String pass=EditInputPassword.getText().toString();
                String Rpass=EditInputRpass.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(SignupActivity.this,"Please enter your full name ",Toast.LENGTH_LONG).show();
                    EditInputFullname.setError("The name is necessary ");
                    EditInputFullname.requestFocus();

                }else if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignupActivity.this,"Please enter your email ",Toast.LENGTH_LONG).show();
                    EditInputEmail.setError("Email is necessary");
                    EditInputEmail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(SignupActivity.this,"Please re-enter your email ",Toast.LENGTH_LONG).show();
                    EditInputEmail.setError("Inappropriate email ");
                    EditInputEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(pass)){
                    Toast.makeText(SignupActivity.this,"Please enter the password ",Toast.LENGTH_LONG).show();
                    EditInputPassword.setError("Password is required   ");
                    EditInputPassword.requestFocus();
                    //verifier if the password length more then 8 characters

                }else if (TextUtils.isEmpty(Rpass)){
                    Toast.makeText(SignupActivity.this,"Please confirm your password ",Toast.LENGTH_LONG).show();
                    EditInputRpass.setError("Password confirmation is required");
                    EditInputRpass.requestFocus();
                } else{
                    //make progresse bar visible
                    pBar.setVisibility(View.VISIBLE);
                    register(name,email,pass);
                }
            }
        });

    }
    private void register(String name,String email,String pass){
        FirebaseAuth auth =FirebaseAuth.getInstance();
        //create user profile
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignupActivity.this,"the registration is done",Toast.LENGTH_LONG).show();
                    //enter user data into the firebase realtime database

                    Users writeUserDetails =new Users(name,email);


                    //Extracting User referene from Database for "signup users"
                    referenceprofile = FirebaseDatabase.getInstance().getReference("signupusers");

                    referenceprofile.child("users").child(auth.getCurrentUser().getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignupActivity.this,"successfully registered",Toast.LENGTH_LONG).show();
                                //send verfication email
                                auth.getCurrentUser().sendEmailVerification();
                                //open user profile after sucessful signup
                                Intent intent = new Intent(SignupActivity.this, Profile.class);

                                startActivity(intent);

                            }
                            else{
                                Toast.makeText(SignupActivity.this,"Registration failed, please try again",Toast.LENGTH_LONG).show();

                            }
                            pBar.setVisibility(View.GONE);
                        }
                    });

                }else {
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        EditInputPassword.setError("your password is too weak use a mix alphabets ,numbersand special characters");
                        EditInputPassword.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        EditInputEmail.setError("our email is invalaid or already is use");
                        EditInputEmail.requestFocus();
                    }catch (FirebaseAuthUserCollisionException e){
                        EditInputEmail.setError("user is already registerd with this email.use anthore email");
                        EditInputEmail.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(SignupActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                    pBar.setVisibility(View.GONE);
                }
            }
        });
    }
}