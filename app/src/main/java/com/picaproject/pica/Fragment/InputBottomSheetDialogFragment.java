package com.picaproject.pica.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.picaproject.pica.R;

import mabbas007.tagsedittext.TagsEditText;

public class InputBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private TagsEditText tagsTextview;
    public InputBottomSheetDialogFragment() {
    }

    public static InputBottomSheetDialogFragment newInstance() {
        InputBottomSheetDialogFragment fragment = new InputBottomSheetDialogFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ui_input_bottom_sheet, container);
        initView(rootView);

        return rootView;
    }

    private void initView(View view) {
        tagsTextview = view.findViewById(R.id.tagsEditText);
        tagsTextview.setTagsBackground(R.drawable.bg_rounded_tag_box);
        tagsTextview.setTagsTextColor(android.R.color.white);
        tagsTextview.setCloseDrawableRight(R.drawable.ic_baseline_close_24);
        tagsTextview.setTagsTextSize(R.dimen.tags_text_size);
        tagsTextview.setCloseDrawablePadding(R.dimen.tags_close_padding);
        //tagsTextview.setTagsListener(this);
    }

}
