import java.util.Date;

public class Food {

    private String name;
    private double fatContent;
    private int calorieCount;
    private Date dateScanned;

    public Food(String foodName, double fatContent, int calorieCount, Date dateScanned){
        name = foodName;
        this.fatContent = fatContent;
        this.calorieCount = calorieCount;
        this.dateScanned = dateScanned;
    }

    public String toString(){
        return name + "Fat: "+ fatContent + "g, Calories: " + calorieCount + " Scanned on: " + dateScanned;
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

    public Date getDateScanned(){
        return dateScanned;
    }
}
