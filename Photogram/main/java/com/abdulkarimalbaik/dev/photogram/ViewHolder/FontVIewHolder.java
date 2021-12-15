package com.abdulkarimalbaik.dev.photogram.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulkarimalbaik.dev.photogram.Interface.IRecyclerItemClickListener;
import com.abdulkarimalbaik.dev.photogram.R;

public class FontVIewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_font_demo , txt_font_name;
    public ImageView img_check;

    private IRecyclerItemClickListener listener;

    public void setListener(IRecyclerItemClickListener listener) {
        this.listener = listener;
    }

    public FontVIewHolder(View itemView) {
        super(itemView);

        txt_font_name = (TextView)itemView.findViewById(R.id.txt_font_name);
        txt_font_demo = (TextView)itemView.findViewById(R.id.txt_font_demo);
        img_check = (ImageView)itemView.findViewById(R.id.img_check);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v , getAdapterPosition());
    }
}
