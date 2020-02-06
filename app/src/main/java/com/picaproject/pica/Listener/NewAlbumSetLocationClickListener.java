package com.picaproject.pica.Listener;

import android.view.View;

import com.picaproject.pica.CustomView.UploadPicController;
import com.picaproject.pica.Item.UploadPicData;

public class NewAlbumSetLocationClickListener implements View.OnClickListener {
    private UploadPicController controller;
    private UploadPicData data;

    public NewAlbumSetLocationClickListener(UploadPicController controller, UploadPicData data) {
        this.controller = controller;
        this.data = data;
    }

    @Override
    public void onClick(View view) {
        controller.openLoactionView(data);
    }
}
