package com.example.proyectofinalalonso;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

import static com.example.proyectofinalalonso.Aplicacion.bitmap1;
import static com.example.proyectofinalalonso.Aplicacion.getAppContext;

/**
 * Created by Alonso on 24/09/2017.
 */

public class ServicioMusica extends Service implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    MediaPlayer mediaPlayer;
    MediaController mediaController;
    //ToggleButton buttonStreaming;
    private static final int ID_NOTIFICACION_CREAR = 1;

    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();

    }

    @Override
    public int onStartCommand(Intent i, int flags, int idArranque) {
        try {

            Bundle bundle = i.getExtras();
            String URLAudio = bundle.getString("URLAudio");
            String idEmisora = bundle.getString("idEmisora");
            final Uri audio = Uri.parse(URLAudio);
            startPlaying(audio);
            NotificationCompat.Builder notific = new NotificationCompat.Builder(this).setContentTitle("Creando Servicio de Música").setSmallIcon(R.mipmap.ic_launcher).setContentText("información adicional");
            PendingIntent intencionPendiente = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
            notific.setContentIntent(intencionPendiente);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ID_NOTIFICACION_CREAR, notific
                    .setLargeIcon(bitmap1)
                    .setWhen(System.currentTimeMillis() + 1000 * 60 * 60)
                    .setContentTitle(idEmisora)
                    .setTicker("Texto en barra de estado")

                    .build());


            //mediaPlayer.start();
            return START_STICKY;

        } catch (Exception ex) {
            return Service.START_NOT_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        try {
            stopPlaying();
        } catch (Exception ex) {

        }

    }

    @Override
    public IBinder onBind(Intent intencion) {
        return null;
    }


    //MEDIAPLAYER
    @Override
    public void onPrepared(MediaPlayer mp) {
       try {
           mp.start();
           mediaController.setMediaPlayer(this);
           mediaController.setEnabled(true);
           mediaController.show();
       }
       catch (Exception ex)
       {

       }
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
        try {
            return mediaPlayer.getCurrentPosition();
        } catch (Exception e) {
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

    public void startPlaying(Uri audio) {

        try {

            Toast.makeText(getApplicationContext(),
                    "Conectando con la radio, espere unos segundos...",
                    Toast.LENGTH_LONG).show();

            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, audio);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {

                    mediaPlayer.start();
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
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
        catch (Exception ex)
        {

        }
    }


}