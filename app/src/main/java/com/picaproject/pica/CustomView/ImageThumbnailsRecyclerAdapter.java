package com.picaproject.pica.CustomView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.picaproject.pica.Item.ThumbnailItem;
import com.picaproject.pica.Listener.ThumbnailCallbackListener;
import com.picaproject.pica.R;

import java.util.List;

public class ImageThumbnailsRecyclerAdapter extends RecyclerView.Adapter<ImageThumbnailsRecyclerAdapter.ViewHolder> {
    private Context context;
    private static int lastPosition = 0;
    private ThumbnailCallbackListener thumbnailCallback;
    private List<ThumbnailItem> dataSet;

    public ImageThumbnailsRecyclerAdapter(Context context, List<ThumbnailItem> list, ThumbnailCallbackListener listener) {
        this.thumbnailCallback = listener;
        this.dataSet = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_filter_thumnail, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThumbnailItem item = dataSet.get(position);
        Glide.with(context).load(item.getImage())
                .apply(RequestOptions.circleCropTransform())
                .error(R.drawable.img_sample)
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public ThumbnailItem getSelectItem() {
        return dataSet.get(lastPosition);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnail;
        public ViewHolder(View item) {
            super(item);
            thumbnail = (ImageView) item.findViewById(R.id.thumbnail);
            thumbnail.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            int idx = getAdapterPosition();
            if (lastPosition != idx) {
                lastPosition = idx;
                thumbnailCallback.onThumbnailClick(dataSet.get(idx).getImage());
            }
        }
    }
}
