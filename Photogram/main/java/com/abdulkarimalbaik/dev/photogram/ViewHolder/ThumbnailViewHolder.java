package com.abdulkarimalbaik.dev.photogram.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulkarimalbaik.dev.photogram.R;

public class ThumbnailViewHolder extends RecyclerView.ViewHolder {

    public ImageView thumbnail;
    public TextView filter_name;

    public ThumbnailViewHolder(View itemView) {
        super(itemView);

        thumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
        filter_name = (TextView)itemView.findViewById(R.id.filter_name);
    }

}
