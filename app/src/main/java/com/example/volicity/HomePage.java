package com.example.volicity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomePage extends AppCompatActivity {

    ImageView ivthreeDot;
    RecyclerView rvTopMusic, rvListMusic;
    RecycleAdaptorTopList adaptorTopList;
    RecycleAdaptorList adaptorList;
    ArrayList<MusicList> listMusic = new ArrayList<>();
    ArrayList<MusicList> topListMusic = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        getMusicAPITopMusic();
        listMusic();

    }

    public void getMusicAPITopMusic() {

        AndroidNetworking.get("https://volicitymusicvectoryapi.000webhostapp.com/music?sort=name")
                .addQueryParameter("sort","view")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("TAG", "onResponse: " + jsonObject);
                        try {
                            JSONArray jsonMusicList = jsonObject.getJSONArray("data_music");
                            for (int i = 0; i < jsonMusicList.length(); i++) {
                                MusicList musicList = new MusicList();
                                JSONObject jsonMusic = jsonMusicList.getJSONObject(i);
                                musicList.setId(jsonMusic.getInt("id"));
                                musicList.setTitle(jsonMusic.getString("name"));
                                musicList.setSinger(jsonMusic.getString("singer"));
                                musicList.setCover(jsonMusic.getString("cover"));
                                musicList.setViewCount(jsonMusic.getInt("view"));
                                listMusic.add(musicList);
                                topListMusic.add(musicList);
                            }

                            topListMusic();
                            listMusic();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void topListMusic(){
        Collections.sort(topListMusic, new Comparator<MusicList>() {
            @Override
            public int compare(MusicList music1, MusicList music2) {
                return Integer.compare(music2.getViewCount(), music1.getViewCount());
            }
        });

        List<MusicList> topMusicList = topListMusic.subList(0, Math.min(5, topListMusic.size()));

        rvTopMusic = findViewById(R.id.rvTopMusic);
        adaptorTopList = new RecycleAdaptorTopList(getApplicationContext(), topMusicList);
        rvTopMusic.setLayoutManager(new LinearLayoutManager(HomePage.this, RecyclerView.HORIZONTAL, false));
        rvTopMusic.setAdapter(adaptorTopList);

        int margin = getResources().getDimensionPixelSize(R.dimen.last_item_margin);
        rvTopMusic.addItemDecoration(new LastItemMarginDecoration(margin));
    }

    public void listMusic(){

        Collections.sort(listMusic, new Comparator<MusicList>() {
            @Override
            public int compare(MusicList music1, MusicList music2) {
                return Integer.compare(music1.getId(), music2.getId());
            }
        });

        ivthreeDot = findViewById(R.id.ivthreeDot);

        rvListMusic = findViewById(R.id.rvListMusic);
        adaptorList = new RecycleAdaptorList(getApplicationContext(), listMusic);
        rvListMusic.setLayoutManager(new LinearLayoutManager(HomePage.this, RecyclerView.VERTICAL, false));
        rvListMusic.setAdapter(adaptorList);
    }

    public class LastItemMarginDecoration extends RecyclerView.ItemDecoration {
        private int margin;

        public LastItemMarginDecoration(int margin) {
            this.margin = margin;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildAdapterPosition(view);
            int itemCount = parent.getAdapter().getItemCount();

            if (position == itemCount - 1) {
                outRect.right = margin;
            }
        }
    }


}