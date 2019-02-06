package com.dejobhu.skhu.dejobhu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionViewHolder> {


    ArrayList<Question> mData;




    @androidx.annotation.NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.questioncard,null);

        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull QuestionViewHolder holder, int position) {
        holder.body.setText(mData.get(position).getBody());
        holder.ID.setText(mData.get(position).getName());
        holder.Time.setText(mData.get(position).getTimestep());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
class QuestionViewHolder extends RecyclerView.ViewHolder {
    TextView body;
    TextView ID;
    TextView Time;

    public QuestionViewHolder(@NonNull View itemView) {
        super(itemView);
        body=itemView.findViewById(R.id.question_title);
        ID=itemView.findViewById(R.id.question_id);
        Time=itemView.findViewById(R.id.question_time);
    }
}
