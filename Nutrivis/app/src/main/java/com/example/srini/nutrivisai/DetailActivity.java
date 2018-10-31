package com.example.srini.nutrivisai;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        Intent i = getIntent();
       // String title = i.getStringExtra("title");
        //String url = i.getStringExtra("url");
        //String collection = i.getStringExtra("collection");
        //String artist = i.getStringExtra("artist");

        TextView food = (TextView) findViewById(R.id.textView15);
        TextView calorieCount = (TextView) findViewById(R.id.textView14);
        TextView date = (TextView) findViewById(R.id.textView16);
        TextView fatContent = (TextView) findViewById(R.id.textView2);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        String url = "https://cdn.cnn.com/cnnnext/dam/assets/171027052520-processed-foods-exlarge-tease.jpg";
        new DownloadImageTask(imageView).execute(url);
        food.setText("Hot Dog");
        calorieCount.setText("200");
        date.setText("10/22/18");
        fatContent.setText("10g");
    }
}
