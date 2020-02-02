package com.picaproject.pica.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.picaproject.pica.CustomView.AlbumRecyclerAdapter;
import com.picaproject.pica.CustomView.SpacesItemDecoration;
import com.picaproject.pica.Item.AlbumItem;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class MyAlbumFragment extends Fragment {

    private View view;
    private ArrayList<AlbumItem> albumList;
    public MyAlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDefaultItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_album, container, false);
        initView();
        return view;
    }

    private void initView(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new AlbumRecyclerAdapter(getContext(), albumList));
        recyclerView.addItemDecoration(new SpacesItemDecoration(SpacesItemDecoration.RecyclerViewOrientation.LINEAR_VERTICAL, 6));
    }

    private void setupDefaultItem(){
        albumList = new ArrayList<>();
        for (int i = 0; i<7; i++)
            albumList.add(new AlbumItem("앨범 샘플"+(i+1), "mhwan", R.drawable.img_sample));

    }
}
