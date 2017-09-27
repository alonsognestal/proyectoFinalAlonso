package com.example.proyectofinalalonso;

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

import static com.example.proyectofinalalonso.Aplicacion.PLAY_SERVICES_RESOLUTION_REQUEST;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Alonso on 02/09/2017.
 */

public class ListadoPodcastsPorRadio extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter adapter;
    private Query query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_podcasts_por_emisora, container, false);
        Bundle bundle = this.getArguments();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(),1);
        final String idEmisora = bundle.getString("idEmisora");
        final String imagen = bundle.getString("imagen");
        String URLAudio = bundle.getString("URLAudio");
        String rss = "";
        final String genero = bundle.getString("genero");
        Integer i = 0;
        HashMap<String, Object> listadoPodcasts = (HashMap<String, Object>) bundle.getSerializable("podcast");
        ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
        ArrayList<String> ownImages = new ArrayList<>();
        ArrayList<String> links = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        for (Object key : listadoPodcasts.keySet()) {
            rss = listadoPodcasts.get(key).toString();
            ObtenerPodcast getXML = new ObtenerPodcast(rss);
            getXML.execute();
            links = getXML.getLinks();
            outer.add(getXML.getLinks());
            descriptions = getXML.getDescription();
            outer.add(getXML.getDescription());
            ownImages = getXML.getOwnImages();
            outer.add(getXML.getOwnImages());
            durations = getXML.getDuration();
            outer.add(getXML.getDuration());
            titles = getXML.getTitle();
            outer.add(getXML.getTitle());
            images = getXML.getImages();
            outer.add(getXML.getImages());
            i++;
        }

        String textoGenero = bundle.getString("textoGenero");
        if (!comprobarGooglePlayServices()) {
            Toast.makeText(getActivity(), "Error, Google Play Services no está instalado o no es válido", Toast.LENGTH_LONG);
        }
        ButterKnife.bind(getActivity());
        Aplicacion app = new Aplicacion(textoGenero);
        //app = (Aplicacion) getApplicationContext();
        app.setGenero(textoGenero);
        query = app.obtenerReferenciaDatabaseEmisoras();
        databaseReference = app.getRadiosConPodcastReference();

        AdaptadorPodcastsPorRadio adapter = new AdaptadorPodcastsPorRadio(getApplicationContext(), outer);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return rootView;

    }

    @Override
    public void onResume() {
        RadioActivity detalleFragment = (RadioActivity) getFragmentManager().findFragmentById(R.id.fragment_detalle);
        if (detalleFragment == null) {
            ((MainActivity) getActivity()).mostrarElementos(false);
        }
        super.onResume();
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
