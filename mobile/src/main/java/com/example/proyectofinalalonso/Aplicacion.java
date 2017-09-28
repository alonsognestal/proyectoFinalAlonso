package com.example.proyectofinalalonso;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
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
    private String NOTICIAS = "noticias";
    private String genero;
    private String nombreEmisora;

    private static DatabaseReference generosReference; //Referencia a la base de datos de géneros
    private static DatabaseReference radiosReference; //Referencia a la base de datos de emisoras de radio
    private static DatabaseReference noticiasReference; //Referencia a la base de datos de noticias
    private static DatabaseReference radiosConPodcastReference; //Referencia a la base de datos de emisoras de radio con podcast

    private static Context context;
    private FirebaseAuth auth;
    private static RequestQueue colaPeticiones;
    private static ImageLoader lectorImagenes;
    static Bitmap bitmap1;
    private static CastSession mCastSession;
    private static SessionManager mSessionManager;

    static boolean haAcabadoHiloSecundario = false;
    static ArrayList<ArrayList<String>> listadoGlobalPodcasts;


    public Aplicacion() {
    }

    public Aplicacion(String genero) {
        this.genero = genero;
    }

    public Aplicacion(CastSession castSession, SessionManager sessionManager)
    {
        this.mCastSession = castSession;
        this.mSessionManager = sessionManager;
    }

    //Getters
    public static DatabaseReference getItemsReference() {
        return generosReference;
    }

    public static DatabaseReference getItemsRadioReference() {
        return radiosReference;
    }

    public static DatabaseReference getRadiosConPodcastReference() {
        return radiosConPodcastReference;
    }

    public static Context getAppContext() {
        return Aplicacion.context;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public static ImageLoader getLectorImagenes() {
        return lectorImagenes;
    }

    //Setters
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public static void setmCastSession(CastSession mCastSession) {
        Aplicacion.mCastSession = mCastSession;
    }
    public static void setmSessionManager(SessionManager mSessionManager) {
        Aplicacion.mSessionManager = mSessionManager;
    }
    //Getters
    public static CastSession getmCastSession() {
        return Aplicacion.mCastSession;
    }
    public static SessionManager getmSessionManager() {
        return Aplicacion.mSessionManager;
    }
   /* public static Boolean getHaAcabadoHiloSecun()
    {
        return haAcabadoHiloSecundario;
    }

    public void setHaAcabadoHiloSecundario(Boolean haAcabado)
    {
        this.haAcabadoHiloSecundario = haAcabado;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        Aplicacion.context = getApplicationContext();
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        colaPeticiones = Volley.newRequestQueue(this);
        lectorImagenes = new ImageLoader(colaPeticiones, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
        });

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
        //secondDatabase.getReference().setValue(ServerValue.TIMESTAMP);
        return query;

    }

    //Método público para obtener la referencia de la BBDD de las emisoras de radio
    public DatabaseReference obtenerEmisorasConRss() {
        //final ArrayList<EmisoraRadio> lista = new ArrayList<EmisoraRadio>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //Importante poner el keepSynced(True) para obtener siempre la última información en remoto
        firebaseDatabase.getReference(NOTICIAS).keepSynced(true);
        noticiasReference = firebaseDatabase.getReference(NOTICIAS);

        Query query = noticiasReference.orderByChild("nombre");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    System.out.println("Hay " + dataSnapshot.getChildrenCount() + " emisoras");
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Noticia noticia = postSnapshot.getValue(Noticia.class);
                        System.out.println(noticia.getNombre() + " - " + noticia.geturlimagen() + " - " + noticia.getRss());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return noticiasReference;

    }

    //Método público para obtener la referencia de la BBDD de las emisoras de radio
    public Query obtenerReferenciaEmisoras() {
        //final ArrayList<EmisoraRadio> lista = new ArrayList<EmisoraRadio>();
        FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance();
        //Importante poner el keepSynced(True) para obtener siempre la última información en remoto
        secondDatabase.getReference(RADIOS).keepSynced(true);
        radiosReference = secondDatabase.getReference(RADIOS);
        Query query = radiosReference.orderByChild("id");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    System.out.println("Hay " + dataSnapshot.getChildrenCount() + " emisoras");
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        postSnapshot.child("Podcast").getChildren();
                        EmisoraRadio emisora = postSnapshot.getValue(EmisoraRadio.class);
                        System.out.println(emisora.getId() + " - " + emisora.getCategoria() + " - " + emisora.getUrlImagen() + " - " + emisora.getUrlAudio());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //secondDatabase.getReference().setValue(ServerValue.TIMESTAMP);
        return query;

    }

}