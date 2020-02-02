package com.picaproject.pica.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.picaproject.pica.CustomView.ImageRecyclerAdapter;
import com.picaproject.pica.CustomView.SpacesItemDecoration;
import com.picaproject.pica.Item.ImageItem;
import com.picaproject.pica.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumMainFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    public AlbumMainFragment() {
        // Required empty public constructor
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

        ImageRecyclerAdapter adapter = new ImageRecyclerAdapter(getContext(), makeImageSampleList());
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(SpacesItemDecoration.RecyclerViewOrientation.GRID, 10, 3);
        mRecyclerView.addItemDecoration(decoration);
    }

    private ArrayList<ImageItem> makeImageSampleList(){
        ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i=0; i< 35; i++)
            imageItems.add(new ImageItem(R.drawable.img_sample));

        return imageItems;
    }
}
