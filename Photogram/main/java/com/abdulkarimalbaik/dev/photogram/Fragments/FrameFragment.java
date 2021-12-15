package com.abdulkarimalbaik.dev.photogram.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.abdulkarimalbaik.dev.photogram.Adapter.FrameAdapter;
import com.abdulkarimalbaik.dev.photogram.Interface.AddFrameListener;
import com.abdulkarimalbaik.dev.photogram.Interface.FrameAdapterListener;
import com.abdulkarimalbaik.dev.photogram.R;
import com.abdulkarimalbaik.dev.photogram.Utils.Common;

public class FrameFragment extends BottomSheetDialogFragment implements FrameAdapterListener {

    public static FrameFragment instance;

    RecyclerView recycler_frame;
    Button btn_add_frame;
    AddFrameListener listener;

    int frame_selected_fragment = -1;

    public FrameFragment() {
    }

    public void setListener(AddFrameListener listener) {
        this.listener = listener;
    }

    public static FrameFragment getInstance(){

        if (instance == null)
            instance = new FrameFragment();

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_frame, container, false);

        recycler_frame = (RecyclerView)view.findViewById(R.id.recycler_frame);
        btn_add_frame = (Button)view.findViewById(R.id.btn_add_frame);

        recycler_frame.setHasFixedSize(true);
        recycler_frame.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false));
        recycler_frame.setAdapter(new FrameAdapter(getContext() , this));

        btn_add_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.frame_selected > -1)
                    listener.onAddFrame(frame_selected_fragment);
                else
                    Toast.makeText(getActivity(), "Please select a frame first !", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onFrameSelected(int frame) {

        frame_selected_fragment = frame;
    }
}
