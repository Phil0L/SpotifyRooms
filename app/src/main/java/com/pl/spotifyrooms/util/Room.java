package com.pl.spotifyrooms.util;

import android.util.Log;

import com.pl.spotifyrooms.LoginActivity;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.UserPublic;

public class Room {

    public static final String TAG = "Room";
    public static Room currentRoom;

    public String id;
    public List<UserPublic> member;
    public Queue queue;
    public Track currentTrack;
    public boolean paused;

    public Room(){
        String seed = getSaltID();
        while (hasRoomInDatabase(seed))
            seed = getSaltID();
        this.id = seed;
        Log.i(TAG, "joined room " + seed);

        member = new ArrayList<>();
        queue = new Queue();
    }

    public static boolean hasRoomInDatabase(String id){
        return false;
    }

    public static String getSaltID(){
        Random random = new Random();
        String symbols = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < 5; i++){
            out.append(symbols.charAt(random.nextInt(symbols.length())));
        }
        return out.toString();
    }

    public void nextTrack(){
        SpotifyAppRemote remote = LoginActivity.spotifyController;
        if (queue.isEmpty()) {
            remote.getPlayerApi().pause();
            this.paused = true;
            return;
        }
        this.currentTrack = queue.pull();
        // play current Track
    }

}
