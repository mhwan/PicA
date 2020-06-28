package com.picaproject.pica.CustomView;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.picaproject.pica.Activity.ImageDetailActivity;
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.NetworkItems.ImageResultItem;

import java.util.ArrayList;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.Viewholder>{

    private Context context;
    private ArrayList<ImageResultItem> imgList;


    public ImageRecyclerAdapter(Context context, ArrayList<ImageResultItem> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    @Override
    public ImageRecyclerAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image, parent, false);
        Viewholder viewholder = new Viewholder(layoutView);
        return viewholder;
    }


    public void resetImageList(ArrayList<ImageResultItem> imageItems){
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        ImageResultItem item = imgList.get(position);
        //Glide.with(context).load(item.getSampleImg()).override(400).into(holder.imageView);

            Glide.with(context).load(item.getFile())
                    .error(R.drawable.img_sample)
                    .override(200)
                    .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }
    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public Viewholder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            Intent intent = new Intent(context, ImageDetailActivity.class);
            intent.putExtra(IntentProtocol.INTENT_START_POSITION, i);
            intent.putExtra(IntentProtocol.INTENT_ALBUM_IMAGE_LIST, imgList);
            context.startActivity(intent);
        }
    }
}
