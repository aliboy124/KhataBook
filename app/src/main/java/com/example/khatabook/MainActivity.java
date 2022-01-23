package com.example.khatabook;

import java.util.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<Transaction> transactionList = new ArrayList<Transaction>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth auth;
        DatabaseReference dbreference;


        Button logout = findViewById(R.id.logout);
        Button add = findViewById(R.id.add);
        TextView nametextview = findViewById(R.id.name);
        TextView pos = findViewById(R.id.positive);
        TextView neg = findViewById(R.id.negative);
        Button showAll = findViewById(R.id.showall);
        Button showUnapproved = findViewById(R.id.showunapproved);
        Button showUnpaid = findViewById(R.id.showunpiad);

        User currentUser = new User();

        auth = FirebaseAuth.getInstance();

        dbreference = FirebaseDatabase.getInstance().getReference();
        dbreference = dbreference.child("Users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid());

        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String password = snapshot.child("password").getValue().toString();
                int negbalance = Integer.parseInt(snapshot.child("negBalance").getValue().toString()) ;
                int posbalance = Integer.parseInt(snapshot.child("posBalance").getValue().toString());

                currentUser.setEmail(email);
                currentUser.setName(name);
                currentUser.setPassword(password);
                currentUser.setNegBalance(negbalance);
                currentUser.setPosBalance(posbalance);

                nametextview.setText(currentUser.getName());
                pos.setText("Rs. " + currentUser.getPosBalance());
                neg.setText("Rs. " + currentUser.getNegBalance());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), NewTransaction.class);

                intent.putExtra("name",currentUser.getName());
                intent.putExtra("email",currentUser.getEmail());
                intent.putExtra("password",currentUser.getPassword());
                intent.putExtra("neg",currentUser.getNegBalance());
                intent.putExtra("pos",currentUser.getPosBalance());

                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                auth.signOut();

                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        //// setting transaction list using custom method call at the end and data from firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Transactions");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionList = new ArrayList<Transaction>();

                for (DataSnapshot dsp : snapshot.getChildren()) {
                    transactionList.add((dsp.getValue(Transaction.class)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setTransactionListAdapter();

        // show all button
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Transactions");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        transactionList = new ArrayList<Transaction>();

                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            transactionList.add((dsp.getValue(Transaction.class)));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                setTransactionListAdapter();
            }
        });

        // show unapproved and show unpaid buttons
        showUnapproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Transactions");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        transactionList = new ArrayList<Transaction>();

                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            if(!dsp.getValue(Transaction.class).isApproved())
                                transactionList.add((dsp.getValue(Transaction.class)));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                setTransactionListAdapter();
            }
        });

        showUnpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Transactions");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        transactionList = new ArrayList<Transaction>();

                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            if(!dsp.getValue(Transaction.class).isPaid())
                                transactionList.add((dsp.getValue(Transaction.class)));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                setTransactionListAdapter();
            }
        });

    }

    public void setTransactionListAdapter(){
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyRecyclerViewAdapter(transactionList,MainActivity.this);
        recyclerView.setAdapter(adapter);
    }
}