package com.abdulkarimalbaik.dev.photogram.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulkarimalbaik.dev.photogram.Interface.ColorAdapterListener;
import com.abdulkarimalbaik.dev.photogram.Interface.IRecyclerItemClickListener;
import com.abdulkarimalbaik.dev.photogram.R;
import com.abdulkarimalbaik.dev.photogram.Utils.Common;
import com.abdulkarimalbaik.dev.photogram.ViewHolder.ColorViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorViewHolder> {

    Context context;
    List<Integer> colorList;
    ColorAdapterListener listener;

    public ColorAdapter(Context context , ColorAdapterListener listener) {
        this.context = context;
        this.colorList = getColorList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.color_item , parent , false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ColorViewHolder holder, final int position) {

        holder.color_section.setBackgroundColor(colorList.get(position));
        holder.setListener(new IRecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                Common.color_selected = position;
                listener.onColorSelected(colorList.get(position));
                notifyDataSetChanged();
            }
        });

        if (Common.color_selected == position)
            holder.color_selected.setVisibility(View.VISIBLE);
        else
            holder.color_selected.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }


    private List<Integer> getColorList() {

        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.parseColor("#ed1a26"));
        colorList.add(Color.parseColor("#20409a"));
        colorList.add(Color.parseColor("#420666"));
        colorList.add(Color.parseColor("#a8251d"));
        colorList.add(Color.parseColor("#870000"));
        colorList.add(Color.parseColor("#511d1f"));
        colorList.add(Color.parseColor("#030303"));
        colorList.add(Color.parseColor("#5a4862"));
        colorList.add(Color.parseColor("#656a6d"));
        colorList.add(Color.parseColor("#7a7a7a"));
        colorList.add(Color.parseColor("#9ddba1"));
        colorList.add(Color.parseColor("#2c7575"));
        colorList.add(Color.parseColor("#ecd3a3"));
        colorList.add(Color.parseColor("#838300"));
        colorList.add(Color.parseColor("#787832"));
        colorList.add(Color.parseColor("#99424f"));
        colorList.add(Color.parseColor("#9f5561"));
        colorList.add(Color.parseColor("#2b2b2b"));
        colorList.add(Color.parseColor("#5c5e76"));
        colorList.add(Color.parseColor("#535e76"));
        colorList.add(Color.parseColor("#5c5e37"));
        colorList.add(Color.parseColor("#553355"));
        colorList.add(Color.parseColor("#938fad"));
        colorList.add(Color.parseColor("#d3dbdc"));
        colorList.add(Color.parseColor("#d3db3c"));
        colorList.add(Color.parseColor("#000666"));

        return colorList;
    }
}
