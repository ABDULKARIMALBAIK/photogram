package com.abdulkarimalbaik.dev.photogram.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulkarimalbaik.dev.photogram.Adapter.ThumbnailAdapter;
import com.abdulkarimalbaik.dev.photogram.Interface.FiltersListFragmentListener;
import com.abdulkarimalbaik.dev.photogram.MainActivity;
import com.abdulkarimalbaik.dev.photogram.R;
import com.abdulkarimalbaik.dev.photogram.Utils.BitmapUtils;
import com.abdulkarimalbaik.dev.photogram.Utils.SpacesItemDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;


public class FiltersListFragment extends BottomSheetDialogFragment implements FiltersListFragmentListener{

    public RecyclerView recyclerView;
    public List<ThumbnailItem> thumbnailItems = new ArrayList<>();
    ThumbnailAdapter adapter;
    FiltersListFragmentListener listener;

    public static FiltersListFragment instance;
    public static Bitmap bitmap;
    public static Activity activityy;

    public FiltersListFragment() {
        // Required empty public constructor
    }

    public void setListener(FiltersListFragmentListener listener) {
        this.listener = listener;
    }

    public static FiltersListFragment getInstance(Bitmap bitmapSave , Activity activity){

        bitmap = bitmapSave;
        activityy = activity;

        if (instance == null){

            instance = new FiltersListFragment();
        }

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
        View view = inflater.inflate(R.layout.fragment_filters_list, container, false);

        thumbnailItems  = new ArrayList<>();

        displayThumbnail(bitmap);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , 8 , getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        adapter = new ThumbnailAdapter(activityy , thumbnailItems , this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void displayThumbnail(final Bitmap bitmap) {

        Bitmap thumbImg;
        if (bitmap == null){
            thumbImg = BitmapUtils.getBitmapFromAssets(activityy , MainActivity.pictureName , 100 , 100);
        }
        else
            thumbImg = Bitmap.createScaledBitmap(bitmap ,100 , 100 , false);

        if (thumbImg == null)
            return;

        ThumbnailsManager.clearThumbs();
        thumbnailItems.clear();

        //add normal bitmap first
        ThumbnailItem item = new ThumbnailItem();
        item.image = thumbImg;
        item.filterName = "Normal";
        ThumbnailsManager.addThumb(item);

        List<Filter> filters = FilterPack.getFilterPack(activityy);
        for(Filter filter : filters){

            ThumbnailItem tI = new ThumbnailItem();
            tI.image = thumbImg;
            tI.filter = filter;
            tI.filterName = filter.getName();
            ThumbnailsManager.addThumb(tI);
        }

        thumbnailItems.addAll(ThumbnailsManager.processThumbs(activityy));
    }

    @Override
    public void onFilterSelected(Filter filter) {

        if (listener != null)
            listener.onFilterSelected(filter);
    }
}
