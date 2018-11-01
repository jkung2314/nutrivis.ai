package com.example.srini.nutrivisai;


import java.util.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.media.MediaPlayer;
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
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context,CameraActivity.class);//change to camera activity
                i.putExtra("mUsername", mFirebaseUser.getDisplayName());
                startActivity(i);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
