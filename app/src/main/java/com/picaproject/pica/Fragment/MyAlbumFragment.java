package com.picaproject.pica.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.picaproject.pica.CustomView.AlbumRecyclerAdapter;
import com.picaproject.pica.CustomView.SpacesItemDecoration;
import com.picaproject.pica.Item.AlbumItem;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.NetworkItems.MyAlbumResultListItem;
import com.picaproject.pica.Util.NetworkUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAlbumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AlbumRecyclerAdapter recyclerAdapter;
    public MyAlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupDefaultItem();
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
        getMyAlbumTask();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.album_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new AlbumRecyclerAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(SpacesItemDecoration.RecyclerViewOrientation.LINEAR_VERTICAL, 6));
    }

    /*
    private void setupDefaultItem(){
        albumList = new ArrayList<>();
        for (int i = 0; i<7; i++)
            albumList.add(new AlbumItem("앨범 샘플"+(i+1), "mhwan", R.drawable.img_sample));

    }*/

    private void getMyAlbumTask(){
        NetworkUtility.getInstance().getMyAlbumList(1, new Callback<MyAlbumResultListItem>() {
            @Override
            public void onResponse(Call<MyAlbumResultListItem> call, Response<MyAlbumResultListItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MyAlbumResultListItem item = response.body();
                    Log.i("Album_response", item.toString());
                    switch (item.getCode()) {
                        case NetworkUtility.APIRESULT.RESULT_SUCCESS :
                            ArrayList<AlbumItem> albumList;
                            if (item.getResult() != null && item.getResult().size() > 0) {
                                albumList = (ArrayList<AlbumItem>) item.getResult();
                            } else
                                albumList = new ArrayList<>();


                            recyclerAdapter.refreshAllItem(albumList);
                            break;
                        case NetworkUtility.APIRESULT.RESULT_NOT_EXIST :
                            Toast.makeText(getActivity(), "존제하지 않는 멤버입니다.", Toast.LENGTH_SHORT).show();
                            break;
                        case NetworkUtility.APIRESULT.RESULT_MYALBUM_FILE_ERROR :
                            Toast.makeText(getActivity(), "파일을 가져오는 중 실패했습니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }



                }
            }

            @Override
            public void onFailure(Call<MyAlbumResultListItem> call, Throwable t) {
                Log.i("GET_MY_ALBUM", "FAILED");
                t.printStackTrace();
            }
        });
    }
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
