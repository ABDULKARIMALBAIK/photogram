package com.abdulkarimalbaik.dev.photogram.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulkarimalbaik.dev.photogram.Fragments.FiltersListFragment;
import com.abdulkarimalbaik.dev.photogram.Interface.FiltersListFragmentListener;
import com.abdulkarimalbaik.dev.photogram.R;
import com.abdulkarimalbaik.dev.photogram.ViewHolder.ThumbnailViewHolder;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailViewHolder> {

    private Context context;
    private List<ThumbnailItem> thumbnailItems;
    private FiltersListFragmentListener listener;
    private int selectedIndex = 0;

    public ThumbnailAdapter(Context context, List<ThumbnailItem> thumbnailItems, FiltersListFragmentListener listener) {
        this.context = context;
        this.thumbnailItems = thumbnailItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.thumbnail , parent , false);
        return new ThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailViewHolder holder, final int position) {

        final ThumbnailItem thumbnailItem = thumbnailItems.get(position);

        holder.thumbnail.setImageBitmap(thumbnailItem.image);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onFilterSelected(thumbnailItem.filter);
                selectedIndex = position;
                notifyDataSetChanged();
            }
        });
        holder.filter_name.setText(thumbnailItem.filterName);

        if (selectedIndex == position)
            holder.filter_name.setTextColor(ContextCompat.getColor(context , R.color.selected_filter));
        else
            holder.filter_name.setTextColor(ContextCompat.getColor(context , R.color.normal_filter));
    }

    @Override
    public int getItemCount() {
        return thumbnailItems.size();
    }
}
