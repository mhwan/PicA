package com.picaproject.pica.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.picaproject.pica.Activity.FriendListActivity;
import com.picaproject.pica.Activity.MainActivity;
import com.picaproject.pica.Activity.NotificationListActivity;
import com.picaproject.pica.Activity.SettingActivity;
import com.picaproject.pica.CustomView.ProfileIconView;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Listener.PicImageViewClickListener;
import com.picaproject.pica.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private  static RequestManager glide;
    private ImageGridFragment imageListFragment;
    private ImageView profileView;
    private TextView badgeText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProfileIconView profileIconView;
    public UserInfoFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_user_info, container, false);
        glide = Glide.with(this);
        initView();
        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void initView(){
        initFragment();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.user_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        profileView = (ImageView) view.findViewById(R.id.btn_profile);
        profileIconView = (ProfileIconView) view.findViewById(R.id.default_profile);

        view.findViewById(R.id.profile_frame).setOnClickListener(new PicImageViewClickListener(UserInfoFragment.this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profileView.setBackground(new ShapeDrawable(new OvalShape()));
            profileView.setClipToOutline(true);
        }

        badgeText = (TextView) view.findViewById(R.id.badge_text);
        view.findViewById(R.id.btn_friend_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FriendListActivity.class));
            }
        });
        view.findViewById(R.id.btn_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NotificationListActivity.class));
            }
        });
    }

    private void initFragment(){
        imageListFragment = (ImageGridFragment) ImageGridFragment.newInstance(false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.frag_image_list, imageListFragment);
        transaction.commit();
    }

    private void setBadgeString(int val){
        if (val > 99)
            badgeText.setText("99+");
        else if (val <= 0){
            badgeText.setText("");
            badgeText.setVisibility(View.GONE);
        } else
            badgeText.setText(val+"");
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IntentProtocol.GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null){
            profileIconView.setVisibility(View.GONE);
            profileView.setVisibility(View.VISIBLE);
            Uri selectedImage = data.getData();
            glide.load(selectedImage).into(profileView);
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
