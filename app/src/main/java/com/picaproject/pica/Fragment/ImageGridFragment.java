package com.picaproject.pica.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.picaproject.pica.CustomView.ImageRecyclerAdapter;
import com.picaproject.pica.CustomView.SpacesItemDecoration;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.ImageItem;
import com.picaproject.pica.Item.PicPlaceData;
import com.picaproject.pica.Item.UploadPicData;
import com.picaproject.pica.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageGridFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private ImageRecyclerAdapter adapter;
    private boolean recyclerviewScroll = true;
    private ArrayList<UploadPicData> picDataArrayList = null;

    public ImageGridFragment(){
        // Required empty public constructor
    }

    public static Fragment newInstance(boolean param) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle args = new Bundle();
        args.putBoolean("param1", param);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(ArrayList<UploadPicData> picData) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(IntentProtocol.KEY_PARCELABLE_PHOTO_DATA, picData);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(){
        ImageGridFragment fragment = new ImageGridFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recyclerviewScroll = getArguments().getBoolean("param1");
            picDataArrayList = getArguments().getParcelableArrayList(IntentProtocol.KEY_PARCELABLE_PHOTO_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_album_main, container, false);
        initView();
        return view;
    }

    private void initView(){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        if (!recyclerviewScroll)
            mRecyclerView.setNestedScrollingEnabled(false);
        if (picDataArrayList == null) {
            Log.d("pickArraylist", "is null");
            picDataArrayList = makeImageSampleList();
        }
        adapter = new ImageRecyclerAdapter(getContext(), picDataArrayList);
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(SpacesItemDecoration.RecyclerViewOrientation.GRID, 4, 3);
        mRecyclerView.addItemDecoration(decoration);
    }


    public void changeImageList(){
        adapter.resetImageList(makeImageSampleList());
    }
    private ArrayList<UploadPicData> makeImageSampleList(){
        ArrayList<UploadPicData> imageItems = new ArrayList<>();
        UploadPicData data = new UploadPicData(R.drawable.img_sample);
        data.setLocation(new PicPlaceData("강남대학교"));
        data.setContents("여기 케이크 진짜 맛있었는데 크림이 사르르");
        data.setTags(new ArrayList<String>(Arrays.asList(new String[] {"카페", "케이크", "송도맛집"})));

        for (int i=0; i< 35; i++)
            imageItems.add(data);

        return imageItems;
    }
}
