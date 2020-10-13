package com.example.arun.arrow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Signup extends AppCompatActivity {
    private ImageView ImgUserPhoto;
    private static int PReqCode=1;
    private static int REQUESRCODE=1;
    private android.support.v7.widget.Toolbar toolbar;
    private Uri pickedImgUri;
    private Uri imageUri;
    private FirebaseAuth firebaseAuth;
    private EditText editText1,editText2,editText3,editText4;
    private Button buttons;
    private ProgressBar progressBar;
    private Button buttons1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        YoYo.with(Techniques.FlipInY).playOn(findViewById(R.id.relative2));
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImgUserPhoto=findViewById(R.id.userimage);
        buttons1=(Button) findViewById(R.id.Goback);
        progressBar=(ProgressBar) findViewById(R.id.spin);
        RelativeLayout relativeLayout=(RelativeLayout) findViewById(R.id.relative2);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        editText1=findViewById(R.id.useremails);
        editText2=findViewById(R.id.passwords);
        editText3=findViewById(R.id.username);
        editText4=findViewById(R.id.passwords1);
        buttons=findViewById(R.id.Signup);
        firebaseAuth=FirebaseAuth.getInstance();
        buttons1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Signup.this,MainActivityFirst.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=editText1.getText().toString();
                String password=editText2.getText().toString();
                final String name=editText3.getText().toString();
                String password1=editText4.getText().toString();
                if (username.isEmpty() || name.isEmpty() || password.isEmpty()|| pickedImgUri==null)
                {
                    Toast.makeText(getApplicationContext(),"Verify all fields",Toast.LENGTH_SHORT).show();
                    buttons.setVisibility(View.VISIBLE);
                    buttons1.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
                else if (!password1.equals(password))
                {
                    Toast.makeText(getApplicationContext(),"Password Mismatch.",Toast.LENGTH_SHORT).show();
                    editText4.requestFocus();
                    return;
                }
                    else
                    {
                        buttons.setVisibility(View.INVISIBLE);
                        buttons1.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        firebaseAuth.createUserWithEmailAndPassword(editText1.getText().toString(),editText2.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    updateUserInfo(name,pickedImgUri,firebaseAuth.getCurrentUser());

                                }
                                else
                                {
                                    progressBar.setVisibility(View.GONE);
                                    buttons.setVisibility(View.VISIBLE);
                                    buttons1.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

            }
        });
        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=23)
                {
                    checkAndRequestForPermission();
                }
                else
                {
                    openGallery();
                }
            }
        });

    }



    private void openGallery() {
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESRCODE);
    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(Signup.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Signup.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(getApplicationContext(),"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(Signup.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }
        else
        {
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode == REQUESRCODE && data!=null)
        {
            pickedImgUri=data.getData();
            CropImage.activity(pickedImgUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode== RESULT_OK)
            {
                imageUri=result.getUri();
                ImgUserPhoto.setImageURI(imageUri);
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error=result.getError();
                Toast.makeText(getApplicationContext(), (CharSequence) error,Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentuser)
    {
        StorageReference mStorage=FirebaseStorage.getInstance().getReference().child("User_Photos");
        final StorageReference imageFilePath=mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        UserProfileChangeRequest profileUpdate=new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentuser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(),"Account is created, now verfy email to login.",Toast.LENGTH_SHORT).show();
                                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        FirebaseAuth.getInstance().signOut();
                                                        Intent intent=new Intent(Signup.this,MainActivityFirst.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        progressBar.setVisibility(View.GONE);
                                                        buttons.setVisibility(View.VISIBLE);
                                                        buttons1.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        progressBar.setVisibility(View.GONE);
                                                        buttons.setVisibility(View.VISIBLE);
                                                        buttons1.setVisibility(View.VISIBLE);
                                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });

                    }
                });



                    }
                });
            }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Signup.this,MainActivityFirst.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }
}
