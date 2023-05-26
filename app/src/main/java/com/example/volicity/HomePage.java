package com.example.volicity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class HomePage extends AppCompatActivity implements RecycleAdaptorList.ContactsAdapterListener{

    ProgressBar pbload;
    TextView tvTopMusic;
    ImageView ivthreeDot;
    ImageButton ibHome, ibSearch, ibAdd, ibProfile;
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

        GetMusicAPI();
        ListMusic();
        Navbar();

    }

    //Get Music API
    public void GetMusicAPI() {
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
                                musicList.setMusicLink(jsonMusic.getString("music"));
                                musicList.setViewCount(jsonMusic.getInt("view"));
                                listMusic.add(musicList);
                                topListMusic.add(musicList);
                            }

                            tvTopMusic = findViewById(R.id.tvTopMusic);
                            tvTopMusic.setVisibility(View.VISIBLE);
                            View rvFilter = findViewById(R.id.rvFilter);
                            rvFilter.setVisibility(View.VISIBLE);
                            View cvNavbar = findViewById(R.id.cvNavbar);
                            cvNavbar.setVisibility(View.VISIBLE);
                            pbload = findViewById(R.id.pbload);
                            pbload.setVisibility(View.GONE);
                            TopListMusic();
                            ListMusic();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    //Filtering Top Music List API
    public void TopListMusic(){
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

    //Filtering Music List API
    public void ListMusic(){
        Collections.sort(listMusic, new Comparator<MusicList>() {
            @Override
            public int compare(MusicList music1, MusicList music2) {
                return Integer.compare(music1.getId(), music2.getId());
            }
        });

        ivthreeDot = findViewById(R.id.ivthreeDot);

        rvListMusic = findViewById(R.id.rvListMusic);
        adaptorList = new RecycleAdaptorList(getApplicationContext(), listMusic, this);
        rvListMusic.setLayoutManager(new LinearLayoutManager(HomePage.this, RecyclerView.VERTICAL, false));
        rvListMusic.setAdapter(adaptorList);

        int margin = 120;
        rvListMusic.addItemDecoration(new LastItemMarginDecorationL(margin));
    }

    //Adaptor Click Listener
    @Override
    public void onContactSelected(MusicList contact) {
        Intent musicPlayer = new Intent(this, MusicPlayer.class);
        musicPlayer.putExtra("onlineMusic", contact);
        musicPlayer.putExtra("arrayMusic", listMusic);
        startActivity(musicPlayer);
    }

    //Add Margin In Last Index Off Top Music List API
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

    //Add Margin In Last Index Off Music List API
    public class LastItemMarginDecorationL extends RecyclerView.ItemDecoration {
        private int margin;

        public LastItemMarginDecorationL(int margin) {
            this.margin = margin;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildAdapterPosition(view);
            int itemCount = parent.getAdapter().getItemCount();

            if (position == itemCount - 1) {
                outRect.bottom = margin;
            }
        }
    }

    //NavBar
    public void Navbar(){
        ibHome = findViewById(R.id.ibHome);
        ibSearch = findViewById(R.id.ibSearch);
        ibAdd = findViewById(R.id.ibAdd);
        ibProfile = findViewById(R.id.ibprofile);
        ibProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ProfilePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}