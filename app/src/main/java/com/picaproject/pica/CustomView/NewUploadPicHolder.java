package com.picaproject.pica.CustomView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.picaproject.pica.R;

public class NewUploadPicHolder extends RecyclerView.ViewHolder {

    private Button backBtn;
    private ImageView imageView;

    public NewUploadPicHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.picView);
        this.backBtn = (Button) itemView.findViewById(R.id.remove_btn);
    }

    public Button getBackBtn() {
        return backBtn;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
