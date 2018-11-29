package com.example.srini.nutrivisai;


import java.io.File;
import java.util.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
<<<<<<< HEAD
import android.widget.Toast;

import java.io.IOException;
=======
>>>>>>> broken_imports
import java.util.ArrayList;
import com.google.firebase.firestore.*;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity implements AsyncRequester{
    private final Context context = this;
    private static final int RC_SIGN_IN = 123;

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD

        //try{ Thread.sleep(10000); } catch (Exception e){ e.printStackTrace(); }

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
=======
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

//        if(savedInstanceState.containsKey("photoUri")){
//            Log.d("___URI",savedInstanceState.get("photoUri").toString() );
//        }
        if (mFirebaseUser == null){
            //Not signed in, launch the Sign In Activity
            startActivity(new Intent(this, AuthUiActivity.class));
            finish();
            return;
        }else {
            String mUsername = mFirebaseUser.getDisplayName();
            String mEmailAddress = mFirebaseUser.getEmail();

            Log.d("__login user", mUsername);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setTimestampsInSnapshotsEnabled(true)
                    .build();
            db.setFirestoreSettings(settings);

>>>>>>> broken_imports

//        if (mFirebaseUser == null){
//            //Not signed in, launch the Sign In Activity
//            startActivity(new Intent(this, AuthUiActivity.class));
//            finish();
//            return;
//        }else {
//            String mUsername = mFirebaseUser.getDisplayName();
//            String mEmailAddress = mFirebaseUser.getEmail();
//            Log.d("__login user", mUsername);
//        }
        try{
            Log.d("______RES", savedInstanceState.get("docs").toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.lv);

        final ArrayList<Food> listItems = new ArrayList<Food>();

        String url = "https://cdn.cnn.com/cnnnext/dam/assets/171027052520-processed-foods-exlarge-tease.jpg";
        listItems.add(new Food("Pizza",30.0,2000.0,url));
        listItems.add(new Food("Pasta",20.0,1000.0,url));

        CustomAdapter adapter = new CustomAdapter(this,listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent createEventIntent = new Intent(context, DetailActivity.class);

                if(listItems.size()>1) {
                    Food f = listItems.get(position);
                    createEventIntent.putExtra("name",f.getName());
                    createEventIntent.putExtra("cal",f.getCalorieCount()+"g");
                    createEventIntent.putExtra("fat",f.getFatContent()+"g");
                    createEventIntent.putExtra("url",f.getURL());
                    createEventIntent.putExtra("date",f.getDateScanned());
                    createEventIntent.putExtra("servings",f.getServings());
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

                i.putExtra("user", mFirebaseAuth.getCurrentUser());
                //DataManagement dm = new DataManagement(mFirebaseUser);
                //i.putExtra("dm",( new Gson().toJson(dm)));
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
        runTask();
    }
    //this method is called when asynctask is completed
    public void onCompletedTask(String str){
        //TODO do something with the string
        //Log.e("nutri call",  str);
    }
    public void onCompletedTask(Map responseVals){
        //TODO do something with the string
        Log.d("___QUERY_RESP",  responseVals.toString());
    }
    /*use this method to start the async request*/
    private void runTask(){
        Intent i = getIntent();
        String path = i.getStringExtra("imagePath");
        HashMap map = GVision.callGVis(path);
        Log.d("TAG", "Map of preds: " + Arrays.asList(map));
        String finalFood = ResolveFood.resolveFood(map, context);
        Log.d("TAG", "Final Food: " + finalFood);
        AsyncTask<String, Void, String> nutrition = new NutritionixTaskCall(this).execute(finalFood);
        try {
            String n = nutrition.get();
            if (n == null) {
                Toast.makeText(getApplicationContext(),"Invalid Food Scanned",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e ) {
            // e.printStackTrace(); // FOOD NOT IDENTIFIED
            Toast.makeText(getApplicationContext(),"Invalid Food Scanned",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
