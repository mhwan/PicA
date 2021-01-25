package com.picaproject.pica.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.picaproject.pica.CustomView.CustomBottomButton;
import com.picaproject.pica.CustomView.InvitedFriendsRecyclerAdapter;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.IntentProtocol;
import com.picaproject.pica.Item.ContactItem;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.NetworkItems.DefaultResultItem;
import com.picaproject.pica.Util.NetworkUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAlbumActivity extends BaseToolbarActivity {
    private ImageView imageShow;
    private ImageView imageUpload;
    private RecyclerView recyclerView;
    private TextView noFriendsView;
    private TextView countFriendsView;
    private EditText editName, editDesc;
    private InvitedFriendsRecyclerAdapter recyclerAdapter;
    private CustomBottomButton customBottomButton;
    private boolean isManageMode = false;
    private Uri pictureUri;
    private String initAlbumName, initAlbumDesc, initAlbumPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);
        isManageMode = getIntent().getBooleanExtra(IntentProtocol.INTENT_ALBUM_MODE, false);

        if (isManageMode) {
            initAlbumName = getIntent().getStringExtra(IntentProtocol.INTENT_INPUT_ALBUM_TITLE);
            initAlbumDesc = getIntent().getStringExtra(IntentProtocol.INTENT_INPUT_ALBUM_DESC);
            initAlbumPic = getIntent().getStringExtra(IntentProtocol.INTENT_INPUT_ALBUM_PICTURE);
        }
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String buttonName = "생성";
        if (isManageMode)
            buttonName = "수정";
        setToolbarButton(buttonName, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CreateAlbumActivity.this, AlbumMainActivity.class));
                Log.d("make album", "on click");
                createAlbumTask();
            }
        });

    }

    private void createAlbumTask() {
        NetworkUtility.getInstance().createNewAlbum(pictureUri, editName.getText().toString(), editDesc.getText().toString(), AppUtility.memberId, new Callback<DefaultResultItem>() {

            @Override
            public void onResponse(Call<DefaultResultItem> call, Response<DefaultResultItem> response) {
                DefaultResultItem defaultResultItem = response.body();

                System.out.println();
                if (response.isSuccessful() && defaultResultItem != null) {
                    switch (defaultResultItem.getCode()) {
                        case NetworkUtility.APIRESULT.RESULT_SUCCESS:
                            Toast.makeText(getApplicationContext(), "앨범이 생성되었습니다.", Toast.LENGTH_SHORT).show();

                            finish();
                            break;
                        case NetworkUtility.APIRESULT.RESULT_CREATEALBUM_SERVER_ERROR:
                            Toast.makeText(getApplicationContext(), "서버오류로 앨범생성에 실패했습니다.", Toast.LENGTH_SHORT).show();

                            break;
                        case NetworkUtility.APIRESULT.RESULT_CREATEALBUM_DB_ERROR:
                            Toast.makeText(getApplicationContext(), "DB오류로 앨범생성에 실패했습니다.", Toast.LENGTH_SHORT).show();

                            break;
                    }
                }
                /*
                if (response.isSuccessful()) {
                    Log.i("CREATE ALBUM", response.body().toString());
                } else
                    Log.i("CREATE ALBUM", "onresponse but failed");*/
            }

            @Override
            public void onFailure(Call<DefaultResultItem> call, Throwable t) {
                Log.i("CREATE ALBUM", "FAILED");
                t.printStackTrace();
            }
        });
    }

    private void initView() {
        editName = (EditText) findViewById(R.id.edit_albumname);
        editDesc = (EditText) findViewById(R.id.edit_albumdesc);
        noFriendsView = (TextView) findViewById(R.id.no_friends_text);
        countFriendsView = (TextView) findViewById(R.id.invite_friends_count);
        imageShow = (ImageView) findViewById(R.id.imageview_show);
        imageUpload = (ImageView) findViewById(R.id.imageview_upload);
        recyclerView = (RecyclerView) findViewById(R.id.invite_recyclerview);
        recyclerAdapter = new InvitedFriendsRecyclerAdapter(new ArrayList<ContactItem>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(recyclerAdapter);

        findViewById(R.id.frame_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        customBottomButton = (CustomBottomButton) findViewById(R.id.invited_friends);
        customBottomButton.getButtonBackground().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "ccc");
                Intent intent = new Intent(CreateAlbumActivity.this, InviteMemberActivity.class);
                if (recyclerAdapter.getInvitedList() != null && recyclerAdapter.getItemCount() > 0)
                    intent.putExtra(IntentProtocol.INTENT_CONTACT_LIST, recyclerAdapter.getInvitedList());

                startActivityForResult(intent, IntentProtocol.REQUEST_INVITE_MEMBER);
            }
        });
        updateInviteMemberView(null);

        if (isManageMode) {
            editName.setText(initAlbumName);
            editDesc.setText(initAlbumDesc);
            setImage(initAlbumPic);
        }
    }

    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IntentProtocol.RESULT_LOAD_IMAGE);
    }

    private void updateInviteMemberView(ArrayList<ContactItem> contactItems) {
        if (contactItems != null && contactItems.size() > 0) {
            customBottomButton.setButton_text("초대한 친구관리");
            recyclerView.setVisibility(View.VISIBLE);
            noFriendsView.setVisibility(View.GONE);
            countFriendsView.setVisibility(View.VISIBLE);
            countFriendsView.setText("(" + contactItems.size() + ")");
            recyclerAdapter.setInvitedList(contactItems);
            recyclerAdapter.notifyDataSetChanged();
            //recyclerAdapter.notifyAll();
        } else {
            customBottomButton.setButton_text("친구초대");
            recyclerView.setVisibility(View.GONE);
            noFriendsView.setVisibility(View.VISIBLE);
            countFriendsView.setVisibility(View.GONE);
        }
    }

    private void setImage(String uriData){
        imageShow.setVisibility(View.VISIBLE);
        imageUpload.setVisibility(View.GONE);
        //imageShow.setImageURI(selectedImage);
        Glide.with(getApplicationContext()).load(uriData).error(R.drawable.img_sample).into(imageShow);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentProtocol.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            setImage(selectedImage.toString());
            pictureUri = selectedImage;

            //imageShow.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } else if (requestCode == IntentProtocol.REQUEST_INVITE_MEMBER && resultCode == RESULT_OK) {
            ArrayList<ContactItem> contactList = (ArrayList<ContactItem>) data.getSerializableExtra(IntentProtocol.INTENT_RESULT_SELECTED_ID);
            if (contactList != null && contactList.size() > 0) {
                updateInviteMemberView(contactList);
            }
        }
    }
}
