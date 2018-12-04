package com.example.srini.nutrivisai;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResolveFood {
    private Map<String, Integer> validFoods;
    ResolveFood(Context con){
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getAssets().open("foods.txt")));

            String line;
            validFoods = new HashMap<String, Integer>();// it should be static - whereever you define
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=")) {
                    String[] strings = line.split("=");
                    validFoods.put(strings[0], Integer.parseInt(strings[1]));
                    i++;
                }
            }
            log("Num of foods = " + i);
        } catch (IOException e){
            e.printStackTrace();
        }


    }
    public void log(String m){
        Log.d("___resolve", m);
    }
    public String resolveFood(HashMap<String, Float> map) {
        HashMap<String, Float> newMap = sortByValues(map);

        ArrayList list = new ArrayList(newMap.keySet());
        for (int i = list.size() - 1; i >= 0; i--) {
            String key = (String) list.get(i);
            log( "KeyLISTSET: " + key + ", Value: " + newMap.get(key));
            if(isFood(key)) {
                return key;
            } else {
                continue;
            }
        }
        return null;
    }

    private boolean isFood(String food) {
        return validFoods.containsKey(food);
    }

    private static HashMap<String, Float> sortByValues(HashMap<String, Float> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        HashMap<String, Float> sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Float> entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}
