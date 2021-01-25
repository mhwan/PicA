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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.mhwan.profileiconview.ProfileIconView;
import com.picaproject.pica.Activity.FriendListActivity;
import com.picaproject.pica.Activity.NotificationListActivity;
import com.picaproject.pica.CustomView.ToggledTextView;
import com.picaproject.pica.Util.AcountPreference;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.Listener.PicImageViewClickListener;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.NetworkItems.AlbumResultItem;
import com.picaproject.pica.Util.NetworkItems.DefaultResultItem;
import com.picaproject.pica.Util.NetworkItems.ImageResultItem;
import com.picaproject.pica.Util.NetworkItems.ProfileResultItem;
import com.picaproject.pica.Util.NetworkUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private ArrayList<ImageResultItem> myFavoriteList = new ArrayList<>(), myUploadList = new ArrayList<>();
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
        getMyUploadPictureTask();
        getMyFavoritePictureTask();
        getProfileTask();
        initFragment();
        AcountPreference preference = new AcountPreference(getContext());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.user_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        profileView = (ImageView) view.findViewById(R.id.btn_profile);
        profileIconView = (ProfileIconView) view.findViewById(R.id.default_profile);

        view.findViewById(R.id.profile_frame).setOnClickListener(new PicImageViewClickListener(UserInfoFragment.this));
        ((TextView)view.findViewById(R.id.name_profile)).setText(preference.getNickname());
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
        imageListFragment = (ImageGridFragment) ImageGridFragment.newInstance(false, myUploadList);
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


    private void getMyUploadPictureTask(){
        NetworkUtility.getInstance().myUploadPictureList(AppUtility.memberId, new Callback<AlbumResultItem>() {
            @Override
            public void onResponse(Call<AlbumResultItem> call, Response<AlbumResultItem> response) {
                AlbumResultItem albumResultItem = response.body();

                switch (albumResultItem.getCode()) {
                    case NetworkUtility.APIRESULT.RESULT_SUCCESS:
                        myUploadList = new ArrayList<>();
                        if (albumResultItem.getResult() != null && albumResultItem.getResult().size() > 0) {
                            for (int i = 0; i < albumResultItem.getResult().size(); i++) {
                                myUploadList.add(albumResultItem.getResult().get(i).getResult());
                                myUploadList.get(i).setArrayIndex(i);
                            }
                            updateImageListView();
                        }

                        break;
                    case NetworkUtility.APIRESULT.RESULT_NOT_EXIST:
                        Toast.makeText(getContext(), "존재하지 않는 앨범입니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case NetworkUtility.APIRESULT.RESULT_FILE_GET_ERROR:
                        Toast.makeText(getContext(), "파일을 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case NetworkUtility.APIRESULT.RESULT_NO_AUTHOR:
                        Toast.makeText(getContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<AlbumResultItem> call, Throwable t) {

            }
        });
    }

    private void getMyFavoritePictureTask(){
        NetworkUtility.getInstance().myfavoritePictureList(AppUtility.memberId, new Callback<AlbumResultItem>() {
            @Override
            public void onResponse(Call<AlbumResultItem> call, Response<AlbumResultItem> response) {
                AlbumResultItem albumResultItem = response.body();

                switch (albumResultItem.getCode()) {
                    case NetworkUtility.APIRESULT.RESULT_SUCCESS:
                        myFavoriteList = new ArrayList<>();
                        if (albumResultItem.getResult() != null && albumResultItem.getResult().size() > 0) {
                            for (int i = 0; i < albumResultItem.getResult().size(); i++) {
                                myFavoriteList.add(albumResultItem.getResult().get(i).getResult());
                                myFavoriteList.get(i).setArrayIndex(i);
                            }

                            updateImageListView();
                        }

                        break;
                    case NetworkUtility.APIRESULT.RESULT_NOT_EXIST:
                        Toast.makeText(getContext(), "존재하지 않는 앨범입니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case NetworkUtility.APIRESULT.RESULT_FILE_GET_ERROR:
                        Toast.makeText(getContext(), "파일을 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case NetworkUtility.APIRESULT.RESULT_NO_AUTHOR:
                        Toast.makeText(getContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<AlbumResultItem> call, Throwable t) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IntentProtocol.GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null){
            uploadProfileTask(data.getData());
        }
    }


    private void setProfileView(String data){
        profileIconView.setVisibility(View.GONE);
        profileView.setVisibility(View.VISIBLE);
        glide.load(data)
                .apply(RequestOptions.circleCropTransform())
                .into(profileView);
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

            updateImageListView();
        }
    }

    private void uploadProfileTask(Uri file){
        NetworkUtility.getInstance().uploadProfile(file, AppUtility.memberId, new Callback<DefaultResultItem>() {
            @Override
            public void onResponse(Call<DefaultResultItem> call, Response<DefaultResultItem> response) {
                DefaultResultItem item = response.body();

                switch (item.getCode()) {
                    case NetworkUtility.APIRESULT.RESULT_SUCCESS :
                        setProfileView(file.toString());
                        break;
                    case NetworkUtility.APIRESULT.RESULT_FAIL :
                        Toast.makeText(getContext(), "프로필 사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<DefaultResultItem> call, Throwable t) {

            }
        });
    }

    private void getProfileTask(){
        NetworkUtility.getInstance().getProfilePhoto(AppUtility.memberId, new Callback<ProfileResultItem>() {
            @Override
            public void onResponse(Call<ProfileResultItem> call, Response<ProfileResultItem> response) {
                ProfileResultItem item = response.body();

                Log.d("profile_photo", item.toString());
                switch (item.getCode()) {
                    case NetworkUtility.APIRESULT.RESULT_SUCCESS :
                        if (item.getResult().length() > 0) {
                            setProfileView(item.getResult());
                        }
                        break;
                    case NetworkUtility.APIRESULT.RESULT_FAIL :
                        Toast.makeText(getContext(), "프로필 사진 가져오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProfileResultItem> call, Throwable t) {

            }
        });
    }
    public void refreshAllData(){
        getProfileTask();
        getMyUploadPictureTask();
        getMyFavoritePictureTask();
    }
    private void updateImageListView(){
        //여기서는 그냥 데이터만 바꿔주도록 하자
        if (toggledIndex == 0) {
            //올린사진
            Log.d("toggled", "my upload");
            imageListFragment.resetImageList(myUploadList);
        } else {
            //좋아요한 사진

            Log.d("toggled", "my favorite");
            imageListFragment.resetImageList(myFavoriteList);
        }
    }
}
