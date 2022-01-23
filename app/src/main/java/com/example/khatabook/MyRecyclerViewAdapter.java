package com.example.khatabook;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    List<Transaction> transactionList;
    Activity mAct;
    User currentUser;

    public MyRecyclerViewAdapter(List<Transaction> transactionList, Activity mAct, User currentUser) {
        this.transactionList = transactionList;
        this.mAct = mAct;
        this.currentUser = currentUser;
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
        holder.emailT.setText(String.valueOf(holder.data.getReceiver().getEmail()));
        holder.amountT.setText(String.valueOf(holder.data.getAmount()));
        holder.timeT.setText(holder.data.getTime());
        holder.approvedT.setChecked(holder.data.isApproved());
        holder.paidT.setChecked(holder.data.isPaid());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Transactions");

        // if current user is receiver then show approval dialog
        if(!holder.data.isApproved() && holder.data.getReceiver().getEmail().equals(currentUser.getEmail()))
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Confirm Approval")
                        .setMessage("Are you sure you want to approve this transaction?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ref.child(holder.data.getTime()).child("approved").setValue(true);
                                Toast.makeText(v.getContext(), "Transaction Approved", Toast.LENGTH_SHORT).show();

                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.logo)
                        .show();
            }
        });
        // if current user is sender then show paid dialog
        if(holder.data.isApproved() &&
                !holder.data.isPaid() && holder.data.getSender().getEmail().equals(currentUser.getEmail()))
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Confirm Payment")
                            .setMessage("Are you sure you want to make this transaction paid?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ref.child(holder.data.getTime()).child("paid").setValue(true);
                                    Toast.makeText(v.getContext(), "Transaction Paid", Toast.LENGTH_SHORT).show();
                                }
                            })

                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.logo)
                            .show();
                }
            });
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
