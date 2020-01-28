package com.picaproject.pica.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;

import android.util.Log;
import android.widget.ImageView;


public class ImageViewAsyncTask extends AsyncTask {

    private ImageView imageView;
    private String src;

    private Bitmap bm;

    private static BitmapFactory.Options options;

    public ImageViewAsyncTask(ImageView imageView, String src) {
        this.imageView = imageView;
        this.src = src;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onCancelled(Object o) {
        Log.e("test_hs","ImageViewAsyncTask 이미지 로드 에러 발생");
        super.onCancelled(o);
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {

            if(options==null){
                options = new BitmapFactory.Options();
            }
            options.inSampleSize = 4;
            Bitmap  sample = BitmapFactory.decodeFile(src, options);

            bm = Bitmap.createScaledBitmap(sample, 80, 80, true);

           //bm = MediaStore.Images.Media.getBitmap(contentResolver, uri);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            cancel(true);
            Log.e("test_hs","ImageViewAsyncTask 이미지 로드 에러 발생 : "+e);
        }

        return null;
    }
    @Override
    protected void onPostExecute(Object o) {
        imageView.setImageBitmap(bm);
    }
}
