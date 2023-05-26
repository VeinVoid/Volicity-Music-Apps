package com.example.volicity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    public RecycleAdaptorList(Context context, List <MusicList> musicList, ContactsAdapterListener listener){
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
        final MusicList musicList = this.musicList.get(holder.getAdapterPosition());

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
                listener.onContactSelected(musicList);
            }
        });
        holder.rlMyMusic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_menu);

                LinearLayout llPlayMusic = dialog.findViewById(R.id.llPlayMusic);
                LinearLayout llDelete = dialog.findViewById(R.id.llDelete);

                llDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            deleteItem(position);
                            dialog.dismiss();
                        }
                    }
                });

                ImageView ivPopupCoverMusic = dialog.findViewById(R.id.ivCoverMusicPopup);

                Glide.with(ivPopupCoverMusic.getContext())
                        .load(musicList.getCover())
                        .into(ivPopupCoverMusic);

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                return false;
            }
        });
        holder.ivThreeDote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_menu);

                LinearLayout llPlayMusic = dialog.findViewById(R.id.llPlayMusic);
                LinearLayout llDelete = dialog.findViewById(R.id.llDelete);

                llDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            deleteItem(position);
                            dialog.dismiss();
                        }
                    }
                });

                ImageView ivPopupCoverMusic = dialog.findViewById(R.id.ivCoverMusicPopup);

                Glide.with(ivPopupCoverMusic.getContext())
                        .load(musicList.getCover())
                        .into(ivPopupCoverMusic);

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.musicList.size();
    }

    public interface ContactsAdapterListener {
        void onContactSelected(MusicList contact);
    }

    public void deleteItem(int position) {
        musicList.remove(position);
        notifyItemRemoved(position);
    }
}

