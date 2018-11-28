package com.example.srini.nutrivisai;

import android.util.Log;

import java.io.File;
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
import java.util.Scanner;
import java.util.Set;

public class ResolveFood {

    public static String resolveFood(HashMap<String, Float> map) {
        HashMap<String, Float> newMap = sortByValues(map);

        ArrayList list = new ArrayList(newMap.keySet());
        for (int i = list.size() - 1; i >= 0; i--) {
            String key = (String) list.get(i);
            Log.d("TAG", "KeyLISTSET: " + key + ", Value: " + newMap.get(key));
            if(isFood(key)) {
                return key;
            } else {
                continue;
            }

        }
        return "Food not identified";
    }

    private static boolean isFood(String food) {

        // NEED AN ALGORITHM

        if (food.equals("banana")) {
            return true;
        } else {
            return false;
        }
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
