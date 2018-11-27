package com.example.srini.nutrivisai;

import android.util.Log;

public class StringtoFoodParser {

    public static Food stringToFood(String s) {
        Food food = null;
        try {
            String[] arr = s.split("\\s+");
            String name = arr[0].replace(":", "");
            double fat = Double.parseDouble(arr[2].replace("g", ""));
            double cal = Double.parseDouble(arr[4].replace("g", ""));
            String url = arr[11];
             food = new Food(name,fat,cal,url);

        } catch (Exception e) {
            Log.wtf("StringtoFoodParser", "Unformatted String was passed");
        }
        return food;
    }
}
