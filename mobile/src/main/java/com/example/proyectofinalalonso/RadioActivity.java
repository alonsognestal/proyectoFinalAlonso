package com.example.proyectofinalalonso;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static com.example.proyectofinalalonso.Aplicacion.PLAY_SERVICES_RESOLUTION_REQUEST;

public class RadioActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2); //2 columnas
        if(!comprobarGooglePlayServices())
        {
            Toast.makeText(this, "Error, Google Play Services no está instalado o no es válido", Toast.LENGTH_LONG);
        }
        ButterKnife.bind(this);
        Aplicacion app = (Aplicacion) getApplicationContext();
        databaseReference = app.getItemsReference();
        DataAdapter adapter = new DataAdapter(R.layout.content_radio, databaseReference);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    /*private ArrayList<Genero> prepareData(){

        ArrayList<Genero> listaGeneros = new ArrayList<>();
        for(int i=0;i<nombresGeneros.length;i++){
            Genero genero = new Genero();
            genero.setTitulo(nombresGeneros[i]);
            genero.setUrl(android_image_urls[i]);
            listaGeneros.add(genero);
        }
        return listaGeneros;
    }*/

    private boolean comprobarGooglePlayServices() {
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
    }
}