package com.example.arun.arrow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivityFirst extends AppCompatActivity {
    private EditText editText1, editText2;
    private Button button;
    private TextView textView;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private Button button1;
    private ProgressDialog progressDialog;
    private static int PReqCode=1;
    private static int REQUESRCODE=1;
    private android.support.v7.widget.Toolbar toolbar;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);
        YoYo.with(Techniques.Landing).playOn(findViewById(R.id.relative));
        if (Build.VERSION.SDK_INT>=23)
        {
            checkAndRequestForPermission2();
            checkAndRequestForPermission1();
        }
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.textresetpassword);
        signInButton = findViewById(R.id.google);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        button1 = (Button) findViewById(R.id.register);
        progressBar = (ProgressBar) findViewById(R.id.spin);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        editText1 = (EditText) findViewById(R.id.useremail);
        editText2 = (EditText) findViewById(R.id.password);
        button = (Button) findViewById(R.id.Login);
        Wave threeBounce = new Wave();
        progressBar.setIndeterminateDrawable(threeBounce);
        firebaseAuth = FirebaseAuth.getInstance();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(MainActivityFirst.this);
                progressDialog.setMessage("Loading data...");
                progressDialog.setCanceledOnTouchOutside(false);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityFirst.this, Signup.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editText1.getText().toString().trim();
                String password = editText2.getText().toString().trim();

                if (username.isEmpty()) {
                    editText1.setError("Can't be empty");
                    editText1.requestFocus();
                    return;
                } else if (password.isEmpty()) {
                    editText2.setError("Can't be empty");
                    editText2.requestFocus();
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    button.setVisibility(View.INVISIBLE);
                    button1.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(editText1.getText().toString(), editText2.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    progressBar.setVisibility(View.GONE);
                                    button.setVisibility(View.VISIBLE);
                                    button1.setVisibility(View.VISIBLE);
                                    textView.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(MainActivityFirst.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Logged in successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    button.setVisibility(View.VISIBLE);
                                    button1.setVisibility(View.VISIBLE);
                                    textView.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "Please verify your email address.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                progressBar.setVisibility(View.GONE);
                                button.setVisibility(View.VISIBLE);
                                button1.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivityFirst.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.resetpasswordthroughemail, null);
                dialogBuilder.setView(dialogView);
                final EditText editText =(EditText)dialogView.findViewById(R.id.emailreset);
                final Button button1=(Button) dialogView.findViewById(R.id.resetpassword1);
                final Button button2=(Button) dialogView.findViewById(R.id.resetpassword2);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String emailAddress = editText.getText().toString().trim();
                        if (emailAddress.isEmpty())
                        {
                            editText.setError("Can't be empty.");
                            editText.requestFocus();
                            return;
                        }

                        firebaseAuth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(),"Check your email to reset your password.",Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();

                                        }
                                    }
                                });

                    }
                });


            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

    private void checkAndRequestForPermission1() {
        if(ContextCompat.checkSelfPermission(MainActivityFirst.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityFirst.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(getApplicationContext(),"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivityFirst.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }
        else
        {

        }
    }
    private void checkAndRequestForPermission2() {
        if(ContextCompat.checkSelfPermission(MainActivityFirst.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityFirst.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                Toast.makeText(getApplicationContext(),"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivityFirst.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUESRCODE);
            }
        }
        else
        {

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                progressDialog.show();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(MainActivityFirst.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Logged in successfully.", Toast.LENGTH_SHORT).show();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}


