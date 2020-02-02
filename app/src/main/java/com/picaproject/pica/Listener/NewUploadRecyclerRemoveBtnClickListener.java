package com.picaproject.pica.Listener;

import android.view.View;

import com.picaproject.pica.CustomView.UploadPicController;

public class NewUploadRecyclerRemoveBtnClickListener implements View.OnClickListener {
    private UploadPicController controller;
    private int idx;

    public NewUploadRecyclerRemoveBtnClickListener(UploadPicController controller, int idx) {
        this.controller = controller;
        this.idx = idx;
    }

    @Override
    public void onClick(View view) {
        controller.removePic(idx);
    }
}
