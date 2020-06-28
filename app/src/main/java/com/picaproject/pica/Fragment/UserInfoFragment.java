package com.picaproject.pica.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.mhwan.profileiconview.ProfileIconView;
import com.picaproject.pica.Activity.FriendListActivity;
import com.picaproject.pica.Activity.NotificationListActivity;
import com.picaproject.pica.CustomView.ToggledTextView;
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.Listener.PicImageViewClickListener;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.NetworkItems.ImageResultItem;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 *
 * 시작하면서 받아와야할것
 * 내 프로필 사진, 알림 갯수, 내가 올린사진, 내가좋아요한 사진 리스
 */
public class UserInfoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View view;
    private static RequestManager glide;
    private ImageGridFragment imageListFragment;
    private ImageView profileView;
    private TextView badgeText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProfileIconView profileIconView;
    private ToggledTextView[] toggledTextViews;
    private int toggledIndex = 0;
    public UserInfoFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_user_info, container, false);
        glide = Glide.with(getActivity());
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

        toggledTextViews = new ToggledTextView[2];
        toggledTextViews[0] = view.findViewById(R.id.btn_my_upload_pic);
        toggledTextViews[1] = view.findViewById(R.id.btn_my_favorite_pic);

        for (int i = 0; i< 2; i++) {
            toggledTextViews[i].setTag(i);
            toggledTextViews[i].setOnClickListener(this);
        }

        toggledIndex = 0;
        toggledTextViews[toggledIndex].setChecked(true);
        toggledTextViews[1].setChecked(false);


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
        imageListFragment = (ImageGridFragment) ImageGridFragment.newInstance(false, makeImageSampleList());
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


    private ArrayList<ImageResultItem> makeImageSampleList(){


        ArrayList<ImageResultItem> imageItems = new ArrayList<>();

        int randomSize = (int) (Math.random()*20);


        for (int i = 0; i< randomSize+5; i++) {
            ImageResultItem data = new ImageResultItem();
            data.setFile("https://picsum.photos/200");
            data.setContents("여기 케이크 진짜 맛있었는데 크림이 사르르");
            imageItems.add(data);

        }

        Log.d("random size", imageItems.size()+"");
        return imageItems;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IntentProtocol.GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null){
            profileIconView.setVisibility(View.GONE);
            profileView.setVisibility(View.VISIBLE);
            Uri selectedImage = data.getData();
            glide.load(selectedImage)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileView);
        }
    }

    //새로고침 여기서 데이터를 업데이트 해주는 거고
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        int index = (int)v.getTag();

        if (index != toggledIndex) {
            toggledTextViews[index].setChecked(true);
            toggledTextViews[toggledIndex].setChecked(false);

            toggledIndex = index;

            //여기서는 그냥 데이터만 바꿔주도록 하자
            if (toggledIndex == 0) {
                //올린사진
                Log.d("toggled", "my upload");
                imageListFragment.resetImageList(makeImageSampleList());
            } else {
                //좋아요한 사진

                Log.d("toggled", "my favorite");
                imageListFragment.resetImageList(makeImageSampleList());
            }
        }
    }
}
