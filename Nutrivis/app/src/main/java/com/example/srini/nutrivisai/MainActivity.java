package com.example.srini.nutrivisai;


import java.util.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

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

    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabAdapter adapter;
    private TabLayout layout;


    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;
    String GOOGLE_TOS_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new TabAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        setUp(viewPager);



        setUpTabLayout();


        viewPager.setCurrentItem(1);

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
    //this method is called when asynctask is completed
    public void onCompletedTask(String str){
        //TODO do something with the string
        //Log.e("nutri call",  str);
    }
    /*use this method to start the async request*/
    private void runTask(){
        AsyncTask<String, Void, String> nutrition = new NutritionixTask(this).execute("taco");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                mFirebaseAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void setUp(ViewPager v) {
        layout = (TabLayout) findViewById(R.id.tabs);
        DashboardTab dt = new DashboardTab();
        WorkZonesTab wt = new WorkZonesTab();
        ProfileTab pt = new ProfileTab();


        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addTab(pt, "Profile");
        adapter.addTab(dt, "Dashboard");
        adapter.addTab(wt, "WorkZones");

        v.setAdapter(adapter);

        final CharSequence titles[] = {"Profile","Dashboard", "WorkZones"};


        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                //mTitle.setText(titles[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }
    // method
    private void setUpTabLayout() {
        layout = (TabLayout) findViewById(R.id.tabs);
        layout.setupWithViewPager(viewPager);

        final int[] icons = new int[] {
                R.drawable.eye,
                R.drawable.eye,
                R.drawable.eye
        };
        layout.getTabAt(0).setIcon(icons[0]);
        layout.getTabAt(1).setIcon(icons[1]);
        layout.getTabAt(2).setIcon(icons[2]);
    }


}
