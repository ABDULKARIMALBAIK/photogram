package com.abdulkarimalbaik.dev.photogram.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulkarimalbaik.dev.photogram.Interface.EmojiAdapterListener;
import com.abdulkarimalbaik.dev.photogram.Interface.IRecyclerItemClickListener;
import com.abdulkarimalbaik.dev.photogram.R;
import com.abdulkarimalbaik.dev.photogram.ViewHolder.EmojiViewHolder;

import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiViewHolder> {

    Context context;
    List<String> emojiList;
    EmojiAdapterListener listener;

    public EmojiAdapter(Context context, List<String> emojiList, EmojiAdapterListener listener) {
        this.context = context;
        this.emojiList = emojiList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.emoji_item , parent , false);
        return new EmojiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {

        holder.emojiconTextView.setText(emojiList.get(position));
        holder.setListener(new IRecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                listener.onEmojiItemSelected(emojiList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }
}
