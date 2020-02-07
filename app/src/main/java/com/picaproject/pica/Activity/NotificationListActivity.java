package com.picaproject.pica.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.picaproject.pica.CustomView.NewNotificationRecyclerAdapter;
import com.picaproject.pica.Item.NotificationItem;
import com.picaproject.pica.Listener.NotificationRemoveListener;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class NotificationListActivity extends BaseToolbarActivity implements NotificationRemoveListener {

    private RecyclerView newRecyclerview;
    private LinearLayout newNotifiFrame;
    private RecyclerView recyclerView;
    private NewNotificationRecyclerAdapter newAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbarTitle("알림");
    }

    private void initView(){
        newRecyclerview = (RecyclerView) findViewById(R.id.new_alarm_recyclerview);
        newNotifiFrame = findViewById(R.id.new_alarm_frame);
        recyclerView = (RecyclerView) findViewById(R.id.alarm_recyclerview);

        newAdapter = new NewNotificationRecyclerAdapter(this, getSample(5), true, this);
        newRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        newRecyclerview.setAdapter(newAdapter);
        newRecyclerview.setNestedScrollingEnabled(false);

        NewNotificationRecyclerAdapter adapter = new NewNotificationRecyclerAdapter(this, getSample(10), false, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        setNewNotifiFrame(newAdapter.getItemCount());
    }

    private void setNewNotifiFrame(int count){
        if (count == 0)
            newNotifiFrame.setVisibility(View.GONE);
        else
            newNotifiFrame.setVisibility(View.VISIBLE);
    }
    private ArrayList<NotificationItem> getSample(int size){
        ArrayList<NotificationItem> list = new ArrayList<>();
        for (int i=0; i<size; i++)
            list.add(new NotificationItem(i, "앨범 \'샘플앨범1\'에 초대하였습니다. 알람을 클릭해 사진을 친구들과 공유하세요.", "mhwan", "2020.02.06"));

        return list;
    }

    @Override
    public void onRemoved(int count) {
        setNewNotifiFrame(count);
    }
}
