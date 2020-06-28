package com.picaproject.pica.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.picaproject.pica.CustomView.CustomBottomButton;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.IntentProtocol;

import java.util.ArrayList;

import mabbas007.tagsedittext.TagsEditText;

public class ImageInputContentActivity extends BaseToolbarActivity {
    private TagsEditText tagsTextview;
    private EditText contentsEdittext;
    private String init_contents;
    private ArrayList<String> init_tags;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_input_bottom_sheet);

        init_contents = getIntent().getStringExtra(IntentProtocol.INTENT_INPUT_CONTENT);
        init_tags = getIntent().getStringArrayListExtra(IntentProtocol.INTENT_INPUT_TAGS);

        initView();

    }

    private void initView() {
        contentsEdittext = findViewById(R.id.contents_input);
        tagsTextview = findViewById(R.id.tagsEditText);
        tagsTextview.setTagsBackground(R.drawable.bg_rounded_tag_box);
        tagsTextview.setTagsTextColor(android.R.color.white);
        tagsTextview.setCloseDrawableRight(R.drawable.ic_baseline_close_24);
        tagsTextview.setTagsTextSize(R.dimen.tags_text_size);
        tagsTextview.setCloseDrawablePadding(R.dimen.tags_close_padding);
        CustomBottomButton save = findViewById(R.id.save_button);
        save.getButtonBackground().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> tags = (ArrayList<String>) tagsTextview.getTags();
                String contents = contentsEdittext.getText().toString();

                Intent intent = new Intent();
                intent.putExtra(IntentProtocol.INTENT_OUTPUT_TAGS, tags);
                intent.putExtra(IntentProtocol.INTENT_OUTPUT_CONTENT, contents);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //tagsTextview.setTags(new String[]{"asdfasd", "asdfasdfasdf", "제발"});

        if (init_contents != null && init_contents.length() > 0) {
            Log.d("이게 왜 안들어가", "ㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹ");
            contentsEdittext.setText(init_contents);

        }
        if (init_tags != null && init_tags.size() > 0)
            tagsTextview.setTags(init_tags.toArray(new String[0]));

    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbarTitle("이미지 태그 및 내용");
    }
}