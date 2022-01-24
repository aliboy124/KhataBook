package com.example.khatabook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewTransaction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        Button submit = findViewById(R.id.submit);
        EditText sender = findViewById(R.id.senderr);
        EditText receiver = findViewById(R.id.receiver);
        EditText amount = findViewById(R.id.amountt);


        User user = new User();
        User userReceiver = new User();
        Transaction newTransaction = new Transaction();


        final Bundle[] extras = {getIntent().getExtras()};

        if (extras[0] != null) {
            user.setName(extras[0].getString("name"));
            user.setEmail(extras[0].getString("email"));
            user.setPosBalance(extras[0].getInt("pos"));
            user.setNegBalance(extras[0].getInt("neg"));
            user.setPassword(extras[0].getString("password"));

            sender.setText(user.getEmail());
            sender.setEnabled(false);
            if (sender.getText().toString().equals("")){
                finish();
                Toast.makeText(getApplicationContext(), "Please wait!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else {
            finish();
            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            return;
        }


        FirebaseAuth auth = FirebaseAuth.getInstance();
        final DataSnapshot[] dataSnapshot = new DataSnapshot[1];

        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        dbreference = dbreference.child("Users");

        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                 dataSnapshot[0] = snapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        receiver.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (!receiver.getText().toString().equals(""))
                    {
                        if(receiver.getText().toString().equals(user.getEmail()))
                        {
                            Toast.makeText(getApplicationContext(), "Sender and receiver can't be same!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        boolean bool = false;
                        if (dataSnapshot[0].exists()){
                            HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot[0].getValue();

                            for (String key : dataMap.keySet()){
                                Object data = dataMap.get(key);
                                HashMap<String, Object> userData = (HashMap<String, Object>) data;

                                String temp = userData.get("email").toString();
                                if (temp.equals(receiver.getText().toString()))
                                {
                                    userReceiver.setEmail(userData.get("email").toString());
                                    userReceiver.setName(userData.get("name").toString());
                                    userReceiver.setPassword(userData.get("password").toString());
                                    userReceiver.setNegBalance(Integer.parseInt(userData.get("negBalance").toString()));
                                    userReceiver.setPosBalance(Integer.parseInt(userData.get("posBalance").toString()));
                                    bool=true;
                                    break;
                                }
                            }
                        }

                        if(!bool){
                            Toast.makeText(getApplicationContext(), "\"" + receiver.getText().toString() + "\" do not exist in our database!", Toast.LENGTH_SHORT).show();
                            bool = false;
                            receiver.setText("");
                            userReceiver.setEmail(null);
                            userReceiver.setName(null);
                            userReceiver.setPassword(null);
                            userReceiver.setNegBalance(0);
                            userReceiver.setPosBalance(0);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Receiver email is required!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if(user.getEmail() != null)
                {
                    if(userReceiver.getEmail() != null)
                    {
                        if (!amount.getText().toString().equals(""))
                        {
                            try{
                                int transaction_amount = 0;

                                transaction_amount = Integer.parseInt(amount.getText().toString());
                                if(transaction_amount > 0){
                                    newTransaction.setAmount(transaction_amount);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Amount shouldn't be 0!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            catch (Exception e){
                                Toast.makeText(getApplicationContext(), "Amount invalid!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Transactions");

                            LocalDateTime dateTime = LocalDateTime.now();
                            newTransaction.setSender(user);
                            newTransaction.setReceiver(userReceiver);

                            newTransaction.setApproved(false);
                            newTransaction.setPaid(false);

                            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss");
                            String formatDateTime = dateTime.format(format);
                            newTransaction.setTime(formatDateTime);
                            database.child(formatDateTime).setValue(newTransaction);
                            Toast.makeText(getApplicationContext(), "Transaction created!", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Amount required!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Could not found receiver email!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Could not found sender user!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}