package com.example.srini.nutrivisai;


import java.util.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements AsyncRequester{
    private final Context context = this;
    private static final int RC_SIGN_IN = 123;

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;
    String GOOGLE_TOS_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();




        if (mFirebaseUser == null){
            //Not signed in, launch the Sign In Activity
            startActivity(new Intent(this, AuthUiActivity.class));
            finish();
            return;
        }else {
            String mUsername = mFirebaseUser.getDisplayName();
            String mEmailAddress = mFirebaseUser.getEmail();
            Log.d("__login user", mUsername);


        }
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.lv);

        final ArrayList<String> listItems = new ArrayList<String>();

        listItems.add("No Items scanned yet");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent createEventIntent = new Intent(context, DetailActivity.class);

                if(listItems.size()>1) {
                    startActivity(createEventIntent);
                }
            }

        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setBackgroundColor(Color.BLUE);
        fab.setImageResource(R.drawable.mango);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context,CameraActivity.class);//change to camera activity
                i.putExtra("mUsername", mFirebaseUser.getDisplayName());
                startActivity(i);



            }
        });

        ImageButton signout = findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFirebaseAuth.signOut();
                Intent i = new Intent(context,AuthUiActivity.class);
                startActivity(i);



            }
        });

    }
    //this method is called when asynctask is completed
    public void onCompletedTask(String str){
        //TODO do something with the string
        //Log.e("nutri call",  str);
    }
    /*use this method to start the async request*/
    private void runTask(){
        AsyncTask<String, Void, String> nutrition = new NutritionixTaskCall(this).execute("taco");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



}
