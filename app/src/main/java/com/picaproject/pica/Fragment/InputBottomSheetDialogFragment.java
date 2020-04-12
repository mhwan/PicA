package com.picaproject.pica.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.picaproject.pica.R;

public class InputBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private boolean isTagView;
    public InputBottomSheetDialogFragment() {
    }

    public static BottomSheetDialogFragment newInstance(boolean param) {
        InputBottomSheetDialogFragment fragment = new InputBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("param1", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isTagView = getArguments().getBoolean("param1");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ui_input_bottom_sheet, container);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        return rootView;
    }
}
