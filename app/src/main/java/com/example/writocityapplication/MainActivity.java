package com.example.writocityapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    //declaring variables
    LinearLayout main_LL_loginButton, main_LL_signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing views
        main_LL_loginButton = findViewById(R.id.forgotPass_LL_sendEmail);
        main_LL_signupButton =findViewById(R.id.editProfile_LL_saveChanges);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // No user is signed in
        }

//        SharedPreferences preferences = getSharedPreferences("checkboxRemember",MODE_PRIVATE);
//        String checkboxStatus = preferences.getString("remember","");
//
//        if (checkboxStatus.equals("true")){
//            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
//            startActivity(intent);
//            Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
//        }else if (checkboxStatus.equals("false")){
//            Toast.makeText(getApplicationContext(), "Please SignIn", Toast.LENGTH_SHORT).show();
//        }

        //move to login activity
        main_LL_loginButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,LoginActivity.class)));
        // move to signup activity
        main_LL_signupButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,SignupActivity.class)));
    }
}