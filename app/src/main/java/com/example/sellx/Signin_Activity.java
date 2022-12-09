package com.example.sellx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Signin_Activity extends AppCompatActivity {

    private EditText edtName,edtEmail,edtPassword,edtComPassword;
    private Button btnSignin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();

        initValue();

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


    }
    private void registerUser(){
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String compassword = edtComPassword.getText().toString().trim();

        if (name.isEmpty()){
            edtName.setError("Please enter your name");
            edtName.requestFocus();
            return;
        }
        if (email.isEmpty()){
            edtEmail.setError("Please enter your email");
            edtEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Please enter a vaild email");
            edtEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            edtPassword.setError("Please enter a password");
            edtPassword.requestFocus();
            return;
        }
        if (compassword.isEmpty()){
            edtComPassword.setError("Please confirm your password");
            edtComPassword.requestFocus();
            return;
        }
        if (!compassword.equals(password)){
            edtComPassword.setError("Password mismatch");
            edtComPassword.requestFocus();
            edtComPassword.setText("");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(name,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Signin_Activity.this, "Sign in Successful", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(Signin_Activity.this,Login_Activity.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(Signin_Activity.this, "Sign in Failed, Please try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(Signin_Activity.this, "Sign in Failed, Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    //method to initialize the buttons
    private void initValue(){
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtComPassword = findViewById(R.id.edtComPassword);

        btnSignin = findViewById(R.id.btnSignIn);
    }
}