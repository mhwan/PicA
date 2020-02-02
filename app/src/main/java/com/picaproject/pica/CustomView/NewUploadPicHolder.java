package com.picaproject.pica.CustomView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.picaproject.pica.R;

public class NewUploadPicHolder extends RecyclerView.ViewHolder {

    private RelativeLayout removeBtn;
    private ImageView imageView;
    private View border;

    public NewUploadPicHolder(@NonNull View itemView) {
        super(itemView);
        this.border = itemView.findViewById(R.id.pic_select_border);
        this.imageView = (ImageView) itemView.findViewById(R.id.picView);
        this.removeBtn = (RelativeLayout) itemView.findViewById(R.id.remove_btn);
    }

    public RelativeLayout getRemoveBtn() {
        return removeBtn;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public View getBorderView() {
        return border;
    }
}
