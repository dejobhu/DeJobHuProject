package com.dejobhu.skhu.dejobhu.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dejobhu.skhu.dejobhu.R;
import com.dejobhu.skhu.dejobhu.models.CommentItem;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<CommentItem> arrayList;
    Activity activity;

    public CommentAdapter(Activity activity,ArrayList<CommentItem> arrayList)
    {
        this.activity=activity;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommentViewHolder(activity.getLayoutInflater().inflate(R.layout.comment_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        CommentViewHolder item=(CommentViewHolder)viewHolder;

        item.time.setText(arrayList.get(i).getTime());
        item.name.setText(arrayList.get(i).getName());
        item.comment.setText(arrayList.get(i).getComment());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView comment;
        TextView name;
        TextView time;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.comment=itemView.findViewById(R.id.comment_content);
            this.name=itemView.findViewById(R.id.comment_name);
            this.time=itemView.findViewById(R.id.comment_time);
        }
    }
}
