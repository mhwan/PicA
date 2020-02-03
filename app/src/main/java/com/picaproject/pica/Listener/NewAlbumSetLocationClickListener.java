package com.picaproject.pica.Listener;

import android.view.View;

import com.picaproject.pica.CustomView.UploadPicController;

public class NewAlbumSetLocationClickListener implements View.OnClickListener {
    private UploadPicController controller;
    private long dataId;

    public NewAlbumSetLocationClickListener(UploadPicController controller, long dataId) {
        this.controller = controller;
        this.dataId = dataId;
    }

    @Override
    public void onClick(View view) {
        controller.openLoactionView(dataId);
    }
}
