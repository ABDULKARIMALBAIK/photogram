package com.abdulkarimalbaik.dev.photogram.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.abdulkarimalbaik.dev.photogram.Interface.IRecyclerItemClickListener;
import com.abdulkarimalbaik.dev.photogram.R;

import io.github.rockerhieu.emojicon.EmojiconTextView;

public class EmojiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public EmojiconTextView emojiconTextView;

    private IRecyclerItemClickListener listener;

    public void setListener(IRecyclerItemClickListener listener) {
        this.listener = listener;
    }

    public EmojiViewHolder(View itemView) {
        super(itemView);

        emojiconTextView = (EmojiconTextView)itemView.findViewById(R.id.emoji_text_view);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v , getAdapterPosition());
    }
}
