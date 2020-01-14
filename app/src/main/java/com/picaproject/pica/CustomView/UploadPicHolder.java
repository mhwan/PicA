package com.picaproject.pica.CustomView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.picaproject.pica.R;

public class UploadPicHolder extends RecyclerView.ViewHolder {

    private PicMetaTextView metaTextView;
    private ImageView imageView;

    public UploadPicHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.picView);
        this.metaTextView = (PicMetaTextView) itemView.findViewById(R.id.picMetaText);
    }

    public PicMetaTextView getMetaTextView() {
        return metaTextView;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
