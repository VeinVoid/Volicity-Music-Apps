package com.example.volicity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    //Timer Elepased PerSecond
    int elapsedSeconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        //Get All Data From Home Activity
        Intent music = getIntent();
        MusicList musicList = (MusicList) music.getParcelableExtra("onlineMusic");
        ArrayList<MusicList> musicLists = getIntent().getParcelableArrayListExtra("musicList");
        int currentIndex = getIntent().getIntExtra("currentPosition", 0);

        Log.d("TAG", "onCreate: " + musicList.musicLink);

        //Connect The Activity With XML
        ImageView ivmusicCover = findViewById(R.id.ivmusicCover);
        ImageView ivPlayerButton = findViewById(R.id.ivPlayerButton);
        ImageView ivBackButton = findViewById(R.id.ivBackButton);
        ImageView ivNextPlus = findViewById(R.id.ivNextPlus);
        ImageView ivBackMines = findViewById(R.id.ivBackMines);
        TextView tvMusicTittle = findViewById(R.id.tvMusicTittle);
        TextView tvSinger = findViewById(R.id.tvSinger);
        TextView tvMusicDurationP = findViewById(R.id.tvMusicDurationP);
        TextView tvMusicDurationB = findViewById(R.id.tvMusicDurationB);

        //Set Cover Image
        Glide.with(this).load(musicList.getCover()).into(ivmusicCover);
        tvMusicTittle.setText(musicList.getTitle());
        tvSinger.setText(musicList.getSinger());

        //Get Audio URL
        String audioUrl = musicList.musicLink;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

            int duration = mediaPlayer.getDuration();
            int minutes = (duration / 1000) / 60;
            int seconds = (duration / 1000) % 60;
            String durationString = String.format("%02d:%02d", minutes, seconds);

            Handler handler = new Handler();
            Runnable updateTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    elapsedSeconds++;
                    int minutes = elapsedSeconds / 60;
                    int seconds = elapsedSeconds % 60;
                    String currentTimeString = String.format("%02d:%02d", minutes, seconds);
                    tvMusicDurationP.setText(currentTimeString);
                    handler.postDelayed(this, 1000);
                }
            };
            handler.postDelayed(updateTimeRunnable, 1000);

            new CountDownTimer(duration, 1000) {
                public void onTick(long millisUntilFinished) {
                    int secondsRemaining = (int) (millisUntilFinished / 1000);
                    int minutes = secondsRemaining / 60;
                    int seconds = secondsRemaining % 60;
                    String remainingTimeString = String.format("%02d:%02d", minutes, seconds);
                    tvMusicDurationB.setText(remainingTimeString);
                }

                public void onFinish() {
                    tvMusicDurationB.setText("00:00");
                }
            }.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Back To HomeActivity Button
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                mediaPlayer.release();
            }
        });

        //Pause And Play Button
        ivPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    ivPlayerButton.setImageResource(R.drawable.playbutton);
                    mediaPlayer.pause();
                    Log.d("TAG", "onClick: " + currentIndex);
                }
                else {
                    ivPlayerButton.setImageResource(R.drawable.pausebutton);
                    mediaPlayer.start();
                }
            }
        });

        //Next Music Button
        ivNextPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextMusic = new Intent(MusicPlayer.this, MusicPlayer.class);
                MusicList nextMusicList = null;
                int nextIndex = currentIndex + 1;
                if (nextIndex >= musicLists.size()){
                    nextIndex = 0;
                    nextMusicList = musicLists.get(nextIndex);
                }
                else {
                    nextMusicList = musicLists.get(nextIndex);
                }
                nextMusic.putExtra("myMusic", nextMusicList);
                nextMusic.putExtra("isPlaying", true);
                nextMusic.putExtra("currentPosition", nextIndex);
                nextMusic.putParcelableArrayListExtra("musicList", musicLists);
                mediaPlayer.release();
                startActivity(nextMusic);
                finish();
            }
        });

        //Previous Music Button
        ivBackMines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextMusic = new Intent(MusicPlayer.this, MusicPlayer.class);
                MusicList previousMusicList = null;
                int previousIndex = currentIndex - 1;
                if (previousIndex <= -1){
                    previousIndex = musicLists.size() - 1;
                    previousMusicList = musicLists.get(musicLists.size() - 1);
                }
                else {
                    previousMusicList = musicLists.get(previousIndex);
                }
                nextMusic.putExtra("myMusic", previousMusicList);
                nextMusic.putExtra("isPlaying", true);
                nextMusic.putExtra("currentPosition", previousIndex);
                nextMusic.putParcelableArrayListExtra("musicList", musicLists);
                mediaPlayer.release();
                startActivity(nextMusic);
                finish();
            }
        });
    }
}