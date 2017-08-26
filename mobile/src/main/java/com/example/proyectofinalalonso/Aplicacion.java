package com.example.proyectofinalalonso;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Alonso on 20/08/2017.
 */

public class Aplicacion extends Application {
    /*private ArrayList<Genero> listaGeneros;
    private DataAdapter adaptador;
    private FirebaseStorage storage;
    private static StorageReference storageRef;

    @Override
    public void onCreate() {
        listaGeneros = Genero.ejemploGeneros();
        adaptador = new DataAdapter (this, listaGeneros);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://proyectoalonso-ff084.appspot.com");
    }
    public DataAdapter getAdaptador() {
        return adaptador;
    }
    public ArrayList<Genero> getVectorLibros() {
        return listaGeneros;
    }

    public static StorageReference getStorageReference()
    {
        return storageRef;
    }*/

    static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String ITEMS_CHILD_NAME = "generos";
    private static DatabaseReference eventosReference;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Aplicacion.context = getApplicationContext();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        eventosReference = database.getReference(ITEMS_CHILD_NAME);
    }

    public static Context getAppContext() {
        return Aplicacion.context;
    }

    public static DatabaseReference getItemsReference() {
        return eventosReference;
    }
}