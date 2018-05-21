package com.upc.help_system.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.upc.help_system.R;

public class StartActivity extends AppCompatActivity {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.activity_start);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        textView1 = (TextView) findViewById(R.id.text_1);
        textView2 = (TextView) findViewById(R.id.text_2);
        textView3 = (TextView) findViewById(R.id.text_3);
        textView4 = (TextView) findViewById(R.id.text_4);
        img = (ImageView) findViewById(R.id.imageView3);
        ObjectAnimator t1_animator = ObjectAnimator.ofFloat(textView1, "alpha", 1f, 0f, 1f);
        ObjectAnimator t2_animator = ObjectAnimator.ofFloat(textView2, "alpha", 1f, 0f, 1f);
        ObjectAnimator t3_animator = ObjectAnimator.ofFloat(textView3, "alpha", 1f, 0f, 1f);
        ObjectAnimator t4_animator = ObjectAnimator.ofFloat(textView4, "alpha", 1f, 0f, 1f);
        ObjectAnimator img_animator = ObjectAnimator.ofFloat(img, "alpha", 0f, 1f);
        ObjectAnimator imgx_animator = ObjectAnimator.ofFloat(img, "scaleX", 0.3f, 0.8f);
        ObjectAnimator imgy_animator = ObjectAnimator.ofFloat(img, "scaleY", 0.3f, 0.8f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(t1_animator, t2_animator, t3_animator,t4_animator);
        set.setDuration(2000);
        set.start();
        set.playTogether(img_animator, imgx_animator, imgy_animator);
        set.setDuration(2000);
        set.start();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */
                Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                StartActivity.this.startActivity(mainIntent);
                StartActivity.this.finish();
            }
        }, 4000); //2900 for release
    }
}
