package com.example.srini.nutrivisai;


import java.io.File;
import java.util.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements AsyncRequester{
    private final Context context = this;

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;
    private ListView listView;
    private final ArrayList<Food> foods= new ArrayList<Food>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

       //final ArrayList<Food> foods= new ArrayList<Food>();


        try{
            Bundle extras =  getIntent().getExtras();
            ArrayList docs = extras.getStringArrayList("docs");
            Log.d("__User's Data", docs.toString());

            for(Object s:docs){
                foods.add(StringtoFoodParser.stringToFood(s.toString()));
            }

        }catch (Exception e){
            Log.e("__User's Data", "FAILED TO FIND");
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.lv);



        CustomAdapter adapter = new CustomAdapter(this,foods);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent createEventIntent = new Intent(context, DetailActivity.class);

                if(foods.size()>0) {
                    Food f = foods.get(position);
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
        Food f;

        String path = i.getStringExtra("imagePath");
        Log.e("__UIR AND PATH", path + "   " + i.getStringExtra("uri"));


        HashMap map = GVision.callGVis(path);
        Log.d("TAG", "Map of preds: " + Arrays.asList(map));
        String finalFood = ResolveFood.resolveFood(map, context);
        Log.d("TAG", "Final Food: " + finalFood);
        AsyncTask<String, Void, String> nutrition = new NutritionixTaskCall(this).execute(finalFood);
        try {
            String n = nutrition.get();
            f = NutritionixParser.parse(n);





             //f = new Food("test_food", 50.0, 70, "NOSTRING" );
            DataManagement dm = new DataManagement(mFirebaseUser);
            dm.uploadPhotoToStorage(f, path);

        } catch (Exception e ) {

            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Invalid Food Scanned",Toast.LENGTH_SHORT).show();
        }
            // e.printStackTrace(); // FOOD NOT IDENTIFIED
            //Toast.makeText(getApplicationContext(),"Invalid Food Scanned",Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        foods.clear();
        super.onResume();
        try{
            Bundle extras =  getIntent().getExtras();
            ArrayList docs = extras.getStringArrayList("docs");
            Log.d("__User's Data", docs.toString());

            for(Object s:docs){
                foods.add(StringtoFoodParser.stringToFood(s.toString()));
            }

        }catch (Exception e){
            Log.e("__User's Data", "FAILED TO FIND");
            e.printStackTrace();
        }
        CustomAdapter adapter = new CustomAdapter(this,foods);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent createEventIntent = new Intent(context, DetailActivity.class);

                if(foods.size()>0) {
                    Food f = foods.get(position);
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
    }
}
