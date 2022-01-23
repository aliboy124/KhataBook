package com.example.khatabook;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    List<Transaction> transactionList;
    Activity mAct;

    public MyRecyclerViewAdapter(List<Transaction> transactionList, Activity mAct) {
        this.transactionList = transactionList;
        this.mAct = mAct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transactionlistitem,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.data = transactionList.get(position);
        holder.nameT.setText(String.valueOf(holder.data.getReceiver().name));
        holder.emailT.setText(String.valueOf(holder.data.getReceiver()));
        holder.amountT.setText(String.valueOf(holder.data.getAmount()));
        //holder.timeT.setText(String.valueOf(holder.data.getTime()));
        holder.approvedT.setChecked(holder.data.isApproved());
        holder.paidT.setChecked(holder.data.isPaid());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nameT;
        TextView emailT;
        TextView amountT;
        TextView timeT;
        CheckBox approvedT;
        CheckBox paidT;
        Transaction data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameT = itemView.findViewById(R.id.nameT);
            emailT = itemView.findViewById(R.id.emailT);
            amountT = itemView.findViewById(R.id.amountT);
            timeT = itemView.findViewById(R.id.timeT);
            approvedT = itemView.findViewById(R.id.approvedT);
            paidT = itemView.findViewById(R.id.paidT);
        }
    }
}
