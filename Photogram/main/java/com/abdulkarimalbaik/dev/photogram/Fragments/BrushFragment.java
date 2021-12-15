package com.abdulkarimalbaik.dev.photogram.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.abdulkarimalbaik.dev.photogram.Adapter.ColorAdapter;
import com.abdulkarimalbaik.dev.photogram.Interface.BrushFragmentListener;
import com.abdulkarimalbaik.dev.photogram.Interface.ColorAdapterListener;
import com.abdulkarimalbaik.dev.photogram.R;
import com.abdulkarimalbaik.dev.photogram.Utils.Common;

import java.util.ArrayList;
import java.util.List;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class BrushFragment extends BottomSheetDialogFragment implements ColorAdapterListener{

    SeekBar seekBar_brush_size , seekBar_brush_opacity;
    RecyclerView recycler_color;
    SegmentedButtonGroup segmentedButtonGroup;
    ColorAdapter colorAdapter;

    BrushFragmentListener listener;
    public static BrushFragment instance;

    public BrushFragment() {
    }

    public static BrushFragment getInstance() {

        if (instance == null)
            instance = new BrushFragment();
        return instance;
    }

    public void setBrushFragmentListener(BrushFragmentListener brushFragmentListener) {
        this.listener = brushFragmentListener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_brush, container, false);

        seekBar_brush_size = (SeekBar)view.findViewById(R.id.seekBar_brush_size);
        seekBar_brush_opacity = (SeekBar)view.findViewById(R.id.seekBar_brush_opacity);
        segmentedButtonGroup = (SegmentedButtonGroup)view.findViewById(R.id.segmentedButtonGroup);
        recycler_color = (RecyclerView)view.findViewById(R.id.recycler_color);

        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false));
        colorAdapter = new ColorAdapter(getContext() , this);
        recycler_color.setAdapter(colorAdapter);

        seekBar_brush_size.setProgress(25);
        seekBar_brush_size.setMax(100);
        seekBar_brush_opacity.setProgress(100);
        seekBar_brush_opacity.setMax(100);

        if (Common.segmentedButtonState)
            segmentedButtonGroup.setPosition(0,1);
        else
            segmentedButtonGroup.setPosition(1,1);

        //Event
        seekBar_brush_opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                listener.onBrushOpacityChangedListener(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar_brush_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                listener.onBrushSizeChangedListener(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        segmentedButtonGroup.setOnClickedButtonPosition(new SegmentedButtonGroup.OnClickedButtonPosition() {
            @Override
            public void onClickedButtonPosition(int position) {

                if (position == 0){

                    listener.onBrushStateChangedListener(true);
                    Common.segmentedButtonState = true;
                }

                if (position == 1){

                    listener.onBrushStateChangedListener(false);
                    Common.segmentedButtonState = false;
                }

            }
        });

        return view;
    }



    @Override
    public void onColorSelected(int color) {
        listener.onBrushColorChangedListener(color);
    }
}
