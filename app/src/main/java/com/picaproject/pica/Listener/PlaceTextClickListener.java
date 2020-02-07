package com.picaproject.pica.Listener;

import android.util.Log;
import android.view.View;

import com.picaproject.pica.Activity.LocationListActivity;
import com.picaproject.pica.Item.PicPlaceData;

public class PlaceTextClickListener implements View.OnClickListener {
    private LocationListActivity.ActivityCallBack activityCallBack;
    private PicPlaceData p;

    public PlaceTextClickListener(LocationListActivity.ActivityCallBack activityCallBack, PicPlaceData p) {
        this.activityCallBack = activityCallBack;
        this.p = p;
    }

    @Override
    public void onClick(View view) {
        Log.i("test_hs","PlaceTextClickListener ");
        activityCallBack.setLocationFromList(p);
    }
}
