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
    private String RADIOS = "emisoras";
    private String genero;
    private String LOCALES = "Locales";
    private String POP = "Pop";
    private String NOTICIAS = "Noticias";
    private String CLASICA = "Clasica";
    private String LATINA = "Latina";
    private String DANCE = "Dance";
    private String OLDIES = "Oldies";
    private String EXITOS = "Exitos";
    private String ALTERNATIVA = "Alternativa";
    private String ROCK = "Rock";
    private String nombreEmisora;

    private static DatabaseReference generosReference;
    private static DatabaseReference radiosReference;
    private static Context context;

    public Aplicacion()
    {
    }

    public Aplicacion(String genero)
    {
        this.genero = genero;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Aplicacion.context = getApplicationContext();
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.setPersistenceEnabled(true);

        generosReference = database.getReference(ITEMS_CHILD_NAME);


    }

    public static Context getAppContext() {
        return Aplicacion.context;
    }

    public static DatabaseReference getItemsReference() {
        return generosReference;
    }
    public static DatabaseReference getItemsRadioReference() {
        return radiosReference;
    }

    public void setItemsRadioReference(String genero)
    {
        this.genero = genero;
    }

    public Query obtenerReferenciaDatabase()
    {
        FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance();
        //Importante poner el keepSynced(True) para obtener siempre la última información en remoto
        secondDatabase.getReference(RADIOS).keepSynced(true);
        radiosReference = secondDatabase.getReference(RADIOS);
        Query query = radiosReference.orderByChild("Categoria").equalTo(genero);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot emisoras : dataSnapshot.getChildren()) {
                      dataSnapshot.getValue(EmisoraRadio.class);


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