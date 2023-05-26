package com.example.volicity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    TextView tvAccountUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        tvAccountUsername = findViewById(R.id.tvAccountUsername);

        Intent intent = getIntent();
        UserData userData = intent.getParcelableExtra("userData");
        tvAccountUsername.setText(userData.Username);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeScreen = new Intent(getApplicationContext(), HomePage.class);
                startActivity(homeScreen);
                finish();
            }
        }, 1000);
    }
}