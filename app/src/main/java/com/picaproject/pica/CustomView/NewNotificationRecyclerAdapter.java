package com.picaproject.pica.CustomView;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.picaproject.pica.Item.NotificationItem;
import com.picaproject.pica.Listener.NotificationRemoveListener;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class NewNotificationRecyclerAdapter extends RecyclerView.Adapter<NewNotificationRecyclerAdapter.ViewHolder> {
    private ArrayList<NotificationItem> notificationItems;
    private boolean removable;
    private NotificationRemoveListener listener;
    private Context context;
    public NewNotificationRecyclerAdapter(Context context, ArrayList<NotificationItem> notificationItems, boolean removable, NotificationRemoveListener listener){
        this.notificationItems = notificationItems;
        this.removable = removable;
        this.listener = listener;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_alarm_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        NotificationItem item = notificationItems.get(i);
        String build = "<font color='#6fcfce'>" + item.getNickname() + "</font>"+"님이 "+item.getContent();
        viewHolder.content.setText(Html.fromHtml(build));
        viewHolder.date.setText(item.getDate());

        if (removable){
            viewHolder.bg.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightWhiteGray));
        }
    }


    @Override
    public int getItemCount() {
        return notificationItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView content;
        TextView date;
        ImageView deleteIcon;
        RelativeLayout bg;
        public ViewHolder(View itemView){
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.alarm_content);
            date = (TextView) itemView.findViewById(R.id.alarm_date);
            deleteIcon = (ImageView) itemView.findViewById(R.id.remove_alarm);
            bg = itemView.findViewById(R.id.bg);
            if (removable == false)
                deleteIcon.setVisibility(View.GONE);
            else {
                deleteIcon.setVisibility(View.VISIBLE);
                deleteIcon.setOnClickListener(this);
            }

        }
        @Override
        public void onClick(View v) {
            int id = getAdapterPosition();
            notificationItems.remove(notificationItems.get(id));
            notifyItemRemoved(id);

            listener.onRemoved(getItemCount());
        }
    }
}
