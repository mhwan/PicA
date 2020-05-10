package com.picaproject.pica.Activity;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.picaproject.pica.CustomView.CustomBottomButton;
import com.picaproject.pica.CustomView.InvitedFriendsRecyclerAdapter;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.ContactItem;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.NetworkUtility;

import java.util.ArrayList;

import okhttp3.ResponseBody;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);
        isManageMode = getIntent().getBooleanExtra(IntentProtocol.INTENT_ALBUM_MODE, false);

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
                createAlbumTask();
            }
        });

    }

    private void createAlbumTask(){
        NetworkUtility.getInstance().createNewAlbum(pictureUri, editName.getText().toString(), editDesc.getText().toString(), 1,  new Callback<ResponseBody>(){

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("CREATE ALBUM", response.body().toString());
                } else
                    Log.i("CREATE ALBUM", "onresponse but failed");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("CREATE ALBUM", "FAILED");
            }
        });
    }

    private void initView(){
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
                if (recyclerAdapter.getInvitedList() != null &&recyclerAdapter.getItemCount() > 0)
                    intent.putExtra(IntentProtocol.INTENT_CONTACT_LIST, recyclerAdapter.getInvitedList());

                startActivityForResult(intent, IntentProtocol.REQUEST_INVITE_MEMBER);
            }
        });
        updateInviteMemberView(null);
    }

    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IntentProtocol.RESULT_LOAD_IMAGE);
    }

    private void updateInviteMemberView(ArrayList<ContactItem> contactItems){
        if (contactItems != null && contactItems.size() > 0) {
            customBottomButton.setButton_text("초대한 친구관리");
            recyclerView.setVisibility(View.VISIBLE);
            noFriendsView.setVisibility(View.GONE);
            countFriendsView.setVisibility(View.VISIBLE);
            countFriendsView.setText("("+contactItems.size()+")");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentProtocol.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            imageShow.setVisibility(View.VISIBLE);
            imageUpload.setVisibility(View.GONE);
            imageShow.setImageURI(selectedImage);

            pictureUri = selectedImage;
            //imageShow.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } else if (requestCode == IntentProtocol.REQUEST_INVITE_MEMBER && resultCode == RESULT_OK) {
            ArrayList<ContactItem> contactList = (ArrayList<ContactItem>)data.getSerializableExtra(IntentProtocol.INTENT_RESULT_SELECTED_ID);
            if (contactList != null && contactList.size() >0) {
                updateInviteMemberView(contactList);
            }
        }
    }
}
