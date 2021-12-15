package com.abdulkarimalbaik.dev.photogram.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulkarimalbaik.dev.photogram.Interface.FrameAdapterListener;
import com.abdulkarimalbaik.dev.photogram.Interface.IRecyclerItemClickListener;
import com.abdulkarimalbaik.dev.photogram.R;
import com.abdulkarimalbaik.dev.photogram.Utils.Common;
import com.abdulkarimalbaik.dev.photogram.ViewHolder.FrameViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FrameAdapter extends RecyclerView.Adapter<FrameViewHolder> {

    Context context;
    List<Integer> frameList;
    FrameAdapterListener listener;

    public FrameAdapter(Context context, FrameAdapterListener listener) {
        this.context = context;
        this.frameList = getFramesList();
        this.listener = listener;
    }

    private List<Integer> getFramesList() {

        List<Integer> result = new ArrayList<>();
        result.add(R.drawable.card_1_resize);
        result.add(R.drawable.card_2_resize);
        result.add(R.drawable.card_3_resize);
        result.add(R.drawable.card_4_resize);
        result.add(R.drawable.card_5_resize);
        result.add(R.drawable.card_6_resize);
        result.add(R.drawable.card_7_resize);
        result.add(R.drawable.card_8_resize);
        result.add(R.drawable.card_9_resize);
        result.add(R.drawable.card_10_resize);

        return result;
    }

    @NonNull
    @Override
    public FrameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.frame_item , parent , false);
        return new FrameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FrameViewHolder holder, int position) {

        if (Common.frame_selected == position)
            holder.img_check.setVisibility(View.VISIBLE);
        else
            holder.img_check.setVisibility(View.INVISIBLE);

        holder.img_frame.setImageResource(frameList.get(position));

        holder.setListener(new IRecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                listener.onFrameSelected(frameList.get(position));
                Common.frame_selected = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return frameList.size();
    }
}
