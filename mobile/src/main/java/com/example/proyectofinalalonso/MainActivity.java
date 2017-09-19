package com.example.proyectofinalalonso;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.GravityCompat;


import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.facebook.FacebookSdk;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.R.attr.description;

/**
 * Created by Alonso on 01/09/2017.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private static final int ID_NOTIFICACION = 1;
    private NotificationManager notificManager;
    private NotificationCompat.Builder notificacion;
    private RemoteViews remoteViews;
    public static final String ACCION_DEMO = "com.example.notificacionpersonalizada.ACCION_DEMO";
    public static final String EXTRA_PARAM = "com.example.notificacionpersonalizada.EXTRA_PARAM";
    private int contador = 0;

    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;

    private int mCurrentState;

    private MediaBrowserCompat mMediaBrowserCompat;
    private MediaControllerCompat mMediaControllerCompat;

    private Button mPlayPauseToggleButton;
    private CastSession mCastSession;
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.navigation_view);

        // Añado toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Obtengo la referencia del FrameLayout donde voy a meter cada uno de los 3 fragmentos y del tablayout
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);

        //Añado las 3 pestañas que me interesan
        final TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("NOTICIAS");
        tabLayout.addTab(firstTab);

        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("PODCAST");
        tabLayout.addTab(secondTab);

        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("RADIO EN DIRECTO");
        tabLayout.addTab(thirdTab);

        // Navigation Drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Nombre de usuario
        SharedPreferences pref = getSharedPreferences(
                "com.example.proyectofinalalonso", MODE_PRIVATE);
        String name = pref.getString("name", null);
        View headerLayout = navigationView.getHeaderView(0);
        TextView txtName = (TextView) headerLayout.findViewById(R.id.txtName);
        txtName.setText(String.format(getString(R.string.welcome_message), name));

// perform setOnTabSelectedListener event on TabLayout
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Obtengo la posición de la pestaña pulsada y reemplazo el fragment con el que yo quiera
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new NewsActivity();
                        break;
                    case 1:
                        fragment = new PodcastActivity();
                        break;
                    case 2:
                        fragment = new RadioActivity();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//Añadimos código para configurar la notificación personalizada
        remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViews.setImageViewResource(R.id.reproducir, android.R.drawable.ic_media_play);
        remoteViews.setImageViewResource(R.id.imagen, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.titulo, "Notificatión personalizada");
        remoteViews.setTextColor(R.id.titulo, Color.BLACK);
        remoteViews.setTextViewText(R.id.texto, "Texto de la notificación.");
        remoteViews.setTextColor(R.id.texto, Color.BLACK);

        Intent intent = new Intent();
        intent.setAction(ACCION_DEMO);
        intent.putExtra(EXTRA_PARAM, "otro parámetro");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.reproducir, pendingIntent);

        // Foto de usuario
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        Uri urlImagen = usuario.getPhotoUrl();
        if (urlImagen != null) {
            NetworkImageView fotoUsuario = (NetworkImageView)
                    headerLayout.findViewById(R.id.imageView);
            Aplicacion aplicacion = (Aplicacion) getApplicationContext();
            fotoUsuario.setImageUrl(urlImagen.toString(),
                    aplicacion.getLectorImagenes());
        }
        CastContext castContext = CastContext.getSharedInstance(this);
        mSessionManager = castContext.getSessionManager();
        /*//Lanzamos la notificación
        notificacion = new NotificationCompat.Builder(this);
        notificacion
                .setContent(remoteViews)
                .setPriority(Notification.PRIORITY_MAX)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setUsesChronometer(true)

                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notificación personalizada")
                .setContentText("Artista - Álbum")
                .setStyle(new NotificationCompat.MediaStyle());

        notificManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificManager.notify(ID_NOTIFICACION, notificacion.build());*/

        //Para lanzar una actividad cuando se pulse en la notificación
        /*Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificacion = new NotificationCompat.Builder(this).setContentIntent(pendingIntent);
*/
        /*IntentFilter filtro = new IntentFilter(ACCION_DEMO);
        registerReceiver(new ReceptorAnuncio(), filtro);*/
        //android.os.Debug.waitForDebugger();
    }

    //region Barra de acciones

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if( getSupportMediaController().getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING ) {
            getSupportMediaController().getTransportControls().pause();
        }*/

        //mMediaBrowserCompat.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_preferencias) {
            Toast.makeText(this, "Preferencias", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.menu_acerca) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Mensaje de Acerca De");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    //endregion
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_todos) {

        } else if (id == R.id.nav_epico) {

        } else if (id == R.id.nav_XIX) {

        } else if (id == R.id.nav_signout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            SharedPreferences pref = getSharedPreferences(
                                    "com.example.proyectofinalalonso", MODE_PRIVATE);
                            pref.edit().remove("provider").commit();
                            pref.edit().remove("email").commit();
                            pref.edit().remove("name").commit();
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                    });
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* public class ReceptorAnuncio extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String param = intent.getStringExtra(EXTRA_PARAM);
            Toast.makeText(context, "Parámetro:" + param, Toast.LENGTH_LONG).show();
            contador++;
            remoteViews.setTextViewText(R.id.texto, "Contador: " + contador);
            notificManager.notify(ID_NOTIFICACION, notificacion.build());
        }
    }*/


}