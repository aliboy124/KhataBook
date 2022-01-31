package com.example.khatabook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashfile);

        ImageView image = (ImageView)findViewById(R.id.splashimageview);
        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_left);
        image.startAnimation(anim1);

        TextView txt = (TextView)findViewById(R.id.splashtextview);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_right);
        txt.startAnimation(anim2);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,Signup.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }
}
