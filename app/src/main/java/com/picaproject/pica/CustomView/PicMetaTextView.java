package com.picaproject.pica.CustomView;

import android.content.Context;
import android.util.AttributeSet;


public class PicMetaTextView extends androidx.appcompat.widget.AppCompatTextView {

    private String metaText;
    /*
    * 뷰 생성시 바로 보이는것이 아닌 메타데이터가 설정되어야 보이는 뷰
    *
    * */
    public PicMetaTextView(Context context) {
        super(context);
        setVisibility(GONE);
    }

    public PicMetaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVisibility(GONE);
    }

    public PicMetaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVisibility(GONE);
    }

    public void removeMetaText() {
        metaText = null;
        setVisibility(GONE);
    }

    public void setMetaText(String metaText) {
        this.metaText = metaText;
        setVisibility(VISIBLE);
    }
}
