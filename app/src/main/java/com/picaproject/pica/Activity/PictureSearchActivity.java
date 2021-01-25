package com.picaproject.pica.Activity;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.picaproject.pica.Fragment.ImageGridFragment;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.Util.NetworkItems.AlbumResultItem;
import com.picaproject.pica.Util.NetworkItems.ImageResultItem;
import com.picaproject.pica.Util.NetworkUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PictureSearchActivity extends BaseToolbarActivity {
    private String searchQuery;
    private ImageGridFragment imageListFragment;
    private ArrayList<ImageResultItem> searchList = new ArrayList<>();
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_search);
        searchQuery = getIntent().getStringExtra(IntentProtocol.INTENT_QUERY);

        initView();
    }

    private void initView(){
        initFragment();
        textView = (TextView) findViewById(R.id.result_text);

        SearchView searchView = (SearchView) findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() >= 1){
                    searchQuery = s;
                    searchDataService();
                } else
                    Toast.makeText(getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.requestFocus();

        if (searchQuery != null && searchQuery.length() > 0) {
            searchView.setQuery(searchQuery, true);
            //searchDataService();
        }
    }

    private void initFragment(){
        imageListFragment = (ImageGridFragment) ImageGridFragment.newInstance(false, searchList);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frag_image_list, imageListFragment);
        transaction.commit();
    }

    private void updateData(){
        imageListFragment.resetImageList(searchList);
    }

    private void searchDataService(){
        NetworkUtility.getInstance().getPictureSearchList(AppUtility.memberId, searchQuery, new Callback<AlbumResultItem>() {
            @Override
            public void onResponse(Call<AlbumResultItem> call, Response<AlbumResultItem> response) {
                AlbumResultItem albumResultItem = response.body();

                switch (albumResultItem.getCode()) {
                    case NetworkUtility.APIRESULT.RESULT_SUCCESS:
                        searchList = new ArrayList<>();
                        int size = 0;
                        if (albumResultItem.getResult() != null && albumResultItem.getResult().size() > 0) {
                            size = albumResultItem.getResult().size();
                            for (int i = 0; i < albumResultItem.getResult().size(); i++) {
                                searchList.add(albumResultItem.getResult().get(i).getResult());
                                searchList.get(i).setArrayIndex(i);
                            }


                        } else if (albumResultItem.getResult().size() == 0) {
                            Toast.makeText(getApplicationContext(), "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
                        }

                        updateData();
                        textView.setText("검색결과 : " + size);

                        break;
                    case NetworkUtility.APIRESULT.RESULT_NOT_EXIST:
                        Toast.makeText(getApplicationContext(), "존재하지 않는 앨범입니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case NetworkUtility.APIRESULT.RESULT_FILE_GET_ERROR:
                        Toast.makeText(getApplicationContext(), "파일을 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case NetworkUtility.APIRESULT.RESULT_NO_AUTHOR:
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<AlbumResultItem> call, Throwable t) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        setToolbarTitle("인공지능 검색");
    }
}