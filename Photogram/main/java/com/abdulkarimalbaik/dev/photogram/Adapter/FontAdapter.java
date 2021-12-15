package com.abdulkarimalbaik.dev.photogram.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulkarimalbaik.dev.photogram.Interface.FontAdapterClickListener;
import com.abdulkarimalbaik.dev.photogram.Interface.IRecyclerItemClickListener;
import com.abdulkarimalbaik.dev.photogram.R;
import com.abdulkarimalbaik.dev.photogram.Utils.Common;
import com.abdulkarimalbaik.dev.photogram.ViewHolder.FontVIewHolder;

import java.util.ArrayList;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontVIewHolder> {

    Context context;
    FontAdapterClickListener listener;
    List<String> fontList;

    public FontAdapter(Context context, FontAdapterClickListener listener) {
        this.context = context;
        this.listener = listener;
        fontList = getFontList();
    }

    private List<String> getFontList() {

        List<String> result = new ArrayList<>();
        result.add("sumi.otf");
        result.add("nabila.TTF");
        result.add("mali-light.ttf");
        result.add("mali-italic.ttf");
        result.add("indie_flower.ttf");

        return result;
    }

    @NonNull
    @Override
    public FontVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.font_item , parent , false);
        return new FontVIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FontVIewHolder holder, int position) {

        if (Common.row_selected == position)
            holder.img_check.setVisibility(View.VISIBLE);
        else
            holder.img_check.setVisibility(View.INVISIBLE);

        holder.txt_font_name.setText(fontList.get(position).substring(0 , fontList.get(position).length() - 4));

        Typeface typeface = Typeface.createFromAsset(context.getAssets() , new StringBuilder("fonts/")
        .append(fontList.get(position)).toString());

        holder.txt_font_demo.setTypeface(typeface);

        holder.setListener(new IRecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                listener.onFontSelected(fontList.get(position));
                Common.row_selected = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }
}
