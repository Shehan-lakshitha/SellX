package com.example.sellx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;

    private TextView txtUsername;

    private EditText edtSearchBar;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    private Animation swipefromleft;
    private Animation swipefromtop;
    private Animation swipefromright;

    private ConstraintLayout middleCard;
    private ConstraintLayout constraintLayoutTop;
    private LinearLayout linearLayoutTop;

    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initValue();

        animation();

        middleCard.setAnimation(swipefromleft);


        //Button to logout
        //TODO temporary button need to be remove - logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Logout")
                        .setMessage("Are you sure you want to logout")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("haslogged","false");
                                editor.apply();

                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(MainActivity.this,Login_Activity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                builder.show();


            }
        });

        //to get user information from database
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);

                String username = userprofile.name;
                txtUsername.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something Wrong happened", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //animations in start
    private void animation() {
        middleCard.setAnimation(swipefromleft);
        constraintLayoutTop.setAnimation(swipefromtop);
        linearLayoutTop.setAnimation(swipefromtop);
        edtSearchBar.setAnimation(swipefromright);

    }

    //method to initiate elements
    private void initValue(){
        btnLogout = findViewById(R.id.btnLogout);

        txtUsername = findViewById(R.id.txtUserName);

        edtSearchBar = findViewById(R.id.edtSearchBar);

        swipefromleft = AnimationUtils.loadAnimation(this,R.anim.swipefromleft);
        swipefromtop = AnimationUtils.loadAnimation(this,R.anim.swipefromtop);
        swipefromright = AnimationUtils.loadAnimation(this,R.anim.swipefromright);

        middleCard = findViewById(R.id.middleCard);
        constraintLayoutTop = findViewById(R.id.constraintLayout);
        linearLayoutTop = findViewById(R.id.linearLayoutTop);

    }
}