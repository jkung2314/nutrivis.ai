package com.example.srini.nutrivisai;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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

        TextView food = (TextView) findViewById(R.id.textView15);
        TextView calorieCount = (TextView) findViewById(R.id.textView14);
        TextView date = (TextView) findViewById(R.id.textView16);
        TextView fatContent = (TextView) findViewById(R.id.textView2);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        EditText servingsView =  findViewById(R.id.servingsInput);

        servingsView.setText(servingsValue);
        //String fatStr = fatContent.getText().toString();
        //String calStr = calorieCount.getText().toString();
        //Log.e("same", calStr.substring(0, calStr.length()-1) + " " + fatStr.substring(0, fatStr.length()-1));
        servingsView.addTextChangedListener(new MyTextWatcher(
                Float.parseFloat(cal.substring(0, cal.length()-1)),
                Float.parseFloat(fat.substring(0, fat.length()-1)),
                calorieCount, fatContent, servingsView
        ));


        new DownloadImageTask(imageView).execute(url);
        food.setText(name);
        calorieCount.setText(cal);
        date.setText(scanned);
        fatContent.setText(fat);
        //updateServings(Integer.parseInt(fat), fatContent, Integer.parseInt(cal), calorieCount, Integer.parseInt(servingsValue));
    }
}
