package com.example.proyectofinalalonso;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

import static android.R.attr.key;
import static com.example.proyectofinalalonso.Aplicacion.PLAY_SERVICES_RESOLUTION_REQUEST;
import static com.example.proyectofinalalonso.Aplicacion.listadoGlobalPodcasts;
import static com.example.proyectofinalalonso.Aplicacion.listadoinner;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Alonso on 02/09/2017.
 */

public class PodcastsPorPrograma extends Activity implements AsyncResponse,RecyclerViewClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter adapter;
    private Query query;
    ArrayList<ArrayList<String>> listado;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcasts_por_emisora);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        final String idPodcast = bundle.getString("idPodcast");
        final String imagen = bundle.getString("imagen");
        String rss = bundle.getString("rss");

        Integer i = 0;
        listado = new ArrayList<ArrayList<String>>();
        ArrayList<String> ownImages = new ArrayList<>();
        ArrayList<String> links = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();
        Aplicacion.listadoGlobalPodcasts = new ArrayList<ArrayList<String>>();
        listadoinner= new ArrayList<String>();
        if (!Aplicacion.listadoGlobalPodcasts.isEmpty()) {
            Aplicacion.listadoGlobalPodcasts.clear();
        }
        if (!listadoinner.isEmpty()) {
            listadoinner.clear();
        }
        ObtenerPodcast getXML = new ObtenerPodcast(rss, getApplicationContext());
        getXML.execute();

        if (!comprobarGooglePlayServices()) {
            Toast.makeText(this, "Error, Google Play Services no está instalado o no es válido", Toast.LENGTH_LONG);
        }
        ButterKnife.bind(this);
        while (Aplicacion.haAcabadoHiloSecundario == false) {

        }
        AdaptadorPodcastsPorPrograma adapter = new AdaptadorPodcastsPorPrograma(getApplicationContext(), listadoGlobalPodcasts,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void recyclerViewListClicked(View v, int position){

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean comprobarGooglePlayServices() {
        try {

            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    finish();
                }
                return false;
            }
            return true;
        }catch (Exception ex)
        {
            return false;
        }
    }

    //Aquí recibo el resultado del onPostExecute() del hilo secundario
    @Override
    public void processFinish(ArrayList<ArrayList<String>> outer) {
        listado = outer;
    }
}
