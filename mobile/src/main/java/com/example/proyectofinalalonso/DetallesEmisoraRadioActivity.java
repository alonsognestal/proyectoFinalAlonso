package com.example.proyectofinalalonso;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.bumptech.glide.Glide;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.R.attr.bitmap;
import static android.R.attr.category;
import static com.example.proyectofinalalonso.Aplicacion.bitmap1;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Alonso on 07/09/2017.
 */

public class DetallesEmisoraRadioActivity extends Fragment implements View.OnTouchListener {

    TextView txtView;
    ImageView imgView;
    boolean isPlay = false;
    ToggleButton buttonStreaming;
    String URLAudio = "";
    //private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_STOPPED = 0;
    //private static final int STATE_STOP = 2;
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
    }

    @Override
    public void onDestroy() {
        mMediaBrowserCompat.disconnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_emisora_radio_detalle, container, false);
        Bundle bundle = this.getArguments();
        final String idEmisora = bundle.getString("idEmisora");
        final String imagen = bundle.getString("imagen");
        URLAudio = bundle.getString("URLAudio");
        String rss = bundle.getString("rss");
        final String genero = bundle.getString("genero");

        final SessionManager mSessionManager = Aplicacion.getmSessionManager();
        final CastSession mCastSession = Aplicacion.getmCastSession();

        txtView = (TextView) rootView.findViewById(R.id.txtTitulo);
        imgView = (ImageView) rootView.findViewById(R.id.imgLogo);
        txtView.setText(idEmisora);
        Glide.with(this).load(imagen).into(imgView);

       /* imgView.setImageBitmap(bitmap1);*/
        imgView.buildDrawingCache();
        bitmap1 = imgView.getDrawingCache();
        final Uri audio = Uri.parse(URLAudio);
        //bundle.putParcelable("logoEmisora", imgView);
        bundleGlobal = bundle;
        final Intent i = new Intent(getActivity(),
                ServicioMusica.class);
        i.putExtra("idEmisora", idEmisora);
        i.putExtra("imagen", imagen);
        i.putExtra("URLAudio", URLAudio);
        i.putExtra("rss", rss);
        i.putExtra("genero", genero);

        // Inicializo el objeto MediaPlayer
        //initializeMediaPlayer();

        // Inicializando el volumen
        initializeVolume(rootView);

        mMediaBrowserCompat = new MediaBrowserCompat(getActivity(), new ComponentName(getActivity(), BackgroundAudioService.class),
                mMediaBrowserCompatConnectionCallback, bundleGlobal);
        mMediaBrowserCompat.connect();

        buttonStreaming = (ToggleButton) rootView.findViewById(R.id.playPauseButton);

        buttonStreaming.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buttonStreaming.setEnabled(true);

                isPlay = !isPlay;

                if (isPlay) {
                    getActivity().startService(i);
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
                    //startPlaying(audio);
                } else {
                    /*if (getActivity().getSupportMediaController().getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {
                        getActivity().getSupportMediaController().getTransportControls().stop();
                        mCurrentState = STATE_STOPPED;
                        mMediaBrowserCompat.disconnect();
                        Log.d("", "Llamada a disconnect() dentro del else");
                        Log.d("", String.valueOf(mMediaBrowserCompat.isConnected()));
                    }*/
                    if (remoteMediaClient != null) {
                        if (isPlaying) {

                            remoteMediaClient.pause();
                            isPlaying = false;
                        } else {

                            remoteMediaClient.play();
                            isPlaying = true;
                        }
                    }
                    //stopPlaying();
                    getActivity().stopService(i);
                    //buttonStreaming.setEnabled(true);
                }

            }
        });
        return rootView;

    }


    private void initializeVolume(View rootView) {
        try {
            final SeekBar volumeBar = (SeekBar) rootView.findViewById(R.id.volumeSeekBar);
            final AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

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

    /*private void initializeMediaPlayer() {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("Buffering", "" + percent);
            }
        });
    }*/


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

  /*  @Override
    public void onStop() {
        //TODO: Cuando entra aquí el mediaController es nulo :S
        //mediaController.hide();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            Log.d("", "Error en mediaPlayer.stop()");
        }
        super.onStop();
    }*/


    private MediaBrowserCompat.ConnectionCallback mMediaBrowserCompatConnectionCallback = new MediaBrowserCompat.ConnectionCallback() {

        @Override
        public void onConnected() {
            super.onConnected();
            try {
                Log.d("", "Llamada a onconnect()");
                //if (!isPlaying) {
                final Uri audio = Uri.parse(URLAudio);
                mMediaControllerCompat = new MediaControllerCompat(getActivity(), mMediaBrowserCompat.getSessionToken());
                mMediaControllerCompat.registerCallback(mMediaControllerCompatCallback);
                getActivity().setSupportMediaController(mMediaControllerCompat);
                Log.d("Llamada a getactivit()", audio.toString() + bundleGlobal.toString());
                getActivity().getSupportMediaController().getTransportControls().playFromUri(audio, bundleGlobal);
                //}

            } catch (RemoteException e) {

            }
        }

        @Override
        public void onConnectionFailed() {
            super.onConnectionFailed();
            Log.d("Fallo", "onConnectionFailed");
        }
    };

    private MediaControllerCompat.Callback mMediaControllerCompatCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            if (state == null) {
                return;
            }

            switch (state.getState()) {
                case PlaybackStateCompat.STATE_PLAYING: {
                    mCurrentState = STATE_PLAYING;
                    break;
                }
               /* case PlaybackStateCompat.STATE_PAUSED: {
                    mCurrentState = STATE_PAUSED;
                    break;
                }*/
                case PlaybackStateCompat.STATE_STOPPED: {
                    mCurrentState = STATE_STOPPED;
                    break;
                }
            }
        }
    };

   /* private final View.OnClickListener btnClickListener = new View.OnClickListener() {
        String idEmisora = bundleGlobal.getString("idEmisora");
        String imagen = bundleGlobal.getString("imagen");
        String URLAudio = bundleGlobal.getString("URLAudio");
        String rss = bundleGlobal.getString("rss");
        String genero = bundleGlobal.getString("genero");

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playPauseButton:
                    if (isPlaying) {
                        buttonStreaming.setText("Play");
                        remoteMediaClient.pause();
                        isPlaying = false;
                    } else {
                        buttonStreaming.setText("Pausa");
                        remoteMediaClient.play();
                        isPlaying = true;
                    }
                    break;
                case R.id.btn_audio:
                    MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
                    Uri.parse(URLAudio);
                    movieMetadata.putString(MediaMetadata.KEY_TITLE, "Big Buck Bunny");
                    movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, "Demo Google Cast UPV");
                    movieMetadata.addImage(new WebImage(Uri.parse("http://bbb3d.renderfarming.net/img/logo.png")));
                    MediaInfo mediaInfo = new MediaInfo.Builder(URLAudio).setStreamType(MediaInfo.STREAM_TYPE_BUFFERED).setContentType("videos/mp4").setMetadata(movieMetadata).build();
                    remoteMediaClient = mCastSession.getRemoteMediaClient();
                    remoteMediaClient.load(mediaInfo, true, 0);
                    break;

            }
        }
    };*/


}