package com.example.proyectofinalalonso;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;

/**
 * Created by Alonso on 10/09/2017.
 */

public class MediaPlayerService {
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";

    private MediaPlayer mMediaPlayer;
    private MediaSessionManager mManager;
    private MediaSession mSession;
    private MediaController mController;


}
