package com.example.volicity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements RecycleAdaptor.ContactsAdapterListener{

    RecyclerView rvRecycleView;
    ArrayList<MusicList> musicLists;
    boolean isPlaying = false;
    private RecycleAdaptor reAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_menu);
        musicLists = new ArrayList<>();
        ListMusic();
    }

    public void ListMusic(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://volicitymusicvectoryapi.000webhostapp.com/music")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();

                    try {
                        JSONObject jsonMusicDataObject = new JSONObject(responseString);
                        JSONArray jsonMudicDataArray = jsonMusicDataObject.getJSONArray("data_music");

                        for (int i = 0; i < jsonMudicDataArray.length(); i++) {
                            MusicList musicListModel = new MusicList();
                            JSONObject jsonMusic = jsonMudicDataArray.getJSONObject(i);
                            musicListModel.setTitle(jsonMusic.getString("name"));
                            musicListModel.setCover(jsonMusic.getString("cover"));
                            musicListModel.setSinger(jsonMusic.getString("singer"));
                            musicListModel.setMusicLink(jsonMusic.getString("music"));
                            musicLists.add(musicListModel);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(() -> {
                        Loading();
                        rvRecycleView = findViewById(R.id.rvRecycleView);
                        reAdaptor = new RecycleAdaptor(getApplicationContext(), musicLists, HomeActivity.this);
                        rvRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
                        rvRecycleView.setAdapter(reAdaptor);

                    });

                } else {
                    throw new IOException("Unexpected response code " + response);
                }
            }
        });

        Cache cache = new Cache(this.getCacheDir(), 10 * 1024 * 1024);
        OkHttpClient clienCache = new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Override
    public void onContactSelected(MusicList content, int indexPos) {
        Intent music = new Intent(this, MusicPlayer.class);
        music.putExtra("myMusic", content);
        music.putExtra("musicList", musicLists);
        music.putExtra("currentPosition", indexPos);
        startActivity(music);
    }

    public void Loading(){
        RecyclerView rvRecycleView = findViewById(R.id.rvRecycleView);
        ImageView ivBanner = findViewById(R.id.ivBanner);
        TextView tvCategoryMyMusic = findViewById(R.id.tvCategoryMyMusic);
        ProgressBar progressBar = findViewById(R.id.rlProgressbar);
        progressBar.setVisibility(View.GONE);

        if (progressBar != null){
            rvRecycleView.setVisibility(View.VISIBLE);
            ivBanner.setVisibility(View.VISIBLE);
            tvCategoryMyMusic.setVisibility(View.VISIBLE);
        }
    }
}