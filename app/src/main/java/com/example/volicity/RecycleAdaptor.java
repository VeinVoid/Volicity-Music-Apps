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

public class RecycleAdaptor extends RecyclerView.Adapter<RecycleAdaptor.RecycleViewHolder> {

    private Context context;
    private List<MusicList> musicList;
    private ContactsAdapterListener listener;

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitleMusic;
        public ImageView ivCoverMusic;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleMusic = itemView.findViewById(R.id.tvTitleMusic);
            ivCoverMusic = itemView.findViewById(R.id.ivCoverMusic);
        }
    }

    public RecycleAdaptor(Context context, List <MusicList> musicList, ContactsAdapterListener listener){
        this.context = context;
        this.musicList = musicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecycleAdaptor.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_my_music, parent, false);

        return new RecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdaptor.RecycleViewHolder holder, int position) {
        final MusicList musicList = this.musicList.get(position);
        holder.tvTitleMusic.setText(musicList.getTitle());
        Glide.with(holder.ivCoverMusic.getContext())
                .load(musicList.getCover())
                .apply(new RequestOptions().override(108,108))
                .into(holder.ivCoverMusic);
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
