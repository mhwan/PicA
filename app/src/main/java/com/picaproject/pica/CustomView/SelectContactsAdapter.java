package com.picaproject.pica.CustomView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.picaproject.pica.Item.ContactItem;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class SelectContactsAdapter extends BaseAdapter implements Filterable {
    private ArrayList<ContactItem> contactItems;
    private Context context;
    //원본 주소록
    private ArrayList<ContactItem> origList;

    public SelectContactsAdapter(ArrayList<ContactItem> contactItems, Context context){
        this.contactItems = contactItems;
        this.origList = contactItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contactItems.size();
    }

    @Override
    public ContactItem getItem(int position) {
        return contactItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOriginalItemChecked(int id, boolean value){
        origList.get(id).setChecked(value);
    }

    public boolean getOriginalItemChecked(int id){
        return origList.get(id).isChecked();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewholder holder;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_select_contact, parent, false);
            holder = new Viewholder();
            holder.phNum_textview = (TextView) convertView.findViewById(R.id.select_phNumber);
            holder.userName_textview = (TextView) convertView.findViewById(R.id.select_name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.select_checkbox);

            convertView.setTag(holder);
        }
        else
            holder = (Viewholder)convertView.getTag();

        final ContactItem contactItem = contactItems.get(position);
        holder.phNum_textview.setText(contactItem.getPhoneNumber());
        holder.userName_textview.setText(contactItem.getName());

        holder.checkBox.setClickable(false);
        holder.checkBox.setFocusable(false);
        holder.checkBox.setChecked(contactItem.isChecked());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults return_filter = new FilterResults();
                final ArrayList<ContactItem> results = new ArrayList<ContactItem>();
                if (constraint != null){
                    if (origList != null && origList.size()>0){
                        for (final ContactItem c : origList){
                            if (c.getName().toLowerCase().contains(constraint.toString()))
                                results.add(c);
                            else if (c.getPhoneNumber().contains(constraint.toString()))
                                results.add(c);
                        }
                    }
                    return_filter.values = results;
                }
                return return_filter;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactItems = (ArrayList<ContactItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public int getRealItemId(int position) {
        return contactItems.get(position).getId();
    }

    private int getSelectedItemCount(){
        int num = 0;
        for (ContactItem contactItem : origList){
            if (contactItem.isChecked())
                num++;
        }
        return num;
    }

    public ArrayList<ContactItem> getAllCheckedItem(){
        if (getSelectedItemCount() <= 0)
            return null;
        ArrayList<ContactItem> array = new ArrayList<ContactItem>();
        for (int i = 0; i< origList.size(); i++){
            if (origList.get(i).isChecked())
                array.add(origList.get(i));
        }

        return array;
    }
    class Viewholder {
        TextView phNum_textview, userName_textview;
        CheckBox checkBox;
    }
}
