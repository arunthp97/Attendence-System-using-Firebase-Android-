package com.example.arun.arrow;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Security extends AppCompatActivity {
    private Toolbar toolbar;
    private DatabaseReference firebaseAuth;
    private EditText editText;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        RelativeLayout relativeLayout=findViewById(R.id.relativeLayout);
        textView=findViewById(R.id.textsecurity);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable1.setEnterFadeDuration(2000);
        animationDrawable1.setExitFadeDuration(4000);
        animationDrawable1.start();
        editText=findViewById(R.id.password);
        button=findViewById(R.id.buttonsecurity);
        YoYo.with(Techniques.Pulse)
                .duration(700)
                .repeat(999)
                .playOn(findViewById(R.id.textsecurity));
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth=FirebaseDatabase.getInstance().getReference().child("Admin");
        firebaseAuth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Map<String,Object> map=(Map<String, Object>)ds.getValue();
                    Object names=map.get("code");
                    final String code=String.valueOf(names);
                    textView.setText("You are ready to go.");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String pass=editText.getText().toString().trim();
                            if (pass.equals(code))
                            {
                                Intent intent = new Intent(Security.this, MainActivityFirst.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Wrong Code",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
