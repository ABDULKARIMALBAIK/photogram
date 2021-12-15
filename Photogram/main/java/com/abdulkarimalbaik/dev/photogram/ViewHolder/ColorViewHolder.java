package com.abdulkarimalbaik.dev.photogram.ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.abdulkarimalbaik.dev.photogram.Interface.ColorAdapterListener;
import com.abdulkarimalbaik.dev.photogram.Interface.IRecyclerItemClickListener;
import com.abdulkarimalbaik.dev.photogram.R;

public class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CardView color_section;
    public RelativeLayout color_selected;

    private IRecyclerItemClickListener listener;

    public void setListener(IRecyclerItemClickListener listener) {
        this.listener = listener;
    }

    public ColorViewHolder(View itemView) {
        super(itemView);

        color_section = (CardView)itemView.findViewById(R.id.color_section);
        color_selected = (RelativeLayout)itemView.findViewById(R.id.color_selected);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        listener.onClick(v , getAdapterPosition());
    }
}
