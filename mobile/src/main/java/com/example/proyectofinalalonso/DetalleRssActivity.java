package com.example.proyectofinalalonso;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.in;

/**
 * Created by Alonso on 15/09/2017.
 */

public class DetalleRssActivity extends AppCompatActivity {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        Intent intent = getIntent();
        String rss = intent.getStringExtra("rss");
        /*listview = (ListView)findViewById(R.id.lista);*/
        ArrayList<String> headlines = new ArrayList<>();
        ArrayList<String> links = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();

        ObtenerFeed getXML = new ObtenerFeed(rss);
        getXML.execute();
        headlines = getXML.getHeadlines();
        links = getXML.getLinks();
        description = getXML.getDescription();

        listView = (ExpandableListView) findViewById(R.id.lvExp);
        hashMap = initData(headlines, description);
        int numero = hashMap.size();
        Log.d("Número: ",Integer.toString(numero));
        //SystemClock.sleep(2000);
        listAdapter = new ExpandableListAdapter(this, headlines, hashMap, 0);
        //SystemClock.sleep(2000);
        listView.setAdapter(listAdapter);

        /*// Binding data
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2, links);*/

        /*setListAdapter(adapter);*/
        SystemClock.sleep(4000);
        //getListView().setTextFilterEnabled(true);
        listAdapter.notifyDataSetChanged();
    }

    private HashMap initData(ArrayList<String> headlines, ArrayList<String> links) {

        hashMap = new HashMap<>();
        int j = 0;
        for (String i : headlines) {
            hashMap.put(i, links.get(j));
            j++;
        }
        return hashMap;
    }
}