package com.example.srini.nutrivisai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.searchView);
        String suggestWord = "Search for Scanned Items";
        searchView.setQueryHint(suggestWord);
        searchView.clearFocus();

        ListView listView = findViewById(R.id.listView);
        List<String> listItems = new ArrayList<>();
        listItems.add("No Items Scanned");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);

        listView.setAdapter(adapter);

    }
}
