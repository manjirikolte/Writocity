package com.example.writocityapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingMenuActivity extends AppCompatActivity {

    TextView setting_TV_resetPass, setting_theme;
    LinearLayout setting_LL_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_menu);
        setting_TV_resetPass= findViewById(R.id.setting_TV_resetPass);
        setting_LL_logout = findViewById(R.id.setting_LL_logout);
        setting_theme = findViewById(R.id.setting_Theme);

        setting_TV_resetPass.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class)));
        setting_LL_logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

        setting_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ApplyThemeActivity.class));

            }
        });


    }
}