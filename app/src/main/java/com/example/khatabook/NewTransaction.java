package com.example.khatabook;

import androidx.appcompat.app.AppCompatActivity;

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






        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {









                





            }
        });


    }
}