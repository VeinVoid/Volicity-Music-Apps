package com.example.volicity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RecycleAdaptorList extends RecyclerView.Adapter<RecycleAdaptorList.RecycleViewHolder> {

    private Context context;
    private List<MusicList> musicList;
    private ContactsAdapterListener listener;

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMusicName, tvSingerName;
        public ImageView ivCoverMusic, ivThreeDote;
        RelativeLayout rlMyMusic;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMusicName = itemView.findViewById(R.id.tvMusicName);
            tvSingerName = itemView.findViewById(R.id.tvSingerName);
            ivCoverMusic = itemView.findViewById(R.id.ivCoverMusic);
            ivThreeDote = itemView.findViewById(R.id.ivthreeDot);
            rlMyMusic = itemView.findViewById(R.id.rlMyMusic);
        }
    }

    public RecycleAdaptorList(Context context, List <MusicList> musicList){
        this.context = context;
        this.musicList = musicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_my_music, parent, false);
        return new RecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdaptorList.RecycleViewHolder holder, int position) {
        final MusicList musicList = this.musicList.get(position);

        holder.tvMusicName.setText(musicList.getTitle());
        holder.tvSingerName.setText(musicList.getSinger());
        Glide.with(holder.ivCoverMusic.getContext())
                .load(musicList.getCover())
                .fitCenter()
                .apply(new RequestOptions().override(192,146))
                .into(holder.ivCoverMusic);
        holder.rlMyMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                listener.onContactSelected(musicList, position);
            }
        });
        holder.ivThreeDote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupMenu = inflater.inflate(R.layout.popup_menu, null);

            }
        });

    }

    @Override
    public int getItemCount() {
        return this.musicList.size();
    }

    public interface ContactsAdapterListener {
        void onContactSelected(MusicList musicList, int position);
    }
}

