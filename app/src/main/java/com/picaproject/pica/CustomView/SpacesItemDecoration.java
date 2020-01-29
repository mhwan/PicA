package com.picaproject.pica.CustomView;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * horizontal에서 첫번째가 아닌데도 왼쪽에 너비가 생기는 현상..
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace = -1;
    private int mLeftSpace = -1;
    private int mRightSpace = -1;
    private int mBottomSpace = -1;
    private int mTopSpace = -1;
    private int mRow = 0;
    private RecyclerViewOrientation orientation;

    public SpacesItemDecoration(RecyclerViewOrientation orientation, int space) {
        this(orientation, space, 0);
    }

    public SpacesItemDecoration(RecyclerViewOrientation orientation, int space, int rowCount){
        this.orientation = orientation;
        this.mSpace = space;
        this.mRow = rowCount;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mLeftSpace > -1 && mRightSpace > -1 && mBottomSpace > -1 && mTopSpace > -1) {
            outRect.left = mLeftSpace;
            outRect.right = mRightSpace;
            outRect.bottom = mBottomSpace;
            outRect.top = mTopSpace;
        } else {
            if (orientation == RecyclerViewOrientation.LINEAR_VERTICAL) {
                outRect.left = mSpace;
                outRect.right = mSpace;
                outRect.bottom = mSpace;
                if (parent.getChildAdapterPosition(view) == 0)
                    outRect.top = mSpace;
            } else if (orientation == RecyclerViewOrientation.LINEAR_HORIZONTAL){
                outRect.top = mSpace;
                outRect.bottom = mSpace;
                outRect.right = mSpace;
                if (parent.getChildAdapterPosition(view) == 0)
                    outRect.left = mSpace;
            } else if (orientation == RecyclerViewOrientation.GRID && mRow > 0) {
                //first line need top other is not
                int i = parent.getChildAdapterPosition(view);
                outRect.bottom = mSpace;
                outRect.right = mSpace;
                if (i < mRow)
                    outRect.top = mSpace;

                if (i % mRow == 0)
                    outRect.left = mSpace;
            }
        }
        if (mSpace > -1) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
                outRect.left = mSpace;
                outRect.right = mSpace;
            }
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
        }

        // Add top margin only for the first item to avoid double space between items
        /*
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = mSpace;*/
    }
    public SpacesItemDecoration(int left, int right, int top, int bottom){
        this.mLeftSpace = left;
        this.mRightSpace = right;
        this.mTopSpace = top;
        this.mBottomSpace = bottom;
    }

    public enum  RecyclerViewOrientation{
        LINEAR_VERTICAL, LINEAR_HORIZONTAL, GRID}
}
