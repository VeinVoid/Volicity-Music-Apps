package com.example.volicity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ProfilePage extends AppCompatActivity {

    ImageButton ibHome, ibSearch, ibAdd, ibProfile;
    TextView tvAccountUsername, tvAccountEmail;
    ImageView ivPhotoProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        EventBus.getDefault().register(this);

        Navbar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onUserDataReceived(UserData userData) {

        tvAccountUsername = findViewById(R.id.tvAccountUsernameProf);
        tvAccountEmail = findViewById(R.id.tvAccountEmail);
        ivPhotoProfile = findViewById(R.id.ivPhotoProfile);

        String username = userData.getUsername();
        String email = userData.getEmail();
        Uri imageProfile = userData.getImageProfile();

        tvAccountUsername.setText(username);
        tvAccountEmail.setText(email);
        Glide.with(this).load(imageProfile).into(ivPhotoProfile);
    }

    //NavBar
    public void Navbar(){
        ibHome = findViewById(R.id.ibHome);
        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
        ibSearch = findViewById(R.id.ibSearch);
        ibAdd = findViewById(R.id.ibAdd);
        ibProfile = findViewById(R.id.ibprofile);
    }
}