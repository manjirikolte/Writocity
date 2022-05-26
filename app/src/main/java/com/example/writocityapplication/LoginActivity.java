package com.example.writocityapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText login_ET_enterEmail, login_ET_enterPassword;
    TextView login_TV_forgotPass;
    CheckBox login_checkbox_rememberMe;

    LinearLayout login_Button_login;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_ET_enterEmail = findViewById(R.id.login_ET_enterEmail);
        login_ET_enterPassword = findViewById(R.id.login_ET_enterPass);
        login_Button_login = findViewById(R.id.login_Button_login);
        login_TV_forgotPass = findViewById(R.id.login_TV_forgotPass);
        login_checkbox_rememberMe = findViewById(R.id.login_checkbox_rememberMe);



        // Set properties to progress dialog box
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
       // login_Button_login.setOnClickListener(view -> userLogin());

        login_TV_forgotPass.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class)));

        login_checkbox_rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkboxRemember", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "checked", Toast.LENGTH_SHORT).show();

                }else if (!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkboxRemember", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Unchecked", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    // check credentials
    private void userLogin() {
        String email = login_ET_enterEmail.getText().toString().trim();
        String password = login_ET_enterPassword.getText().toString().trim();

        if (email.isEmpty()){
            login_ET_enterEmail.setError("Email is required!");
            login_ET_enterEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            login_ET_enterEmail.setError("Please provide valid email!");
            login_ET_enterEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            login_ET_enterPassword.setError("Password is required");
            login_ET_enterPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            login_ET_enterPassword.setError("Password should be more than 6 character");
            login_ET_enterPassword.requestFocus();
            return;
        }

        //  Show progress dialog box loading...
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), "Failed to login!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }
}