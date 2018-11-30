package com.example.srini.nutrivisai;

import android.annotation.SuppressLint;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Food {

    private String name;
    private double fatContent;
    private double calorieCount;
    private String dateScanned;
    private String url;
    private double servings = 1.0;

    @SuppressLint("NewApi")
    public Food(String foodName, double fatContent, double calorieCount, String url){
        name = foodName;
        this.fatContent = fatContent;
        this.calorieCount = calorieCount;
        this.url = url;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.dateScanned = dtf.format(now);
    }

    public String toString(){
        return name + ": Fat: "+ fatContent + "g Calories: " + calorieCount + " Scanned on: " + dateScanned + " Displayed at: " + url;
    }

    public String getName(){
        return name;
    }
    public String getServings(){
        return String.valueOf(servings);
    }

    public double getFatContent(){
        return fatContent;
    }

    public double getCalorieCount(){
        return calorieCount;
    }

    public String getDateScanned(){
        return dateScanned;
    }

    public String getURL(){
        return url;
    }
    public void setUrl(String URL){
        this.url = URL;
    }
}

