package com.abdulkarimalbaik.dev.photogram.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.abdulkarimalbaik.dev.photogram.Interface.EditImageFragmentListener;
import com.abdulkarimalbaik.dev.photogram.R;

public class EditImageFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener{

    private EditImageFragmentListener listener;
    public SeekBar seekBar_brightness , seekBar_constraint , seekBar_saturation;
    public static EditImageFragment instance;

    public EditImageFragment() {
        // Required empty public constructor
    }

    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }

    public static EditImageFragment getInstance(){

        if (instance == null)
            instance = new EditImageFragment();

        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_image , container , false);

        seekBar_brightness = (SeekBar)view.findViewById(R.id.seekBar_brightness);
        seekBar_constraint = (SeekBar)view.findViewById(R.id.seekBar_constraint);
        seekBar_saturation = (SeekBar)view.findViewById(R.id.seekBar_saturation);

        seekBar_brightness.setMax(200);
        seekBar_brightness.setProgress(100);

        seekBar_constraint.setMax(200);
        seekBar_constraint.setProgress(100);

        seekBar_saturation.setMax(200);
        seekBar_saturation.setProgress(100);

        seekBar_brightness.setOnSeekBarChangeListener(this);
        seekBar_constraint.setOnSeekBarChangeListener(this);
        seekBar_saturation.setOnSeekBarChangeListener(this);

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (listener != null){
            if (seekBar.getId() == R.id.seekBar_brightness){

                listener.onBrightnessChanged(progress - 100);
            }
            else if (seekBar.getId() == R.id.seekBar_constraint){

                progress += 10;
                float value = .10f * progress;
                listener.onConstraintChanged(value);
            }
            else if (seekBar.getId() == R.id.seekBar_saturation){

                float value = .10f * progress;
                listener.onSaturationCanged(value);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        if (listener != null)
            listener.onEditStarted();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (listener != null)
            listener.onEditComplete();
    }

    public void resetControls(){

        seekBar_brightness.setProgress(100);
        seekBar_constraint.setProgress(0);
        seekBar_saturation.setProgress(10);
    }
}
