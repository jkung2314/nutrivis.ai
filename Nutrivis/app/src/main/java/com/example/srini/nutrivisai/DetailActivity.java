package com.example.srini.nutrivisai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private final Context context = this;

    private String idUrl;
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
        idUrl = url;
        baseFat = Float.parseFloat(fat.substring(0, fat.length()-1));
        baseCal = Float.parseFloat(cal.substring(0, cal.length()-1));
        servings = Float.parseFloat(servingsValue);

        TextView food = (TextView) findViewById(R.id.textView15);
        TextView calorieCount = (TextView) findViewById(R.id.textView14);
        TextView date = (TextView) findViewById(R.id.textView16);
        TextView fatContent = (TextView) findViewById(R.id.textView2);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        EditText servingsView =  findViewById(R.id.servingsInput);
        servingsView.addTextChangedListener(new MyTextWatcher(this));

        new DownloadImageTask(imageView).execute(url);
        food.setText(name);
        date.setText(scanned);
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
