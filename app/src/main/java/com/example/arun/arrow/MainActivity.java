package com.example.arun.arrow;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DatabaseReference mRef;
    private DrawerLayout drawerLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FloatingActionButton floatingActionButton;
    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        YoYo.with(Techniques.Landing).playOn(findViewById(R.id.drawer));
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        AnimationDrawable animationDrawable1 = (AnimationDrawable) drawerLayout.getBackground();
        animationDrawable1.setEnterFadeDuration(2000);
        animationDrawable1.setExitFadeDuration(4000);
        animationDrawable1.start();
        AnimationDrawable animationDrawable2 = (AnimationDrawable) navigationView.getBackground();
        animationDrawable2.setEnterFadeDuration(2000);
        animationDrawable2.setExitFadeDuration(4000);
        animationDrawable2.start();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.pdf_viewer: {
                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.dashboard: {
                        Intent intent = new Intent(MainActivity.this, Dashboard.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.share: {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        String sharebody = "'Attendence System Using Face Recognition' This app and software was developed by Acropolis students as a minor project (Team members are Arun Kumar Thapa,Arshil Singh Bhatia,Arpit Jain and Akshada Godbole).\nhttps://drive.google.com/open?id=1YCKJ6ZvKjnYabY9K5aB1Z8YCtExyGvVD";
                        String sharesub = "Attendence System Using Face recognition";
                        intent.putExtra(Intent.EXTRA_SUBJECT, sharesub);
                        intent.putExtra(Intent.EXTRA_TEXT, sharebody);
                        intent.putExtra(Intent.EXTRA_TITLE, sharesub);
                        startActivity(Intent.createChooser(intent, "Share using"));
                        break;
                    }
                    case R.id.about_us: {
                        Intent intent = new Intent(MainActivity.this, About.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.sign_out: {
                        FirebaseAuth.getInstance().signOut();
                        mGoogleSignInClient.signOut();
                        Intent intent = new Intent(MainActivity.this, MainActivityFirst.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    }
                    case R.id.feedback: {
                        Intent intent = new Intent(MainActivity.this, Email.class);
                        startActivity(intent);
                        break;
                    }
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRef = FirebaseDatabase.getInstance().getReference().child("Data");
        mRef.keepSynced(true);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Alert");
                builder.setMessage("Are you sure you want to delete all records.");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRef.removeValue();
                        Toast.makeText(getApplicationContext(), "All Records Deleted Successfully.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        MenuItem item = menu.findItem(R.id.searchview);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                firebaseSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                firebaseSearch(s);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert: {
                openDialog();
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    private void firebaseSearch(String searchText) {
        Query firebaseSearchQuery = mRef.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Blog, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, ViewHolder>(Blog.class, R.layout.row, ViewHolder.class, firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Blog model, final int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getRoll_no());
                viewHolder.itemView.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRef.child(post_key).removeValue();
                    }
                });
                viewHolder.itemView.findViewById(R.id.cardview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("Name", getItem(position).getName());
                        intent.putExtra("Roll_No", getItem(position).getRoll_no());
                        intent.putExtra("Branch", getItem(position).getBranch());
                        intent.putExtra("Gender", getItem(position).getGender());
                        intent.putExtra("Time", getItem(position).getTime());
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), getItem(position).name, Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.itemView.findViewById(R.id.cardview).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        Blog blog = getItem(position);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.update, null);
                        dialogBuilder.setView(dialogView);
                        LinearLayout linearLayout1 = (LinearLayout) dialogView.findViewById(R.id.updatess);
                        AnimationDrawable animationDrawable1 = (AnimationDrawable) linearLayout1.getBackground();
                        animationDrawable1.setEnterFadeDuration(2000);
                        animationDrawable1.setExitFadeDuration(4000);
                        animationDrawable1.start();
                        final EditText editText1 = (EditText) dialogView.findViewById(R.id.edit1u);
                        final EditText editText2 = (EditText) dialogView.findViewById(R.id.edit2u);
                        final EditText editText3 = (EditText) dialogView.findViewById(R.id.edit3u);
                        final EditText editText4 = (EditText) dialogView.findViewById(R.id.edit4u);
                        final EditText editText5 = (EditText) dialogView.findViewById(R.id.edit5u);
                        editText1.setText(blog.getName());
                        editText2.setText(blog.getRoll_no());
                        editText3.setText(blog.getBranch());
                        editText4.setText(blog.getGender());
                        editText5.setText(blog.getTime());
                        final Button button1 = (Button) dialogView.findViewById(R.id.buttonu1);
                        final Button button2 = (Button) dialogView.findViewById(R.id.buttonu2);
                        dialogBuilder.setTitle("Update Details of " + blog.getName());
                        final AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Data").child(post_key);
                                String name = editText1.getText().toString().trim();
                                String roll_no = editText2.getText().toString().trim();
                                String branch = editText3.getText().toString().trim();
                                String gender = editText4.getText().toString().trim();
                                String time = editText5.getText().toString().trim();
                                if (name.isEmpty()) {
                                    editText1.setError("Can't be empty");
                                    editText1.requestFocus();
                                    return;
                                } else if (roll_no.isEmpty()) {
                                    editText2.setError("Can't be empty");
                                    editText2.requestFocus();
                                    return;
                                } else if (branch.isEmpty()) {
                                    editText3.setError("Can't be empty");
                                    editText3.requestFocus();
                                    return;
                                } else if (gender.isEmpty()) {
                                    editText4.setError("Can't be empty");
                                    editText4.requestFocus();
                                    return;
                                } else {
                                    Blog blog = new Blog(name, roll_no, branch, gender, time);
                                    databaseReference1.setValue(blog);
                                    Toast.makeText(getApplicationContext(), name + " is updated successfully.", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }
                        });
                        button2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        return false;
                    }
                });


            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }

    private void showAddDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.ad_student, null);
        dialogBuilder.setView(dialogView);
        final String key = mRef.push().getKey();
        LinearLayout linearLayout2 = (LinearLayout) dialogView.findViewById(R.id.adss);
        AnimationDrawable animationDrawable2 = (AnimationDrawable) linearLayout2.getBackground();
        animationDrawable2.setEnterFadeDuration(2000);
        animationDrawable2.setExitFadeDuration(4000);
        animationDrawable2.start();
        final EditText editText1 = (EditText) dialogView.findViewById(R.id.edit1a);
        final EditText editText2 = (EditText) dialogView.findViewById(R.id.edit2a);
        final EditText editText3 = (EditText) dialogView.findViewById(R.id.edit3a);
        final EditText editText4 = (EditText) dialogView.findViewById(R.id.edit4a);
        final EditText editText5 = (EditText) dialogView.findViewById(R.id.edit5a);
        final Button button1 = (Button) dialogView.findViewById(R.id.buttona);
        final Button button2 = (Button) dialogView.findViewById(R.id.buttonb);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText1.getText().toString().trim();
                String roll_no = editText2.getText().toString().trim();
                String branch = editText3.getText().toString().trim();
                String gender = editText4.getText().toString().trim();
                String time = editText5.getText().toString().trim();
                if (name.isEmpty()) {
                    editText1.setError("Can't be empty");
                    editText1.requestFocus();
                    return;
                } else if (roll_no.isEmpty()) {
                    editText2.setError("Can't be empty");
                    editText2.requestFocus();
                    return;
                } else if (branch.isEmpty()) {
                    editText3.setError("Can't be empty");
                    editText3.requestFocus();
                    return;
                } else if (gender.isEmpty()) {
                    editText4.setError("Can't be empty");
                    editText4.requestFocus();
                    return;
                } else {
                    Blog blog = new Blog(name, roll_no, branch, gender, time);
                    mRef.child(key).setValue(blog);
                    Toast.makeText(getApplicationContext(), name + " is added.", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }


            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<Blog, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, ViewHolder>(Blog.class, R.layout.row, ViewHolder.class, mRef) {
            @Override
            protected void populateViewHolder(final ViewHolder viewHolder, final Blog model, final int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getRoll_no());
                viewHolder.itemView.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRef.child(post_key).removeValue();
                    }
                });
                viewHolder.itemView.findViewById(R.id.cardview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("Name", getItem(position).getName());
                        intent.putExtra("Roll_No", getItem(position).getRoll_no());
                        intent.putExtra("Branch", getItem(position).getBranch());
                        intent.putExtra("Gender", getItem(position).getGender());
                        intent.putExtra("Time", getItem(position).getTime());
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), getItem(position).name, Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.itemView.findViewById(R.id.cardview).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        Blog blog = getItem(position);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.update, null);
                        dialogBuilder.setView(dialogView);
                        LinearLayout linearLayout1 = (LinearLayout) dialogView.findViewById(R.id.updatess);
                        AnimationDrawable animationDrawable1 = (AnimationDrawable) linearLayout1.getBackground();
                        animationDrawable1.setEnterFadeDuration(2000);
                        animationDrawable1.setExitFadeDuration(4000);
                        animationDrawable1.start();
                        final EditText editText1 = (EditText) dialogView.findViewById(R.id.edit1u);
                        final EditText editText2 = (EditText) dialogView.findViewById(R.id.edit2u);
                        final EditText editText3 = (EditText) dialogView.findViewById(R.id.edit3u);
                        final EditText editText4 = (EditText) dialogView.findViewById(R.id.edit4u);
                        final EditText editText5 = (EditText) dialogView.findViewById(R.id.edit5u);
                        editText1.setText(blog.getName());
                        editText2.setText(blog.getRoll_no());
                        editText3.setText(blog.getBranch());
                        editText4.setText(blog.getGender());
                        editText5.setText(blog.getTime());
                        final Button button1 = (Button) dialogView.findViewById(R.id.buttonu1);
                        final Button button2 = (Button) dialogView.findViewById(R.id.buttonu2);
                        dialogBuilder.setTitle("Update Details of " + blog.getName());
                        final AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Data").child(post_key);
                                String name = editText1.getText().toString().trim();
                                String roll_no = editText2.getText().toString().trim();
                                String branch = editText3.getText().toString().trim();
                                String gender = editText4.getText().toString().trim();
                                String time = editText5.getText().toString().trim();
                                if (name.isEmpty()) {
                                    editText1.setError("Can't be empty");
                                    editText1.requestFocus();
                                    return;
                                } else if (roll_no.isEmpty()) {
                                    editText2.setError("Can't be empty");
                                    editText2.requestFocus();
                                    return;
                                } else if (branch.isEmpty()) {
                                    editText3.setError("Can't be empty");
                                    editText3.requestFocus();
                                    return;
                                } else if (gender.isEmpty()) {
                                    editText4.setError("Can't be empty");
                                    editText4.requestFocus();
                                    return;
                                } else {
                                    Blog blog = new Blog(name, roll_no, branch, gender, time);
                                    databaseReference1.setValue(blog);
                                    Toast.makeText(getApplicationContext(), name + " is updated successfully.", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }
                        });
                        button2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        return false;
                    }
                });

            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Logout");
        builder.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut();
                Intent intent = new Intent(MainActivity.this, MainActivityFirst.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
