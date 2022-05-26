package com.example.writocityapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText forgotPass_ET_email;
    LinearLayout forgotPass_LL_sendEmail;
    TextView forgotPass_TF_resetPass;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotPass_ET_email = findViewById(R.id.search_post);
        forgotPass_LL_sendEmail = findViewById(R.id.forgotPass_LL_sendEmail);
      //  forgotPass_TF_resetPass = findViewById(R.id.forgotPass_TF_resetPass);

        mAuth = FirebaseAuth.getInstance();


        forgotPass_LL_sendEmail.setOnClickListener(view -> resetPassword());
    }

    private void resetPassword() {
        String email = forgotPass_ET_email.getText().toString().trim();

        if (email.isEmpty()){
            forgotPass_ET_email.setError("Email is required!");
            forgotPass_ET_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            forgotPass_ET_email.setError("Please provide valid email!");
            forgotPass_ET_email.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
                }else {

                }

            }
        });
    }
}