package com.abdulkarimalbaik.dev.photogram.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.EditText;
import android.widget.Toast;

import com.abdulkarimalbaik.dev.photogram.Adapter.ColorAdapter;
import com.abdulkarimalbaik.dev.photogram.Adapter.FontAdapter;
import com.abdulkarimalbaik.dev.photogram.Interface.AddTextFragmentListener;
import com.abdulkarimalbaik.dev.photogram.Interface.ColorAdapterListener;
import com.abdulkarimalbaik.dev.photogram.Interface.FontAdapterClickListener;
import com.abdulkarimalbaik.dev.photogram.R;

public class AddTextFragment extends BottomSheetDialogFragment implements ColorAdapterListener, FontAdapterClickListener {

    int colorSelected = Color.parseColor("#000000"); //Default color of text is black

    AddTextFragmentListener listener;

    EditText edt_add_text;
    RecyclerView recycler_color , recycler_font;
    Button btn_done;

    ColorAdapter colorAdapter;
    public static AddTextFragment instance;

    Typeface typefaceSelected = Typeface.DEFAULT;

    public AddTextFragment() {}

    public void setListener(AddTextFragmentListener listener) {
        this.listener = listener;
    }

    public static AddTextFragment getInstance() {

        if (instance == null)
            instance = new AddTextFragment();
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
        View view =  inflater.inflate(R.layout.fragment_add_text, container, false);

        edt_add_text = (EditText)view.findViewById(R.id.edt_add_text);
        btn_done = (Button)view.findViewById(R.id.btn_done);
        recycler_color = (RecyclerView)view.findViewById(R.id.recycler_color);
        recycler_font = (RecyclerView)view.findViewById(R.id.recycler_font);

        Typeface typeface = Typeface.DEFAULT;
        edt_add_text.setTypeface(typeface);

        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false));
        colorAdapter = new ColorAdapter(getContext() , this);
        recycler_color.setAdapter(colorAdapter);

        recycler_font.setHasFixedSize(true);
        recycler_font.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false));
        FontAdapter fontAdapter = new FontAdapter(getContext() , this);
        recycler_font.setAdapter(fontAdapter);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_add_text.getText().toString().isEmpty())
                    Toast.makeText(getActivity(), "Please enter your text !", Toast.LENGTH_SHORT).show();
                else
                    listener.onAddTextButtonClick(typefaceSelected , edt_add_text.getText().toString() , colorSelected);
            }
        });

        return view;
    }

    @Override
    public void onColorSelected(int color) {

        colorSelected = color;  //Set color when user select
    }

    @Override
    public void onFontSelected(String FontName) {

        typefaceSelected = Typeface.createFromAsset(getContext().getAssets() , new StringBuilder("fonts/")
                .append(FontName).toString());
    }
}
