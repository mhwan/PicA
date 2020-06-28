package com.picaproject.pica.CustomView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Checkable;

import androidx.appcompat.widget.AppCompatTextView;

import com.picaproject.pica.R;

public class ToggledTextView extends AppCompatTextView implements Checkable {
    private boolean isChecked = false;
    private static final int[] STATE_CHECKABLE = {android.R.attr.state_checked};
    private int changed_color = android.R.color.holo_green_dark;
    private int default_color = android.R.color.black;
    private MODE mode = MODE.NONE;

    public ToggledTextView(Context context) {
        super(context);
        setDefault_color(default_color);
        setToggled_color(changed_color);
        setMode(mode);
    }

    public ToggledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ToggledTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @SuppressLint("ResourceAsColor")
    private void init(Context context, AttributeSet attrs) {
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.ToggledTextView, 0, 0);
        changed_color = attrArray.getColor(R.styleable.ToggledTextView_toggled_text_color, android.R.color.darker_gray);
        default_color = attrArray.getColor(R.styleable.ToggledTextView_default_text_color, android.R.color.black);
        isChecked = attrArray.getBoolean(R.styleable.ToggledTextView_toggled, false);
        mode = MODE.values()[attrArray.getInt(R.styleable.ToggledTextView_mode, 0)];
        attrArray.recycle();

        setMode(mode);
        changeTextColor((isChecked == true) ? changed_color : default_color);
    }

    public void setMode(MODE mode) {
        this.mode = mode;
        if (this.mode == MODE.BUTTON) {
            this.setClickable(true);
        }
    }
    public void setDefault_color(int color) {
        default_color = color;
        changeTextColor(isChecked);
    }
    public void setToggled_color(int color) {
        changed_color = color;
        changeTextColor(isChecked);
    }
    private void changeTextColor(int color) {
        this.setTextColor(color);
        invalidate();
    }
    private void changeTextColor(boolean isChecked) {
        int color = (isChecked)? changed_color : default_color;
        this.setTextColor(getResources().getColor(color));
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mode != MODE.NONE) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    changeTextColor(changed_color);
                    refreshDrawableState();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    changeTextColor(default_color);
                    refreshDrawableState();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked())
            mergeDrawableStates(drawableState, STATE_CHECKABLE);
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        if (isChecked)
            changeTextColor(changed_color);
        else
            changeTextColor(default_color);

        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        Log.d("toggle!!!", "" + !isChecked);
        setChecked(!isChecked);
        refreshDrawableState();
    }

    @Override
    public boolean performClick() {
        if (mode == MODE.TOGGLE) {
            toggle();
        }
        return super.performClick();
    }
    public enum MODE { NONE, BUTTON, TOGGLE}
}
