package com.picaproject.pica.CustomView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.picaproject.pica.Activity.LocationListActivity;
import com.picaproject.pica.Listener.PlaceTextClickListener;
import com.picaproject.pica.R;

import java.util.List;

import noman.googleplaces.Place;

public class PicLocationListAdapter extends BaseAdapter {

    private List<Place> items;
    private LocationListActivity.ActivityCallBack activityCallBack;

    public PicLocationListAdapter(List<Place> items, LocationListActivity.ActivityCallBack activityCallBack) {
        this.items = items;
        this.activityCallBack = activityCallBack;
    }

    @Override
    public int getCount() {
        return items.size() ;
    }

    @Override
    public Object getItem(int i) {
        return  items.get(i) ;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();


        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_pic_location_list, viewGroup, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        TextView placeText = (TextView) view.findViewById(R.id.location_text) ;





        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Place p = items.get(i);

        placeText.setText(p.getName());
        view.setOnClickListener(new PlaceTextClickListener(activityCallBack,p));

        return view;
    }

}
