package com.picaproject.pica.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.picaproject.pica.CustomView.ImageThumbnailsRecyclerAdapter;
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.Item.ThumbnailItem;
import com.picaproject.pica.Listener.ThumbnailCallbackListener;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.ImageThumbnailsManager;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.List;

/**
 * 필터를 보여줄 예시 이미지는 사이즈를 리사이징 해서 보여주고
 * 실제로 반환할때는 원본 사이즈에 필터를 적용한 bitmap을 uri로 바꿔서 해준다
 */
public class ImageFilterActivity extends BaseToolbarActivity implements ThumbnailCallbackListener {
    private RecyclerView thumbListView;
    private ImageView placeHolderImageView;
    private Bitmap orgbitmapImage;
    private String originalURiString;
    private ImageThumbnailsRecyclerAdapter adapter;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);
        originalURiString = getIntent().getStringExtra(IntentProtocol.INTENT_FILTER_INPUT);
        initView();
    }

    private void initView() {
        thumbListView = (RecyclerView) findViewById(R.id.thumbnails);
        placeHolderImageView = (ImageView) findViewById(R.id.place_holder_imageview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        thumbListView.setLayoutManager(layoutManager);
        thumbListView.setHasFixedSize(true);

        Uri uri = Uri.parse(originalURiString);
        Glide.with(this)
                .asBitmap()
                .override(640)
                .load(uri)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        orgbitmapImage = resource;
                        bindDataToAdapter();

                        placeHolderImageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });


    }


    private void bindDataToAdapter() {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                Bitmap thumbImage = orgbitmapImage;
                ThumbnailItem t1 = new ThumbnailItem();
                ThumbnailItem t2 = new ThumbnailItem();
                ThumbnailItem t3 = new ThumbnailItem();
                ThumbnailItem t4 = new ThumbnailItem();
                ThumbnailItem t5 = new ThumbnailItem();
                ThumbnailItem t6 = new ThumbnailItem();

                t1.setImage(thumbImage);
                t2.setImage(thumbImage);
                t3.setImage(thumbImage);
                t4.setImage(thumbImage);
                t5.setImage(thumbImage);
                t6.setImage(thumbImage);

                ImageThumbnailsManager.clearThumbs();
                ImageThumbnailsManager.addThumb(t1); // Original Image

                t2.setFilter(SampleFilters.getStarLitFilter());
                ImageThumbnailsManager.addThumb(t2);

                t3.setFilter(SampleFilters.getBlueMessFilter());
                ImageThumbnailsManager.addThumb(t3);

                t4.setFilter(SampleFilters.getAweStruckVibeFilter());
                ImageThumbnailsManager.addThumb(t4);

                t5.setFilter(SampleFilters.getLimeStutterFilter());
                ImageThumbnailsManager.addThumb(t5);

                t6.setFilter(SampleFilters.getNightWhisperFilter());
                ImageThumbnailsManager.addThumb(t6);

                List<ThumbnailItem> thumbs = ImageThumbnailsManager.processThumbs();


                adapter = new ImageThumbnailsRecyclerAdapter(getApplicationContext(), thumbs, ImageFilterActivity.this::onThumbnailClick);
                thumbListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    @Override
    public void onThumbnailClick(Bitmap image) {
        Log.d("thumbnail", "click!!");
        placeHolderImageView.setImageBitmap(image);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbarTitle("이미지 필터");
        setToolbarButton("확인", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //string으로 uri가진걸 새로운 비트맵으로 만든다
                finishWork();
            }
        });

    }

    private void finishWork(){
        Filter selectFilter = adapter.getSelectItem().getFilter();
        Uri uri = Uri.parse(originalURiString);
        Glide.with(this)
                .asBitmap()
                .load(uri)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap newBitmap = selectFilter.processFilter(resource);
                        Uri uri = AppUtility.getAppinstance().getImageUri(newBitmap);
                        Intent intent = new Intent();
                        intent.putExtra(IntentProtocol.INTENT_FILTER_OUTPUT, uri.toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

}