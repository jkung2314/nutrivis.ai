package com.example.srini.nutrivisai;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class NutritionixParser {

    public static Food parse(String nutritionixOutput)
    {
        Food parsedFood=null;
        try{
            JSONObject jo = new JSONObject(nutritionixOutput);
            String name = jo.getJSONArray("foods").getJSONObject(0).get("food_name")+"";
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            double cal = Double.parseDouble(jo.getJSONArray("foods").getJSONObject(0).get("nf_calories")+"");
            double fat = Double.parseDouble(jo.getJSONArray("foods").getJSONObject(0).get("nf_total_fat")+"");
            parsedFood = new Food(name,fat,cal);
        }catch (JSONException j){
            Log.wtf("NutritionixParser","JSONException occurred while parsing to Food");
        }
        return parsedFood;
    }
}
