package com.example.arun.arrow;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.FadingCircle;

public class splash_screen extends AppCompatActivity {
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        YoYo.with(Techniques.Landing).playOn(findViewById(R.id.splash_layout));
        progressBar=(ProgressBar) findViewById(R.id.spin);
        FadingCircle threeBounce=new FadingCircle();
        progressBar.setIndeterminateDrawable(threeBounce);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(splash_screen.this,Security.class);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
                finish();
            }
        },2000);
    }
}
