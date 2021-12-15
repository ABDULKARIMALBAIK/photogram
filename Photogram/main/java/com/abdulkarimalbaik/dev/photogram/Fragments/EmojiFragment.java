package com.abdulkarimalbaik.dev.photogram.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulkarimalbaik.dev.photogram.Adapter.EmojiAdapter;
import com.abdulkarimalbaik.dev.photogram.Interface.EmojiAdapterListener;
import com.abdulkarimalbaik.dev.photogram.Interface.EmojiFragmentListener;
import com.abdulkarimalbaik.dev.photogram.R;

import ja.burhanrashid52.photoeditor.PhotoEditor;

public class EmojiFragment extends BottomSheetDialogFragment implements EmojiAdapterListener {

    RecyclerView recycler_emoji;
    public static EmojiFragment instance;

    EmojiFragmentListener listener;

    public void setListener(EmojiFragmentListener listener) {
        this.listener = listener;
    }

    public EmojiFragment() {
    }



    public static EmojiFragment getInstance(){

        if (instance == null)
            instance = new EmojiFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_emoji, container, false);

        recycler_emoji = (RecyclerView)view.findViewById(R.id.recycler_emoji);
        recycler_emoji.setHasFixedSize(true);
        recycler_emoji.setLayoutManager(new GridLayoutManager(getActivity() , 5));

        EmojiAdapter adapter = new EmojiAdapter(getContext() , PhotoEditor.getEmojis(getContext()) , this);
        recycler_emoji.setAdapter(adapter);

        return view;
    }

    @Override
    public void onEmojiItemSelected(String emoji) {

        listener.omEmojiSelected(emoji);
    }

}
