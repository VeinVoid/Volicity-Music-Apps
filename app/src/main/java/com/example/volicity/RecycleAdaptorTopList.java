package com.example.volicity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RecycleAdaptorTopList extends RecyclerView.Adapter<RecycleAdaptorTopList.RecycleViewHolder> {

    private Context context;
    private List<MusicList> musicList;
    private ContactsAdapterListener listener;

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTopMusicName, tvTopMusicArtis;
        public ImageView ivTopMusicCover;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopMusicName = itemView.findViewById(R.id.tvTopMusicName);
            tvTopMusicArtis = itemView.findViewById(R.id.tvTopMusicArtis);
            ivTopMusicCover = itemView.findViewById(R.id.ivTopMusicCover);
        }
    }

    public RecycleAdaptorTopList(Context context, List <MusicList> musicList){
        this.context = context;
        this.musicList = musicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_my_top_music, parent, false);
        return new RecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdaptorTopList.RecycleViewHolder holder, int position) {
        final MusicList musicList = this.musicList.get(position);

        holder.tvTopMusicName.setText(musicList.getTitle());
        holder.tvTopMusicArtis.setText(musicList.getSinger());
        Glide.with(holder.ivTopMusicCover.getContext())
                .load(musicList.getCover())
                .fitCenter()
                .apply(new RequestOptions().override(192,146))
                .into(holder.ivTopMusicCover);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                listener.onContactSelected(musicList, position);
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
