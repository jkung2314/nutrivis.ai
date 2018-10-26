

import android.annotation.SuppressLint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Food {

    private String name;
    private double fatContent;
    private int calorieCount;
    private String dateScanned;

    @SuppressLint("NewApi")
    public Food(String foodName, double fatContent, int calorieCount){
        name = foodName;
        this.fatContent = fatContent;
        this.calorieCount = calorieCount;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.dateScanned = dtf.format(now);
    }

    public String toString(){
        return name + ": Fat: "+ fatContent + "g Calories: " + calorieCount + " Scanned on: " + dateScanned;
    }

    public String getName(){
        return name;
    }

    public double getFatContent(){
        return fatContent;
    }

    public int getCalorieCount(){
        return calorieCount;
    }

    public String getDateScanned(){
        return dateScanned;
    }
}

