package com.picaproject.pica.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.picaproject.pica.CustomView.CommentRecyclerAdapter;
import com.picaproject.pica.Item.ReplyItem;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.Util.NetworkItems.DefaultResultItem;
import com.picaproject.pica.Util.NetworkItems.ReplyResultItem;
import com.picaproject.pica.Util.NetworkUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends BaseToolbarActivity {
    private RecyclerView recyclerView;
    private EditText editText;
    private int pictureId;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pictureId = getIntent().getIntExtra(IntentProtocol.INTENT_PICTURE_ID, -1);
        setContentView(R.layout.activity_comment);

        initview();
    }

    private void initview(){
        editText = findViewById(R.id.message_input);
        findViewById(R.id.message_send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().length() > 0)
                    addReplyTask(editText.getText().toString());
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        commentRecyclerAdapter = new CommentRecyclerAdapter(this, new ArrayList<>(), 1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentRecyclerAdapter);

        getReplyTask();
    }

    private void addReplyTask(String input) {
        NetworkUtility.getInstance().addPictureReply(pictureId, AppUtility.memberId, input, new Callback<DefaultResultItem>() {
            @Override
            public void onResponse(Call<DefaultResultItem> call, Response<DefaultResultItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DefaultResultItem item = response.body();

                    if (item.getCode() != NetworkUtility.APIRESULT.RESULT_SUCCESS) {
                        Toast.makeText(getApplicationContext(), "댓글 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        editText.setText("");
                        getReplyTask();
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResultItem> call, Throwable t) {

            }
        });
    }
    private void getReplyTask() {
        NetworkUtility.getInstance().getPictureReplyList(pictureId, AppUtility.memberId, new Callback<ReplyResultItem>() {
            @Override
            public void onResponse(Call<ReplyResultItem> call, Response<ReplyResultItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReplyResultItem item = response.body();

                    Log.i("reply", item.toString());
                    switch (item.getCode()) {
                        case NetworkUtility.APIRESULT.RESULT_SUCCESS :
                            ArrayList<ReplyItem> list = new ArrayList<>();
                            if (item.getResult() != null && item.getResult().size() >0) {
                                list = (ArrayList<ReplyItem>) item.getResult();

                                commentRecyclerAdapter.resetCommentData(list);
                            }


                            break;
                        case NetworkUtility.APIRESULT.RESULT_FAIL :
                            Toast.makeText(getApplicationContext(), "댓글을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                            break;
                     }
                }
            }

            @Override
            public void onFailure(Call<ReplyResultItem> call, Throwable t) {

            }
        });
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