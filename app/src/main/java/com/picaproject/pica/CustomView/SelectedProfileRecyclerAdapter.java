package com.picaproject.pica.CustomView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picaproject.pica.Activity.InviteMemberActivity;
import com.picaproject.pica.Item.ContactItem;
import com.picaproject.pica.Listener.SelectContactsRemoveListener;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class SelectedProfileRecyclerAdapter extends RecyclerView.Adapter<SelectedProfileRecyclerAdapter.ViewHolder> {
    private ArrayList<ContactItem> selectedContact;
    private SelectContactsRemoveListener listener;

    private InviteMemberActivity activity;
    public SelectedProfileRecyclerAdapter(InviteMemberActivity activity, SelectContactsRemoveListener listener){
        this.activity = activity;
        this.selectedContact = new ArrayList<>();
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invite_member, null);

        return new SelectedProfileRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ContactItem item = selectedContact.get(i);
        if (item.getName().equals(""))
            viewHolder.name.setText(item.getPhoneNumber());
        else
            viewHolder.name.setText(item.getName());


    }

    public void addSeletedContact(ContactItem contactItem, int numOfselect){
        selectedContact.add(contactItem);
        notifyItemInserted(selectedContact.size()-1);
        notifyItemChanged(numOfselect, selectedContact.size());
    }

    //해당 id부터 순서를 다시 리매핑
    private int findOrder(int id){
        int result = -1;
        for (int i =0; i<selectedContact.size(); i++){
            if (id == selectedContact.get(i).getId()) {
                result = i;
                break;
            }
        }
        return result;
    }

    private void printArraylist(){
        for (int i=0; i<selectedContact.size(); i++)
            Log.d("arraylist", i+" "+selectedContact.get(i).getId());
    }
    public void deleteSelectedContact(int id){
        int result = findOrder(id);
        /*
        int num = orderMap.get(id);
        orderMap.remove(id);
        reOrderMap(id+1);
        Log.d("remove map", id+" "+(num-1));*/

        Log.d("order", result+"");
        printArraylist();
        if (result >= 0) {
            deleteContact(result);
        }
    }

    private int deleteContact(int id){
        int result = selectedContact.get(id).getId();
        selectedContact.remove(id);
        notifyItemRemoved(id);
        notifyItemChanged(id, selectedContact.size());

        return result;
    }

    @Override
    public int getItemCount() {
        return selectedContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View close;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            close = itemView.findViewById(R.id.profile_remove);
            close.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.profile_name);
        }

        @Override
        public void onClick(View v) {
            int id = getAdapterPosition();
            int realId = deleteContact(id);
            listener.onRemoved(realId);
        }
    }
}
