package com.example.srini.nutrivisai;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class MyTextWatcher implements TextWatcher {
    //private float baseCal, baseFat;
    //private TextView cal, fat;
    //private EditText servings;
    private DetailActivity detailPointer;

    /*public MyTextWatcher(float baseCal, float baseFat, TextView calView, TextView fatView, EditText servText){
        this.baseCal = baseCal;
        this.baseFat = baseFat;
        this.cal = calView;
        this.fat = fatView;
        servings = servText;
    }*/
    public MyTextWatcher(DetailActivity da){
        detailPointer = da;
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {  }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void afterTextChanged(Editable s) {
        detailPointer.updateServings();
    }
        //fat.setText(Float.toString(baseFat * Float.parseFloat(servings.getText().toString())) + "g");
        //cal.setText(Float.toString(baseCal * Float.parseFloat(servings.getText().toString())) + "g");
        //cal.setText(Float.toString(Float.parseFloat(cal.substring(0,cal.length()-1)) * Float.parseFloat(ser) ) + cal.substring(cal.length()-1));
        //Log.e("uy", "DEBUG AFETTEXTCHANGED: " + fat.substring(0,fat.length()-1) + " " + cal.substring(0,cal.length()-1) + " " + ser);

}
