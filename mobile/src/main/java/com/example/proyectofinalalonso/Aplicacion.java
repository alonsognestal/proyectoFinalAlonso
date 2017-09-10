package com.example.proyectofinalalonso;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by Alonso on 20/08/2017.
 */

public class Aplicacion extends Application {

    static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String ITEMS_CHILD_NAME = "generos";
    private String RADIOS = "emisoras";
    private String genero;
    private String nombreEmisora;

    private static DatabaseReference generosReference; //Referencia a la base de datos de géneros
    private static DatabaseReference radiosReference; //Referencia a la base de datos de emisoras de radio
    private static Context context;

    public Aplicacion() {
    }

    public Aplicacion(String genero) {
        this.genero = genero;
    }

    //Getters
    public static DatabaseReference getItemsReference() {
        return generosReference;
    }

    public static DatabaseReference getItemsRadioReference() {
        return radiosReference;
    }

    public static Context getAppContext() {
        return Aplicacion.context;
    }

    //Setters
    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Aplicacion.context = getApplicationContext();
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.setPersistenceEnabled(true);
        //Cargo la referencia de la BBDD de géneros
        generosReference = database.getReference(ITEMS_CHILD_NAME);
    }


    //Método público para obtener la referencia de la BBDD de las emisoras de radio
    public Query obtenerReferenciaDatabaseEmisoras() {
        //final ArrayList<EmisoraRadio> lista = new ArrayList<EmisoraRadio>();
        FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance();
        //Importante poner el keepSynced(True) para obtener siempre la última información en remoto
        secondDatabase.getReference(RADIOS).keepSynced(true);
        radiosReference = secondDatabase.getReference(RADIOS);
        Query query = radiosReference.orderByChild("Categoria").equalTo(genero);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    System.out.println("Hay " + dataSnapshot.getChildrenCount() + " emisoras");
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        EmisoraRadio emisora = postSnapshot.getValue(EmisoraRadio.class);
                        System.out.println(emisora.getId() + " - " + emisora.getCategoria() + " - " + emisora.getUrlImagen() + " - " + emisora.getUrlAudio());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        secondDatabase.getReference().setValue(ServerValue.TIMESTAMP);
        return query;

    }
}