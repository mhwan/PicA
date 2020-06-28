package com.picaproject.pica.CustomView;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.picaproject.pica.Item.CommentItem;
import com.picaproject.pica.R;

import java.util.ArrayList;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {
    private ArrayList<CommentItem> commentList;
    private Context context;
    private int authorId;

    public CommentRecyclerAdapter(Context context, ArrayList<CommentItem> commentItems, int Author) {
        this.commentList = commentItems;
        this.context = context;
        this.authorId = Author;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list, null);
        return new CommentRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentItem item = commentList.get(position);

        StringBuilder sb = new StringBuilder();
        if (item.getTagId() != null)
            sb.append("<font color='#6fcfce'><i> @" + item.getTagId() + "</i></font> ");
        sb.append(item.getContents());

        holder.content.setText(Html.fromHtml(sb.toString()));
        holder.date.setText(item.getDate());
        holder.nickname.setText(item.getNickname());
        if (authorId == item.getAuthorId()) {
            holder.deleteIcon.setVisibility(View.VISIBLE);
        } else
            holder.deleteIcon.setVisibility(View.GONE);
    }

    public void resetCommentData(ArrayList<CommentItem> list) {
        commentList.clear();
        commentList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date;
        TextView content;
        ImageView deleteIcon;
        TextView nickname;
        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.comment_date);
            content = (TextView) itemView.findViewById(R.id.comment_content);
            deleteIcon = (ImageView) itemView.findViewById(R.id.remove_comment);
            nickname = (TextView) itemView.findViewById(R.id.commnet_nickname);

            deleteIcon.setOnClickListener(this::onClick);
        }
        @Override
        public void onClick(View v) {
            int id = getAdapterPosition();
            commentList.remove(id);
            notifyItemRemoved(id);
        }
    }
}
