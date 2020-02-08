package com.picaproject.pica.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.picaproject.pica.R;

public class FriendListActivity extends BaseToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbarTitle("내 친구");
    }


}
