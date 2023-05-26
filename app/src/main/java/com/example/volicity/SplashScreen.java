package com.example.volicity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SplashScreen extends AppCompatActivity {

    TextView tvAccountUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        EventBus.getDefault().register(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "run: " + tvAccountUsername);
                Intent homeScreen = new Intent(getApplicationContext(), HomePage.class);
                startActivity(homeScreen);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onUserDataReceived(UserData userData) {

        tvAccountUsername = findViewById(R.id.tvAccountUsername);

        String username = userData.getUsername();

        tvAccountUsername.setText(username);
    }
}