package com.abdulkarimalbaik.dev.photogram.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.abdulkarimalbaik.dev.photogram.Interface.IRecyclerItemClickListener;
import com.abdulkarimalbaik.dev.photogram.R;

public class FrameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView img_frame , img_check;

    private IRecyclerItemClickListener listener;

    public void setListener(IRecyclerItemClickListener listener) {
        this.listener = listener;
    }

    public FrameViewHolder(View itemView) {
        super(itemView);

        img_check = (ImageView)itemView.findViewById(R.id.img_check);
        img_frame = (ImageView)itemView.findViewById(R.id.img_frame);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        listener.onClick(v , getAdapterPosition());
    }
}
