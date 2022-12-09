package com.example.sellx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    private Button btnLogin;
    private EditText edtEmail, edtPassword;
    private TextView txtPasswordRecovery;
    private CheckBox CheckboxRememberMe;

    private ConstraintLayout constraintLayoutLogin;

    private FirebaseAuth mAuth;

    public static final String SHARED_PREFS = "sharedPrefs";
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initValue();
        mAuth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Method for user login
                userLogin();
            }
        });

        txtPasswordRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment_password_recovery = new PasswordRecoveryFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.constraintLayoutLogin,fragment_password_recovery).commit();
                //todo fix error in fragment

            }
        });

    }


    //method for user login with remember me
    private void userLogin(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty()){
            edtEmail.setError("Please enter your email");
            edtEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Please enter a valid email");
            edtEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            edtPassword.setError("Please enter your password");
            edtEmail.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //verifying the email if not verified
                    if (user.isEmailVerified()){

                        //remember the user when the check box is checked
                        //implemented in splash activity
                        if (CheckboxRememberMe.isChecked()){
                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("haslogged","true");
                            editor.apply();
                        }
                        Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login_Activity.this,MainActivity.class);
                        startActivity(intent);
                    }else {
                        user.sendEmailVerification();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
                        builder.setTitle("Email Verification")
                                .setMessage("A verification link was sent to your email: " + email + ".Please click the link to verify your account")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create();
                        builder.show();
                    }

                }else {
                    Toast.makeText(Login_Activity.this, "Login Failed, Please check your credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    //initialing elements
    private void initValue(){
        btnLogin = findViewById(R.id.btnLogin);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        txtPasswordRecovery = findViewById(R.id.txtPasswordRecovery);

        CheckboxRememberMe = findViewById(R.id.CheckboxRememberMe);

        constraintLayoutLogin = findViewById(R.id.constraintLayoutLogin);
        
    }
}