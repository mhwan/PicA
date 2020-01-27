package com.picaproject.pica.CustomView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.picaproject.pica.Item.ContactItem;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class InvitedFriendsRecyclerAdapter extends RecyclerView.Adapter<InvitedFriendsRecyclerAdapter.ViewHolder> {
    private ArrayList<ContactItem> invitedList;
    private Context context;

    public InvitedFriendsRecyclerAdapter(ArrayList<ContactItem> invitedList, Context context){
        this.invitedList = invitedList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invited_friends, null);
        return new InvitedFriendsRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (invitedList.get(i).getName() == null || invitedList.get(i).getName() == "")
            viewHolder.name.setText(invitedList.get(i).getPhoneNumber());
        else
            viewHolder.name.setText(invitedList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return invitedList.size();
    }

    public void setInvitedList(ArrayList<ContactItem> invitedList){
        this.invitedList.clear();
        this.invitedList.addAll(invitedList);
    }

    public ArrayList getInvitedList(){
        return invitedList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ProfileIconView profile;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.friends_profile);
            name = (TextView) itemView.findViewById(R.id.friends_name);
        }
    }
}
