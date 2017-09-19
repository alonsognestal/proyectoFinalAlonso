package com.example.proyectofinalalonso;

import android.app.Activity;
import android.content.Intent;
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

import butterknife.ButterKnife;

import static com.example.proyectofinalalonso.Aplicacion.PLAY_SERVICES_RESOLUTION_REQUEST;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Alonso on 02/09/2017.
 */

public class ListadoRadiosActivity extends Fragment {
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
        View rootView = inflater.inflate(R.layout.activity_listado_radios, container, false);
        Bundle bundle = this.getArguments();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(),1);
        String textoGenero = bundle.getString("textoGenero");
        if (!comprobarGooglePlayServices()) {
            Toast.makeText(getActivity(), "Error, Google Play Services no está instalado o no es válido", Toast.LENGTH_LONG);
        }
        ButterKnife.bind(getActivity());
        Aplicacion app = new Aplicacion(textoGenero);
        //app = (Aplicacion) getApplicationContext();
        app.setGenero(textoGenero);
        query = app.obtenerReferenciaDatabaseEmisoras();
        databaseReference = app.getItemsRadioReference();
        AdaptadorListadoRadios adapter = new AdaptadorListadoRadios(R.layout.content_listado_radios,getApplicationContext(), databaseReference.orderByChild("Categoria").equalTo(textoGenero));
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
