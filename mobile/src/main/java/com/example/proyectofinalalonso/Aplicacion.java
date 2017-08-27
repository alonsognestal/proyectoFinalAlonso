package com.example.proyectofinalalonso;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;

import com.google.firebase.FirebaseApp;
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

    static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String ITEMS_CHILD_NAME = "generos";
    private static DatabaseReference eventosReference;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Aplicacion.context = getApplicationContext();
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        eventosReference = database.getReference(ITEMS_CHILD_NAME);
        String s = eventosReference.getParent().toString();
    }

    public static Context getAppContext() {
        return Aplicacion.context;
    }

    public static DatabaseReference getItemsReference() {
        return eventosReference;
    }
}