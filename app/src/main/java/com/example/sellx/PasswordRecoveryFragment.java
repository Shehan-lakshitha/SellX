package com.example.sellx;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class PasswordRecoveryFragment extends Fragment {

   private EditText edtEmail;
   private Button btnRecover;
   private TextView txtNotice;

   FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_recovery, container, false);

        edtEmail = view.findViewById(R.id.edtEmail);
        btnRecover = view.findViewById(R.id.btnRecover);
        txtNotice = view.findViewById(R.id.txtNotice);

        auth = FirebaseAuth.getInstance();

        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpassword();
            }
        });



        return view;
    }

    private void resetpassword(){
        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty()){
            edtEmail.setError("Please enter your Email");
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Please enter vaild email");
            edtEmail.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    txtNotice.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(),Login_Activity.class);
                startActivity(intent);
            }
        },3000);


    }
}