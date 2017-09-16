package com.example.proyectofinalalonso;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.bumptech.glide.Glide;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.R.attr.bitmap;
import static android.R.attr.category;

/**
 * Created by Alonso on 07/09/2017.
 */

public class DetallesEmisoraRadioActivity extends AppCompatActivity implements View.OnTouchListener, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    MediaController mediaController;
    MediaPlayer mediaPlayer;
    TextView txtView;
    ImageView imgView;
    boolean isPlay = false;
    ToggleButton buttonStreaming;
    String URLAudio = "";
    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;

    private int mCurrentState;

    private MediaBrowserCompat mMediaBrowserCompat;
    private MediaControllerCompat mMediaControllerCompat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emisora_radio_detalle);
        Intent intent = getIntent();
        String idEmisora = intent.getStringExtra("idEmisora");
        String imagen = intent.getStringExtra("imagen");
        URLAudio = intent.getStringExtra("URLAudio");
        String rss = intent.getStringExtra("rss");
        String genero = intent.getStringExtra("genero");

        txtView = (TextView)findViewById(R.id.txtTitulo);
        imgView = (ImageView)findViewById(R.id.imgLogo);
        txtView.setText(idEmisora);
        Glide.with(this).load(imagen).into(imgView);
        final Uri audio = Uri.parse(URLAudio);

        // Inicializo el objeto MediaPlayer
        initializeMediaPlayer();

        // Inicializando el volumen
        initializeVolume();


        mMediaBrowserCompat = new MediaBrowserCompat(this, new ComponentName(this, BackgroundAudioService.class),
        mMediaBrowserCompatConnectionCallback, getIntent().getExtras());

        mMediaBrowserCompat.connect();

        buttonStreaming = (ToggleButton) findViewById(R.id.playPauseButton);
        buttonStreaming.setEnabled(true);
        isPlay=true;
        startPlaying(audio);
        buttonStreaming.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               // buttonStreaming.setEnabled(true);

                isPlay = !isPlay;

                if (isPlay) {
                    startPlaying(audio);
                } else {
                    stopPlaying();
                }
                /*if( mCurrentState == STATE_PAUSED ) {
                    getSupportMediaController().getTransportControls().play();
                    mCurrentState = STATE_PLAYING;
                } else {
                    if( getSupportMediaController().getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING ) {
                        getSupportMediaController().getTransportControls().pause();
                    }

                    mCurrentState = STATE_PAUSED;
                }*/
            }
        });

       /* mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(this);*/
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

    private void initializeMediaPlayer() {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("Buffering", "" + percent);
            }
        });
    }

    public void startPlaying(Uri audio) {

        try {

            Toast.makeText(getApplicationContext(),
                    "Conectando con la radio, espere unos segundos...",
                    Toast.LENGTH_LONG).show();

            mediaPlayer.reset();
            mediaPlayer.setDataSource(this,audio);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {

                    mediaPlayer.start();
                    //Cambio el botón a pause
                    buttonStreaming.setEnabled(true);
                }
            });

            mediaPlayer.prepareAsync();

        } catch (IllegalArgumentException | SecurityException
                | IllegalStateException | IOException e) {
            Toast.makeText(getApplicationContext(),
                    "Error al conectar con la radio", Toast.LENGTH_LONG).show();
        }

    }

    public void stopPlaying() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            initializeMediaPlayer();
            buttonStreaming.setEnabled(true);
        }
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
       mp.start();
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(this.findViewById(R.id.fragment_detalle));
        mediaController.setEnabled(true);
        mediaController.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override public void onStop() {
        //TODO: Cuando entra aquí el mediaController es nulo :S
        //mediaController.hide();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            Log.d("Audiolibros", "Error en mediaPlayer.stop()");
        }
        super.onStop();
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        try{
            return mediaPlayer.getCurrentPosition();
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private MediaBrowserCompat.ConnectionCallback mMediaBrowserCompatConnectionCallback = new MediaBrowserCompat.ConnectionCallback() {

        @Override
        public void onConnected() {
            super.onConnected();
            try {
                final Uri audio = Uri.parse(URLAudio);
                mMediaControllerCompat = new MediaControllerCompat(DetallesEmisoraRadioActivity.this, mMediaBrowserCompat.getSessionToken());
                mMediaControllerCompat.registerCallback(mMediaControllerCompatCallback);
                setSupportMediaController(mMediaControllerCompat);
                getSupportMediaController().getTransportControls().playFromUri(audio, null);

            } catch( RemoteException e ) {

            }
        }
    };

    private MediaControllerCompat.Callback mMediaControllerCompatCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            if( state == null ) {
                return;
            }

            switch( state.getState() ) {
                case PlaybackStateCompat.STATE_PLAYING: {
                    mCurrentState = STATE_PLAYING;
                    break;
                }
                case PlaybackStateCompat.STATE_PAUSED: {
                    mCurrentState = STATE_PAUSED;
                    break;
                }
            }
        }
    };

}
