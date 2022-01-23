package com.example.khatabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;
    EditText emailAddress, password;
    Button signin, signup;
    ProgressBar p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddress = findViewById(R.id.EmailAddress);
        password = findViewById(R.id.PasswordText);
        signin = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        p = findViewById(R.id.progressBar2);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signup.class));
                finish();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String email = emailAddress.getText().toString();
                String passwordd = password.getText().toString();

                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";

                Pattern pat = Pattern.compile(emailRegex);

                if(TextUtils.isEmpty(email)){
                    emailAddress.setError("Email is required!");
                    return;
                }
                else {
                    if (!pat.matcher(email).matches()){
                        Toast.makeText(getApplicationContext(), "Invalid email!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(TextUtils.isEmpty(passwordd)){
                    password.setError("Password is required!");
                    return;
                }

                p.setVisibility(View.VISIBLE);
                signin.setVisibility(View.GONE);
                signup.setVisibility(View.GONE);

                auth.signInWithEmailAndPassword(email, passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            p.setVisibility(View.GONE);
                            signin.setVisibility(View.VISIBLE);
                            signup.setVisibility(View.VISIBLE);

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            p.setVisibility(View.GONE);
                            signin.setVisibility(View.VISIBLE);
                            signup.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Email or password is incorrect!", Toast.LENGTH_SHORT).show();
                            password.setText("");
                        }
                    }
                });
            }
        });


    }
}