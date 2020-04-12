package com.picaproject.pica.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.picaproject.pica.CustomView.SelectContactsAdapter;
import com.picaproject.pica.CustomView.SelectedProfileRecyclerAdapter;
import com.picaproject.pica.CustomView.SpacesItemDecoration;
import com.picaproject.pica.IntentProtocol;
import com.picaproject.pica.Item.ContactItem;
import com.picaproject.pica.Listener.SelectContactsRemoveListener;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppUtility;

import java.util.ArrayList;


/**
 * 연락처 접근권한부분 추가해야함
 */
public class InviteMemberActivity extends BaseToolbarActivity implements SelectContactsRemoveListener {
    private int numOfSelect = 0;
    private RecyclerView recyclerView;
    private ListView listView;
    private ArrayList<ContactItem> contactItems;
    private SelectedProfileRecyclerAdapter recyclerAdapter;
    private SelectContactsAdapter listviewAdapter;
    private int[] preSelectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);
        setContactList();
        initView();
    }

    private void setContactList(){
        contactItems = AppUtility.getAppinstance().getContactList(getApplicationContext());
        ArrayList<ContactItem> invitelist = (ArrayList<ContactItem>) getIntent().getSerializableExtra(IntentProtocol.INTENT_CONTACT_LIST);
        if (invitelist != null && invitelist.size() > 0) {
            preSelectList = new int[invitelist.size()];
            for (int i=0; i<invitelist.size(); i++) {
                int id = invitelist.get(i).getId();
                contactItems.get(id).setChecked(true);
                preSelectList[i] = id;
            }
            numOfSelect = invitelist.size();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        setToolbarButton("초대", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (numOfSelect > 0) {
                    ArrayList<ContactItem> checkedList = listviewAdapter.getAllCheckedItem();
                    intent.putExtra(IntentProtocol.INTENT_RESULT_SELECTED_ID, checkedList);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        setCountToolbarTitle();
    }

    private void setRecyclerViewFist(){
        if (numOfSelect == 0) {
            findViewById(R.id.invited_frame).setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.invited_frame).setVisibility(View.VISIBLE);
            for (int i =0; i< numOfSelect; i++)
                recyclerAdapter.addSeletedContact(contactItems.get(preSelectList[i]), i);
        }
    }
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.select_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerAdapter = new SelectedProfileRecyclerAdapter(this, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(SpacesItemDecoration.RecyclerViewOrientation.LINEAR_HORIZONTAL, 10));
        listView = (ListView) findViewById(R.id.select_listview);
        listviewAdapter = new SelectContactsAdapter(contactItems, getApplicationContext());
        setSearchView();
        listView.setTextFilterEnabled(true);
        listView.setAdapter(listviewAdapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactItem item = listviewAdapter.getItem(position);
                boolean checked = listviewAdapter.getOriginalItemChecked(item.getId());
                if (checked)
                    numOfSelect--;
                else
                    numOfSelect++;

                setCountToolbarTitle();
                item.setChecked(!checked);
                listviewAdapter.setOriginalItemChecked(item.getId(), !checked);
                Log.d("listPosition", position + " " + !checked);
                onStateChange(!checked, listviewAdapter.getRealItemId(position));
                listviewAdapter.notifyDataSetChanged();
            }
        });
        setRecyclerViewFist();

    }
    private void setCountToolbarTitle() {
        if (numOfSelect > 0){
            toolbarTitle.setVisibility(View.VISIBLE);
            String numb = "<font color='#444444'>" + String.format(" %d", numOfSelect) + "</font>";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                toolbarTitle.setText(Html.fromHtml(numb, Html.FROM_HTML_MODE_LEGACY));
            } else
                toolbarTitle.setText(Html.fromHtml(numb));
        } else {
            toolbarTitle.setVisibility(View.GONE);
        }
    }

    private void setSearchView() {
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("search");
        searchView.setBackgroundResource(R.drawable.bg_rounded_bgcolor);
        setSearchViewColor(searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText))
                    listView.clearTextFilter();
                else
                    listView.setFilterText(newText);
                return true;
            }
        });
    }

    private void setSearchViewColor(SearchView searchView) {
        LinearLayout ll = (LinearLayout) searchView.getChildAt(0);
        LinearLayout ll2 = (LinearLayout) ll.getChildAt(2);
        LinearLayout ll3 = (LinearLayout) ll2.getChildAt(1);

        SearchView.SearchAutoComplete autoComplete = ((SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text));
        ImageView searchCloseButton = (ImageView) ll3.getChildAt(1);
        //ImageView searchIcon = (ImageView) searchView.findViewById(android.R.id.search_mag_icon);
        ImageView labelView = (ImageView) ll.getChildAt(1);
        //autoComplete.setTextSize(TypedValue.COMPLEX_UNIT_PX, AppUtility.getAppinstance().dpToPx(18));
        autoComplete.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        autoComplete.setTextColor(getResources().getColor(R.color.colorPrimary));

        //searchIcon.getDrawable().setColorFilter(Color.parseColor("#858585"), PorterDuff.Mode.MULTIPLY);
        searchCloseButton.getDrawable().setColorFilter(Color.parseColor("#858585"), PorterDuff.Mode.MULTIPLY);
        labelView.getDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     *
     * recyclerview animation이 이상해서 주석처리
     */
    /*

    public void slideUp(){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                recyclerView.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        recyclerView.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                recyclerView.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        recyclerView.startAnimation(animate);
    }*/
    private void changeInvitedView() {
        if (numOfSelect == 1 && recyclerView.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.invited_frame).setVisibility(View.VISIBLE);
            //slideDown();
        } else if (numOfSelect == 0 && recyclerView.getVisibility() == View.VISIBLE){
            //slideUp();
            recyclerView.setVisibility(View.GONE);
            findViewById(R.id.invited_frame).setVisibility(View.GONE);
        }
    }


    /**
     *
     * @param isTrue
     * @param id
     */
    private void onStateChange(boolean isTrue, int id) {
        changeInvitedView();

        if (isTrue) {
            recyclerAdapter.addSeletedContact(contactItems.get(id), numOfSelect);
        } else {
            recyclerAdapter.deleteSelectedContact(contactItems.get(id).getId());
        }
    }

    @Override
    public void onRemoved(int id) {
        numOfSelect--;
        setCountToolbarTitle();

        changeInvitedView();
        boolean isChecked = listviewAdapter.getOriginalItemChecked(id);
        listviewAdapter.setOriginalItemChecked(id, !isChecked);
        listviewAdapter.notifyDataSetChanged();
    }
}
