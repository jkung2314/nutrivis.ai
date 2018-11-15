package com.example.srini.nutrivisai;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class NutritionixTaskTest {

    private NutritionixTask task;

    @Before
    public void setUp() throws Exception {
        System.out.println("Beginning Nutritionix Unit + Integration Tests!");
    }

    @Test
    public void callNutritionix() throws IOException {
        task = new NutritionixTask();
        String result = task.getNutritionInfo("taco");
        assertNotNull(result);
    }

    @Test
    public void verifyNutritionixResult() throws IOException {
        NutritionixParser parser = new NutritionixParser();

        //Call Nutritionix and Parse result
        task = new NutritionixTask();
        String result = task.getNutritionInfo("Taco");
        Food parsedFood = parser.parse(result);

        //Verify result
        assertEquals("Taco", parsedFood.getName());
        assertEquals(9.95, parsedFood.getFatContent(), 0.01);
        assertEquals(210.12, parsedFood.getCalorieCount(), 0.01);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Finished Nutritionix Unit + Integration Tests!");
    }
}