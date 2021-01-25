package com.picaproject.pica.CustomView;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.picaproject.pica.Item.ReplyItem;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.AppUtility;
import com.picaproject.pica.Util.NetworkItems.DefaultResultItem;
import com.picaproject.pica.Util.NetworkUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {
    private ArrayList<ReplyItem> commentList;
    private FragmentActivity activity;
    private int authorId;

    public CommentRecyclerAdapter(FragmentActivity activity, ArrayList<ReplyItem> commentItems, int Author) {
        this.commentList = commentItems;
        this.activity = activity;
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
        ReplyItem item = commentList.get(position);


        StringBuilder sb = new StringBuilder();
        /*
        if (item.getTagId() != null)
            sb.append("<font color='#6fcfce'><i> @" + item.getTagId() + "</i></font> ");*/
        sb.append(item.getReplyText());

        holder.content.setText(Html.fromHtml(sb.toString()));
        holder.date.setText(item.getUploadDate());
        holder.nickname.setText(item.getNickName());
        if ("y".equals(item.getIsMyReply())) {
            holder.deleteIcon.setVisibility(View.VISIBLE);
        } else
            holder.deleteIcon.setVisibility(View.GONE);
    }

    private void removeCommentTask(int replyId, int listId) {
        NetworkUtility.getInstance().deletePictureReply(replyId, AppUtility.memberId, new Callback<DefaultResultItem>() {
            @Override
            public void onResponse(Call<DefaultResultItem> call, Response<DefaultResultItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DefaultResultItem item = response.body();

                    if (item.getCode() == NetworkUtility.APIRESULT.RESULT_SUCCESS) {
                        commentList.remove(listId);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(activity, "댓글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResultItem> call, Throwable t) {

            }
        });
    }
    public void resetCommentData(ArrayList<ReplyItem> list) {
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
            removeCommentTask(commentList.get(id).getReplyId(), id);
        }
    }
}
