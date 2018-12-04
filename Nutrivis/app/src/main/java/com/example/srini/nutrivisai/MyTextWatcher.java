package com.example.srini.nutrivisai;

import android.text.Editable;
import android.text.TextWatcher;

public class MyTextWatcher implements TextWatcher {

    private DetailActivity detailPointer;

    public MyTextWatcher(DetailActivity da){
        detailPointer = da;
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {  }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() > 0 && Float.parseFloat(s.toString()) < 0 ){
            s.replace(0, 0, "1.0");
        }

        detailPointer.updateServings();
    }
}
