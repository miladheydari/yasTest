package com.yas.features.musicList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;
import com.yas.R;

import com.yas.pojo.MusicDetail;

public class MusicListAdapter extends RecyclerView.Adapter {

    Context context;
    public List<MusicDetail> mlist;
    Picasso picasso;

    public MusicListAdapter(Context context, Picasso picasso, List<MusicDetail> mlist) {
        this.context = context;
        this.picasso = picasso;
        this.mlist = mlist == null ? new ArrayList<MusicDetail>() : mlist;
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, MusicDetail video);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;


    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public void addItem(MusicDetail v) {
        mlist.add(v);
        notifyItemInserted(mlist.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_music_list, parent, false);
        return new Holder(v, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((Holder) holder).bind();

    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        ImageView imThumbnail;
        TextView tvArtist;
        View item;
        private MusicListAdapter parent;

        public Holder(View itemView, MusicListAdapter parent) {
            super(itemView);
            this.parent = parent;
            this.imThumbnail = (ImageView) itemView.findViewById(R.id.imVideoThumbnail);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            this.tvArtist = (TextView) itemView.findViewById(R.id.tvArtist);
            this.item = itemView;
        }


        public void bind() {
            MusicDetail mv = parent.mlist.get(getAdapterPosition());

            if (mv.title != null)
                tvName.setText(mv.title);
            if (mv.artist != null)
                tvArtist.setText(mv.artist);

            if (mv.imagePath != null)
                picasso.load(mv.imagePath).into(imThumbnail);
//
//            if(mv.getmThumbnail()!=null)
//                Picasso.with(parent.context).load(mv.getmThumbnail()).into(imThumbnail);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (parent.getOnItemClickListener() != null)
                parent.getOnItemClickListener().onItemClick(getAdapterPosition(), parent.mlist.get(getAdapterPosition()));
        }
    }
}
