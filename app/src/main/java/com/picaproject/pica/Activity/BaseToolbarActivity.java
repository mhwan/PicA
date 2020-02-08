package com.picaproject.pica.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.picaproject.pica.R;

/**
 * 흰 바탕에 있는 toolbar를 위한 액티비티 (이 액티비티를 상속받아 작성하면됨)
 * 그리고 xml에서 툴바부분을 <include layout="@layout/header_base_toolbar"/> 이렇게 정의
 *
 * 툴바에 관한 모든것은 onstart에서 처리해야함 (oncreate에서 처리하면 에러발생)
 *
 * 툴바 오른쪽의 버튼 정의 setToolbarButton()
 * 툴바 제목정의 setToolbarTitle()
 * 툴바 왼쪽의 네비게이션 아이콘 정의 setToolbarNavigationIcon
 *
 * 사용 예시 CreateAlbumActivity, InviteMemberActivity
 */

public class BaseToolbarActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected int navigationId;
    protected View.OnClickListener buttonListener;
    protected TextView toolbarTitle;
    protected TextView toolbarSubtitle;
    @Override
    protected void onStart() {
        super.onStart();

        setToolbar();
    }

    protected void setToolbarButton(String buttonName, View.OnClickListener listener){
        this.buttonListener = listener;
        toolbar.findViewById(R.id.toolbar_button).setOnClickListener(listener);
        ((TextView)toolbar.findViewById(R.id.toolbar_button)).setText(buttonName);
    }
    protected void setToolbarTitle(String title){
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(title);
    }

    protected void setToolbarSubTitle(String title){
        if (toolbarSubtitle == null)
            toolbarSubtitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
        toolbarSubtitle.setText(title);
    }

    protected void setToolbarNavigationIcon(int navigationId){
        this.navigationId = navigationId;
        ((ImageView)toolbar.findViewById(R.id.toolbar_close)).setImageResource(navigationId);
    }

    protected void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.findViewById(R.id.toolbar_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setVisibility(View.GONE);
    }
}
