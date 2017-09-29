package com.example.proyectofinalalonso;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;

import static com.example.proyectofinalalonso.Aplicacion.bitmap1;

/**
 * Created by Alonso on 07/09/2017.
 */

public class DetallePodcastActivity extends AppCompatActivity implements View.OnTouchListener {

    TextView txtView;
    ImageView imgView;
    boolean isPlay = false;
    ToggleButton buttonStreaming;
    String URLAudio = "";
    private static final int STATE_PLAYING = 1;
    private static final int STATE_STOPPED = 0;

    Bundle bundleGlobal = new Bundle();
    private int mCurrentState;

    private static MediaBrowserCompat mMediaBrowserCompat;
    private static MediaControllerCompat mMediaControllerCompat;
    MainActivity main = new MainActivity();


    private RemoteMediaClient remoteMediaClient;
    private boolean isPlaying;
    private Button audioButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_emisora_podcast_detalle);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        final String idEmisora = bundle.getString("idEmisora");
        final String imagen = bundle.getString("imagen");
        URLAudio = bundle.getString("URLAudio");
        String rss = bundle.getString("rss");
        final String genero = bundle.getString("genero");
        final CastSession mCastSession = Aplicacion.getmCastSession();

        txtView = (TextView) findViewById(R.id.txtTitulo);
        imgView = (ImageView) findViewById(R.id.imgLogo);
        txtView.setText(idEmisora);
        Glide.with(this).load(imagen).into(imgView);

        imgView.buildDrawingCache();
        bitmap1 = imgView.getDrawingCache();

        bundleGlobal = bundle;
        final Intent i = new Intent(this,
                ServicioMusica.class);
        i.putExtra("idEmisora", idEmisora);
        i.putExtra("imagen", imagen);
        i.putExtra("URLAudio", URLAudio);
        i.putExtra("rss", rss);
        i.putExtra("genero", genero);

        // Inicializando el volumen
        initializeVolume();

       /* mMediaBrowserCompat = new MediaBrowserCompat(this, new ComponentName(this, BackgroundAudioService.class),
                mMediaBrowserCompatConnectionCallback, bundleGlobal);
        mMediaBrowserCompat.connect();*/

        buttonStreaming = (ToggleButton) findViewById(R.id.playPauseButton);

        buttonStreaming.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buttonStreaming.setEnabled(true);

                isPlay = !isPlay;

                if (isPlay) {
                    startService(i);
                    switch (v.getId()) {
                        case R.id.playPauseButton:
                            if (remoteMediaClient != null) {
                                MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
                                movieMetadata.putString(MediaMetadata.KEY_TITLE, idEmisora);
                                movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, genero);
                                movieMetadata.addImage(new WebImage(Uri.parse(imagen)));
                                MediaInfo mediaInfo = new MediaInfo.Builder(URLAudio).setStreamType(MediaInfo.STREAM_TYPE_LIVE).setContentType("audios/mp3").setMetadata(movieMetadata).build();
                                remoteMediaClient = mCastSession.getRemoteMediaClient();
                                remoteMediaClient.load(mediaInfo, true, 0);
                                isPlaying = true;
                                break;
                            }
                    }
                } else {

                    if (remoteMediaClient != null) {
                        if (isPlaying) {

                            remoteMediaClient.pause();
                            isPlaying = false;
                        } else {

                            remoteMediaClient.play();
                            isPlaying = true;
                        }
                    }
                    stopService(i);
                }

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaBrowserCompat.disconnect();
    }

    @Override
    public void onResume() {
       /* RadioFragment detalleFragment = (RadioFragment) getFragmentManager().findFragmentById(R.id.fragment_detalle);
        if (detalleFragment == null) {
            ((MainActivity)this.mostrarElementos(false);
        }*/
        super.onResume();
    }

    private void initializeVolume() {
        try {
            final SeekBar volumeBar = (SeekBar) findViewById(R.id.volumeSeekBar);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            volumeBar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeBar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));

            final OnSeekBarChangeListener eventListener = new OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            };

            volumeBar.setOnSeekBarChangeListener(eventListener);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }




}