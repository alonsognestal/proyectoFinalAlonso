package com.example.proyectofinalalonso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.ButterKnife;

import static com.example.proyectofinalalonso.Aplicacion.PLAY_SERVICES_RESOLUTION_REQUEST;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Alonso on 20/08/2017.
 */

public class NewsActivity extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter adapter;
    private Query query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        View rootView = inflater.inflate(R.layout.activity_news, container, false);
        Intent intent = getActivity().getIntent();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(),1);
        String textoGenero = intent.getStringExtra("textoGenero");
        if (!comprobarGooglePlayServices()) {
            Toast.makeText(getActivity(), "Error, Google Play Services no está instalado o no es válido", Toast.LENGTH_LONG);
        }
        ButterKnife.bind(getActivity());
        Aplicacion app = new Aplicacion();

        databaseReference = app.obtenerEmisorasConRss();

        AdaptadorNews adapter = new AdaptadorNews(R.layout.content_news, getApplicationContext(), databaseReference);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private boolean comprobarGooglePlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                getActivity().finish();
            }
            return false;
        }
        return true;
    }
}


