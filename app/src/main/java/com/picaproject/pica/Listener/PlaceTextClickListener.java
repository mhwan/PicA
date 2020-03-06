package com.picaproject.pica.Listener;

import android.util.Log;
import android.view.View;

import com.picaproject.pica.Activity.LocationListActivity;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.PicPlaceDataWrapper;

public class PlaceTextClickListener implements View.OnClickListener {
    private LocationListActivity.ActivityCallBack activityCallBack;
    private PicPlaceDataWrapper p;

    public PlaceTextClickListener(LocationListActivity.ActivityCallBack activityCallBack, PicPlaceDataWrapper p) {
        this.activityCallBack = activityCallBack;
        this.p = p;
    }

    @Override
    public void onClick(View view) {
        Log.i("test_hs","PlaceTextClickListener ");
        activityCallBack.setLocationFromList(p);
    }
}
