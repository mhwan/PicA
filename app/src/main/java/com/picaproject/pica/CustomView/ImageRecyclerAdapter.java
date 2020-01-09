package com.picaproject.pica.CustomView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.picaproject.pica.Item.ImageItem;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.Viewholder>{

    private Context context;
    private ArrayList<ImageItem> imgList;


    public ImageRecyclerAdapter(Context context, ArrayList<ImageItem> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    @Override
    public ImageRecyclerAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image, parent, false);
        Viewholder viewholder = new Viewholder(layoutView);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        holder.imageView.setImageResource(imgList.get(position).getImage_id());
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }
    class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public Viewholder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
