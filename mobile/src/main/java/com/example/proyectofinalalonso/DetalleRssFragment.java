package com.example.proyectofinalalonso;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alonso on 15/09/2017.
 */

public class DetalleRssFragment extends Fragment {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, String> hashMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_rss, container, false);

        Bundle bundle = this.getArguments();
        String rss = bundle.getString("rss");

        ArrayList<String> headlines = new ArrayList<>();
        ArrayList<String> links = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();

        ObtenerFeed getXML = new ObtenerFeed(rss);
        getXML.execute();
        headlines = getXML.getHeadlines();
        links = getXML.getLinks();
        description = getXML.getDescription();

        listView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
        listAdapter = new ExpandableListAdapter(getActivity(), headlines, description);
        listView.setAdapter(listAdapter);
        SystemClock.sleep(1000);
        listAdapter.notifyDataSetChanged();
        return rootView;
    }
    @Override
    public void onResume() {
        RadioFragment detalleFragment = (RadioFragment) getFragmentManager().findFragmentById(R.id.fragment_detalle);
        if (detalleFragment == null) {
            ((MainActivity) getActivity()).mostrarElementos(false);
        }
        super.onResume();
    }

}