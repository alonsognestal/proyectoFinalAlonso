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
import static com.example.proyectofinalalonso.Aplicacion.getAppContext;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Alonso on 20/08/2017.
 */

public class ListadoPodcastsPorRadio2 extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter adapter;
    private Query query;
    ArrayList<String> listado;
    Object podcast1 = new Object();
    HashMap<String,Object> podcast2 = new HashMap<String,Object>();
    HashMap<String,Object> podcast3 = new HashMap<String,Object>();
    HashMap<String,Object> podcast4 = new HashMap<String,Object>();
    Integer cont = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_listado_radios_con_podcast, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1); //2 columnas
        Bundle bundle = this.getArguments();
        final String idEmisora = bundle.getString("idEmisora");
        final String imagen = bundle.getString("imagen");
        String URLAudio = bundle.getString("URLAudio");
        String rss = "";
        final String genero = bundle.getString("genero");
        Integer i = 0;
        HashMap<String, Object> listadoPodcasts = (HashMap<String, Object>) bundle.getSerializable("podcast");
        for (Object key : listadoPodcasts.keySet()) {

            podcast1 = listadoPodcasts.get(key);
            cont++;
        }

        listado = new ArrayList<String>();
        if (!comprobarGooglePlayServices()) {
            Toast.makeText(getActivity(), "Error, Google Play Services no está instalado o no es válido", Toast.LENGTH_LONG);
        }
        ButterKnife.bind(getActivity());
        Aplicacion app = (Aplicacion) getActivity().getApplicationContext();
        //Obtengo los elementos de la referencia de la base de datos
        query = app.obtenerReferenciaEmisoras();
        databaseReference = app.getItemsRadioReference();
        //Se los paso al adaptador para que los muestre
        AdaptadorPodcastsPorRadio2 adapter = new AdaptadorPodcastsPorRadio2(getAppContext(), podcast1);
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
