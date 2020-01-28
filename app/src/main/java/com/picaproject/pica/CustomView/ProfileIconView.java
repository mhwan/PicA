package com.picaproject.pica.CustomView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.picaproject.pica.R;

public class ProfileIconView extends RelativeLayout {
    private CircleView drawCircle;
    private ImageView icon;
    private int padding_ic_px = -1;
    private int icon_id = R.drawable.ic_person;
    private int background_color;
    private int boarder_width = 0;
    private int boarder_color;
    private boolean isDrawStroke = false;

    public ProfileIconView(Context context) {
        super(context);
        initView(context, null);
        setAllView();
    }

    public ProfileIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
        setTypedArray(context, attrs);
    }

    public ProfileIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
        setTypedArray(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProfileIconView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
        setTypedArray(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        drawCircle = new CircleView(context, attrs);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(drawCircle, params);
        icon = new ImageView(context, attrs);
        params.addRule(CENTER_IN_PARENT, TRUE);
        addView(icon, params);
    }

    private void setAllView(){
        setIconResource(icon_id);
        setCircleBackgroundColor(background_color);
        if(isDrawStroke) {
            setBoarderWidth(boarder_width);
            setBoarderColor(boarder_color);
        }
        Log.d("padding", padding_ic_px+"");
        if (padding_ic_px < 0) {
            autoPadding();
        }

        else {
            padding_ic_px = dpToPx(padding_ic_px);
            setIconPadding(padding_ic_px);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void setTypedArray(Context context, AttributeSet attrs){
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ProfileIconView, 0, 0);
        icon_id = attr.getResourceId(R.styleable.ProfileIconView_icon_src, R.drawable.ic_person);
        background_color = attr.getColor(R.styleable.ProfileIconView_bg_color, context.getResources().getColor(android.R.color.holo_blue_light));
        padding_ic_px = (int) (attr.getDimension(R.styleable.ProfileIconView_icon_padding, -12)/context.getResources().getDisplayMetrics().density);
        boarder_width = (int) (attr.getDimension(R.styleable.ProfileIconView_border_width, 0)/context.getResources().getDisplayMetrics().density);
        boarder_color = attr.getColor(R.styleable.ProfileIconView_border_color, context.getResources().getColor(android.R.color.holo_blue_light));

        if (boarder_width > 0)
            isDrawStroke = true;

        setAllView();

        attr.recycle();
    }


    public void setBoarderWidth(int dp){
        boarder_width = dpToPx(dp);
        if (boarder_width > 0)
            isDrawStroke = true;
        drawCircle.setBoarder(boarder_width);
    }

    public void setBoarderColor(int color){
        boarder_color = color;
        drawCircle.setBoarderColor(boarder_color);
    }
    public void setIconResource(int resource){
        icon_id = resource;
        icon.setImageResource(icon_id);
    }

    public void setCircleBackgroundColor(int color){
        background_color = color;
        drawCircle.setCircleBackgroundColor(background_color);
    }

    public void setIconPadding(int dp){
        padding_ic_px = dp;
        icon.setPadding(padding_ic_px, padding_ic_px, padding_ic_px, padding_ic_px);
    }
    private int dpToPx(int dp) {
        int px = Math.round(dp * ((float) getContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private void autoPadding() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = getMeasuredWidth();
                int padding = (21*width)/84;
                if (padding_ic_px < 0) {
                    icon.setPadding(padding, padding, padding, padding);
                }
            }
        });
    }
}
