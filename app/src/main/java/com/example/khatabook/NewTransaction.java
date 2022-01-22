package com.example.khatabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewTransaction extends AppCompatActivity {


    public static <K, V> Map<K, V> copyMap(Map<K, V> original)
    {
        return new HashMap<>(original);
    }

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


        final Bundle[] extras = {getIntent().getExtras()};

        if (extras[0] != null) {
            user.setName(extras[0].getString("name"));
            user.setEmail(extras[0].getString("email"));
            user.setPosBalance(extras[0].getInt("pos"));
            user.setNegBalance(extras[0].getInt("neg"));
            user.setPassword(extras[0].getString("password"));

            sender.setText(user.getEmail());
            sender.setEnabled(false);
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final DataSnapshot dataSnapshot;

        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        dbreference = dbreference.child("Users");

        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                 dataSnapshot = snapshot;

                if (snapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();

                    for (String key : dataMap.keySet()){

                        Object data = dataMap.get(key);
                        HashMap<String, Object> userData = (HashMap<String, Object>) data;

                        String temp = userData.get("email").toString();
                        if ( temp == receiver.getText().toString())
                        {
                            userReceiver.setEmail(userData.get("email").toString());
                            userReceiver.setName(userData.get("name").toString());
                            userReceiver.setPassword(userData.get("name").toString());
                            userReceiver.setNegBalance(Integer.parseInt(userData.get("negBalance").toString()));
                            userReceiver.setPosBalance(Integer.parseInt(userData.get("posBalance").toString()));
                            break;
                        }
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        receiver.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (!receiver.getText().toString().equals(""))
                    {
                        if(receiver.getText().toString().equals(user.getEmail()))
                        {
                            Toast.makeText(getApplicationContext(), "Sender and receiver cant be same!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
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