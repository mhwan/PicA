package com.picaproject.pica.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.picaproject.pica.CustomView.CommentRecyclerAdapter;
import com.picaproject.pica.Item.CommentItem;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class CommentActivity extends BaseToolbarActivity {
    private RecyclerView recyclerView;
    private EditText editText;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        initview();
    }

    private void initview(){
        editText = findViewById(R.id.message_input);
        findViewById(R.id.message_send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        commentRecyclerAdapter = new CommentRecyclerAdapter(getApplicationContext(), makeSampleList(), 1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentRecyclerAdapter);
    }

    private ArrayList<CommentItem> makeSampleList() {
        ArrayList<CommentItem> list = new ArrayList<>();
        for (int i = 0; i< 6; i++) {
            CommentItem item = new CommentItem();
            item.setId(i);
            item.setAuthorId(0);

            item.setNickname("Mhwan");
            item.setContents("댓글 샘플리스트입니다."+i+1);
            item.setDate("2020.07.01 12:30");
            if (i == 3) {
                item.setTagId("krmax");
                item.setAuthorId(1);
            }

            list.add(item);
        }

        return list;
    }
    @Override
    protected void onStart() {
        super.onStart();

        setToolbarTitle("댓글");
    }

    @Override
    public void finish() {

        overridePendingTransition(R.anim.slide_down, 0);
        super.finish();
    }
}