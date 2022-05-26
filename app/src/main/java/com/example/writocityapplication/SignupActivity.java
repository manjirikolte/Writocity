package com.example.writocityapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.writocityapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    //declaring variables
    EditText signup_ET_email, signup_ET_password, signup_ET_bio, signup_ET_username, signup_ET_fullName;
    LinearLayout signup_LL_signupBtn;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_ET_email = findViewById(R.id.signup_ET_email);
        signup_ET_password = findViewById(R.id.signup_ET_password);
        signup_ET_username = findViewById(R.id.signup_ET_username);
        signup_ET_fullName = findViewById(R.id.signup_ET_fullName);
        signup_ET_bio = findViewById(R.id.signup_ET_bio);
        signup_LL_signupBtn = findViewById(R.id.profile_LL_editProfile);

        // Set properties to progress dialog box
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        signup_LL_signupBtn.setOnClickListener(view -> {
            registerUser();
        });

    }

    //send data to firebase to register user
    private void registerUser() {

        String email = signup_ET_email.getText().toString().trim();
        String password = signup_ET_password.getText().toString().trim();
        String username = signup_ET_username.getText().toString().trim();
        String bio = signup_ET_bio.getText().toString().trim();
        String fullName = signup_ET_fullName.getText().toString().trim();

        if (email.isEmpty()){
            signup_ET_email.setError("Email is required!");
            signup_ET_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signup_ET_email.setError("Please provide valid email!");
            signup_ET_email.requestFocus();
            return;
        }
        if (password.isEmpty()){
            signup_ET_password.setError("Password is required");
            signup_ET_password.requestFocus();
            return;
        }
        if (password.length() < 6){
            signup_ET_password.setError("Password should be more than 6 character");
            signup_ET_password.requestFocus();
            return;
        }

        if (username.isEmpty()){
            signup_ET_username.setError("Username is required!");
            signup_ET_username.requestFocus();
            return;
        }

        if (fullName.isEmpty()){
            signup_ET_fullName.setError("Username is required!");
            signup_ET_fullName.requestFocus();
            return;
        }
        //  Show progress dialog box loading...
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){
                        User user = new User(email,username, fullName, bio);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                Toast.makeText(SignupActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                                signup_ET_email.setText("");
                                signup_ET_password.setText("");
                                signup_ET_username.setText("");
                                signup_ET_fullName.setText("");
                                signup_ET_bio.setText("");

                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                            }else {
                                Toast.makeText(getApplicationContext(), "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                            }
                                    progressDialog.dismiss();
                        });

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to signup! Please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}