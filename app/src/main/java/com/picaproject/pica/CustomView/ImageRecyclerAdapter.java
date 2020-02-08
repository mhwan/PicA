package com.picaproject.pica.CustomView;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.picaproject.pica.Activity.ImageDetailActivity;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.ImageItem;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.Viewholder>{

    private Context context;
    private ArrayList<UploadPicData> imgList;


    public ImageRecyclerAdapter(Context context, ArrayList<UploadPicData> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    @Override
    public ImageRecyclerAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image, parent, false);
        Viewholder viewholder = new Viewholder(layoutView);
        return viewholder;
    }


    public void resetImageList(ArrayList<UploadPicData> imageItems){
        imgList.clear();
        imgList.addAll(imageItems);
        notifyAll();
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        holder.imageView.setImageResource(imgList.get(position).getImgSampleId());
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
