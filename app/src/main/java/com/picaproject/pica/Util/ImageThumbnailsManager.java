package com.picaproject.pica.Util;

import android.content.Context;
import android.graphics.Bitmap;

import com.picaproject.pica.Item.ThumbnailItem;
import com.picaproject.pica.R;

import java.util.ArrayList;
import java.util.List;

public final class ImageThumbnailsManager {
    private static List<ThumbnailItem> filterThumbs = new ArrayList<ThumbnailItem>(10);
    private static List<ThumbnailItem> processedThumbs = new ArrayList<ThumbnailItem>(10);

    private ImageThumbnailsManager() {
    }

    public static void addThumb(ThumbnailItem thumbnailItem) {
        filterThumbs.add(thumbnailItem);
    }

    public static List<ThumbnailItem> processThumbs() {
        for (ThumbnailItem thumb : filterThumbs) {
            // scaling down the image
            float aspectRatio = thumb.getImage().getWidth() /
                    (float) thumb.getImage().getHeight();
            int width = 960;
            int height = Math.round(width / aspectRatio);

            thumb.setImage(Bitmap.createScaledBitmap(thumb.getImage(), width, height, false));
            thumb.setImage(thumb.getFilter().processFilter(thumb.getImage()));
            processedThumbs.add(thumb);
        }
        return processedThumbs;
    }

    public static void clearThumbs() {
        filterThumbs = new ArrayList<>();
        processedThumbs = new ArrayList<>();
    }
}
