package com.kiducar.kiducation.kiducar;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView img = (ImageView)findViewById(R.id.carImg);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);
        img.setAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }
}
