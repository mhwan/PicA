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
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.NetworkItems.ImageResultItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageGridFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private ImageRecyclerAdapter adapter;
    private boolean recyclerviewScroll = true;
    private ArrayList<ImageResultItem> picDataArrayList = null;

    public ImageGridFragment(){
        // Required empty public constructor
    }

    public static Fragment newInstance(boolean param, ArrayList<ImageResultItem> picData) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle args = new Bundle();
        args.putBoolean("param1", param);
        args.putParcelableArrayList(IntentProtocol.KEY_PARCELABLE_PHOTO_DATA, picData);
        fragment.setArguments(args);
        return fragment;
    }
    public static Fragment newInstance(boolean param) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle args = new Bundle();
        args.putBoolean("param1", param);
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
        Log.d("imageGridFragment", "initview");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        if (!recyclerviewScroll)
            mRecyclerView.setNestedScrollingEnabled(false);

        if (picDataArrayList == null)
            picDataArrayList = new ArrayList<>();
        adapter = new ImageRecyclerAdapter(getContext(), picDataArrayList);
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(SpacesItemDecoration.RecyclerViewOrientation.GRID, 1, 5);
        mRecyclerView.addItemDecoration(decoration);
    }


    public void resetImageList(ArrayList<ImageResultItem> list){
        if (picDataArrayList == null)
            picDataArrayList = new ArrayList<>();
        picDataArrayList.clear();
        picDataArrayList.addAll(list);
        adapter.resetImageList(picDataArrayList);
    }
}
