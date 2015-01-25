package edu.asu.msse.aashwath.audioplayback;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/**
 * Copyright (C) 2015 Akshay Ashwathanarayana
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Akshay Ashwathanarayana mailto:Akshay.Ashwathanarayana@asu.edu
 * @version January 21, 2015
 */

public class MusicPlaybackService extends Service implements MediaPlayer.OnCompletionListener{
    public static String COMMAND = "cmd";
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    public static final int RESUME = 3;
    public static final int STOP = 4;
    public static final int NONE = 5;
    private MediaPlayer player;
    private boolean isPlaying;

    @Override
    public void onCreate() {
        // The service is being created
        super.onCreate();
        Log.d(getClass().getSimpleName(), "In onCreate()");
        player = MediaPlayer.create(getApplicationContext(), R.raw.mpthreetest);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnCompletionListener(this);
        isPlaying = false;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        int command = intent.getIntExtra(COMMAND, NONE);
        Log.d(getClass().getSimpleName(), "In onStartCommand()");
        switch (command) {
            case PLAY:
                if(isPlaying) {
                    player.pause();
                    player.seekTo(0);
                }
                player.start();
                isPlaying = true;
                break;
            case PAUSE:
                player.pause();
                isPlaying = false;
                break;
            case RESUME:
                if(!isPlaying) {
                    player.start();
                    isPlaying = true;
                }
                break;
            case STOP:
                isPlaying = false;
                //STOP command stops the service and the MediaPlayer instance is released in the
                //onDestroy() method.
                stopSelf();
                break;
            case NONE:
            default:
                break;

        }
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        Log.d(getClass().getSimpleName(), "In onBind()");
        return null;
    }
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        Log.d(getClass().getSimpleName(), "In onDestroy()");
        if(player != null){
            player.stop();
            player.release();
        }
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.seekTo(0);
        mp.start();
        isPlaying = true;
    }
}
