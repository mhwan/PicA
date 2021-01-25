package com.picaproject.pica.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.picaproject.pica.R;
import com.picaproject.pica.Util.AcountPreference;
import com.picaproject.pica.Util.AppUtility;

public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private final int SPLASH_DELAY_TIME = 1400;
    private View decorView;
    private int uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        setContentView(R.layout.activity_splash);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateScreen();
            }
        }, SPLASH_DELAY_TIME);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    private void navigateScreen() {
        //preference에 저장된 쿠키값이 있을때
        Intent intent;
        AcountPreference preference = new AcountPreference(getApplicationContext());
        int ids = preference.getId();
        if (ids >= 0) {
            AppUtility.getAppinstance().setMemberId(ids);
            intent = new Intent(getApplicationContext(), MainActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), MemberRegisterActivity.class);

        }
        startActivity(intent);
        finish();
        /*
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();*/
    }
}