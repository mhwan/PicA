package com.picaproject.pica.CustomView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.picaproject.pica.Activity.AlbumMainActivity;
import com.picaproject.pica.Item.AlbumItem;
import com.picaproject.pica.R;

import java.util.List;

public class AlbumRecyclerAdapter extends RecyclerView.Adapter<AlbumRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<AlbumItem> itemList;

    public AlbumRecyclerAdapter(Context context, List<AlbumItem> items) {
        this.context = context;
        this.itemList = items;
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
        Drawable drawable = ContextCompat.getDrawable(context, item.getImage_id());
        viewHolder.image.setImageDrawable(drawable);
        viewHolder.title.setText(item.getAlbum_name());
        viewHolder.author.setText("by."+item.getAlbum_author());
        viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AlbumMainActivity.class));
            }
        });
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
