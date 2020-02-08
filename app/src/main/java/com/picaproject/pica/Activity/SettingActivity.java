package com.picaproject.pica.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.picaproject.pica.R;

public class SettingActivity extends BaseToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbarTitle("설정");
    }
}
