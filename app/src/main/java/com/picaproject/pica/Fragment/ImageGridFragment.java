package com.picaproject.pica.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.picaproject.pica.CustomView.ImageRecyclerAdapter;
import com.picaproject.pica.CustomView.SpacesItemDecoration;
import com.picaproject.pica.Item.ImageItem;
import com.picaproject.pica.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageGridFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private ImageRecyclerAdapter adapter;
    private boolean recyclerviewScroll = true;

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

    public static Fragment newInstance(){
        ImageGridFragment fragment = new ImageGridFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recyclerviewScroll = getArguments().getBoolean("param1");
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
        adapter = new ImageRecyclerAdapter(getContext(), makeImageSampleList());
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(SpacesItemDecoration.RecyclerViewOrientation.GRID, 4, 3);
        mRecyclerView.addItemDecoration(decoration);
    }


    public void changeImageList(){
        adapter.resetImageList(makeImageSampleList());
    }
    private ArrayList<ImageItem> makeImageSampleList(){
        ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i=0; i< 35; i++)
            imageItems.add(new ImageItem(R.drawable.img_sample));

        return imageItems;
    }
}
