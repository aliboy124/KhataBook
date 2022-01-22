package com.example.khatabook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            user.setName(extras.getString("name"));
            user.setEmail(extras.getString("email"));
            user.setPosBalance(extras.getInt("pos"));
            user.setNegBalance(extras.getInt("neg"));
            user.setPassword(extras.getString("password"));

            sender.setText(user.getEmail());
            sender.setEnabled(false);
        }


        receiver.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    
                }
            }
        });

//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });


    }
}