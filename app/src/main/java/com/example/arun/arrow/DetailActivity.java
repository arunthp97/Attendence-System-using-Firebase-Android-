package com.example.arun.arrow;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class DetailActivity extends AppCompatActivity {
    private TextView textView1,textView2,textView3,textView4,textView5;
    private RelativeLayout relativeLayout;
    private android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        YoYo.with(Techniques.Landing).playOn(findViewById(R.id.lay));
        toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        relativeLayout=(RelativeLayout) findViewById(R.id.lay);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable1.setEnterFadeDuration(2000);
        animationDrawable1.setExitFadeDuration(4000);
        animationDrawable1.start();
        textView1=(TextView) findViewById(R.id.textview1);
        textView2=(TextView) findViewById(R.id.textview2);
        textView3=(TextView) findViewById(R.id.textview3);
        textView4=(TextView) findViewById(R.id.textview4);
        textView5=(TextView) findViewById(R.id.textview5);

        Intent intent=getIntent();
        String Name=intent.getExtras().getString("Name");
        String Roll_No=intent.getExtras().getString("Roll_No");
        String Branch=intent.getExtras().getString("Branch");
        String Gender=intent.getExtras().getString("Gender");
        String Time=intent.getExtras().getString("Time");
        textView1.setText(Name);
        textView2.setText(Roll_No);
        textView3.setText(Branch);
        textView4.setText(Gender);
        textView5.setText(Time);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
        {

        }
        else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
        {

        }
    }
}
