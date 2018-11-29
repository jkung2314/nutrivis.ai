package com.example.srini.nutrivisai;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
//import android.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*This is the class of the Activity that gets called when the user selects
an item from the list.
 */

public class DetailActivity extends AppCompatActivity {
    private final Context context = this;
/*
    private void updateServings(int fatVal, TextView fatView, int calVal, TextView calView, int servings){
        fatView.setText(fatVal * servings);
        calView.setText(calVal * servings);
        //UPDATE THE DATAB
    }*/
    private String idUrl;
    //private SharedPreferences sharedPref;
    float baseFat, baseCal;
    private float servings;
    public void updateServings(){

        TextView fat = findViewById(R.id.textView2);
        TextView cal = findViewById(R.id.textView14);
        String newServings = ((EditText)findViewById(R.id.servingsInput)).getText().toString();
        if(newServings.length() > 0) {
            servings = Float.parseFloat(newServings);
            fat.setText(Float.toString(baseFat * servings) + "g");
            cal.setText(Float.toString(baseCal * servings) + "g");
        }
        //cal.setText(Float.toString(baseCal * Float.parseFloat(servings.getText().toString())) + "g");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String url = i.getStringExtra("url");
        String fat = i.getStringExtra("fat");
        String cal = i.getStringExtra("cal");
        String scanned = i.getStringExtra("date");
        String servingsValue = i.getStringExtra("servings");
        //grab url to use as id to store the data
        idUrl = url;
        //store the values for 1 serving. drop the "g" at the end
        baseFat = Float.parseFloat(fat.substring(0, fat.length()-1));
        baseCal = Float.parseFloat(cal.substring(0, cal.length()-1));
        servings = Float.parseFloat(servingsValue);

        TextView food = (TextView) findViewById(R.id.textView15);
        TextView calorieCount = (TextView) findViewById(R.id.textView14);
        TextView date = (TextView) findViewById(R.id.textView16);
        TextView fatContent = (TextView) findViewById(R.id.textView2);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        EditText servingsView =  findViewById(R.id.servingsInput);

        //servingsView.setText(servingsValue);
        //String fatStr = fatContent.getText().toString();
        //String calStr = calorieCount.getText().toString();
        //Log.e("same", calStr.substring(0, calStr.length()-1) + " " + fatStr.substring(0, fatStr.length()-1));
        servingsView.addTextChangedListener(new MyTextWatcher(this));

        new DownloadImageTask(imageView).execute(url);
        food.setText(name);
        //calorieCount.setText(cal);
        date.setText(scanned);
        //fatContent.setText(fat);
        //updateServings(Integer.parseInt(fat), fatContent, Integer.parseInt(cal), calorieCount, Integer.parseInt(servingsValue));
    }

    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        //get the saved value of servings
        servings = sharedPref.getFloat(idUrl, servings);

        //write to the field in detailactivity
        EditText serv = findViewById(R.id.servingsInput);
        serv.setText(Float.toString(servings));
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("debug", "detail debug: onRestart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("debug", "detail debug: onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("debug", "detail debug: onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.e("debug", "detail debug: onStop");
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(idUrl, servings);
        editor.apply();

    }

    protected void onDestroy(){
        super.onDestroy();
        Log.e("debug", "detail debug: onDestroy");
    }

}
