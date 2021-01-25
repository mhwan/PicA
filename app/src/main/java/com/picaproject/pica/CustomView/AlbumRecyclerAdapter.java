package com.picaproject.pica.CustomView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.picaproject.pica.Activity.AlbumMainActivity;
import com.picaproject.pica.Item.AlbumItem;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.IntentProtocol;

import java.util.ArrayList;
import java.util.List;

public class AlbumRecyclerAdapter extends RecyclerView.Adapter<AlbumRecyclerAdapter.ViewHolder> {
    private FragmentActivity activity;
    private List<AlbumItem> itemList;
    public static final String KEY_EXTRA_ALBUM_ID = "Intents_from_ALBUM_ID_EXTRA";
    public AlbumRecyclerAdapter(FragmentActivity activity, List<AlbumItem> items) {
        this.activity = activity;
        this.itemList = items;

        if (itemList == null) {
            itemList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public AlbumRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album_card, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumRecyclerAdapter.ViewHolder viewHolder, int i) {
        final AlbumItem item = itemList.get(i);

        Glide.with(activity).load(item.getDefaultPicture())
                .error(R.drawable.img_sample)
                .override(600)
                .into(viewHolder.image);
        viewHolder.title.setText(item.getName());
        viewHolder.author.setText("by. "+item.getNickName());
        viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AlbumMainActivity.class);
                Log.d("passing_album_id", item.getAlbumId()+"");
                intent.putExtra(KEY_EXTRA_ALBUM_ID, item.getAlbumId());
                activity.startActivityForResult(intent, IntentProtocol.CREATE_MANAGE_ALBUM);
            }
        });
    }

    public void refreshAllItem(ArrayList<AlbumItem> items) {
        itemList.clear();
        itemList.addAll(items);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView author;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.background_image);
            title = (TextView) itemView.findViewById(R.id.album_name);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            author = (TextView) itemView.findViewById(R.id.album_author);
        }
    }
}
