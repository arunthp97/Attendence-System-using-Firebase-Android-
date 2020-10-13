package com.example.arun.arrow;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class Email extends AppCompatActivity {
    private EditText meditto;
    private EditText meditsubject;
    private EditText meditmessage;
    private Toolbar toolbar;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        YoYo.with(Techniques.Landing).playOn(findViewById(R.id.linear));
        linearLayout=findViewById(R.id.linear);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable1.setEnterFadeDuration(2000);
        animationDrawable1.setExitFadeDuration(4000);
        animationDrawable1.start();
        toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        meditto=(EditText) findViewById(R.id.editview1);
        meditsubject=(EditText) findViewById(R.id.editview2);
        meditmessage=(EditText) findViewById(R.id.editview3);
        Button button=(Button) findViewById(R.id.button_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }
    private void sendMail(){
        String recipientList=meditto.getText().toString();
        String[] recipients=recipientList.split(",");

        String subject=meditsubject.getText().toString();
        String message=meditmessage.getText().toString();

        Intent intent=new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto","",null));
        intent.putExtra(Intent.EXTRA_EMAIL,recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);

        startActivity(Intent.createChooser(intent,"send email..."));
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
        {

        }
        else if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
        {

        }
    }
}

