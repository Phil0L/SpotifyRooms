package com.pl.spotifyrooms.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class Queue {

    public static final String TAG = "Queue";

    public List<Track> tracks;

    public Queue() {
        tracks = new ArrayList<>();
    }

    public void push(Track... tracks){
        Log.i(TAG, "push: added " + tracks.length + " tracks");
        this.tracks.addAll(Arrays.asList(tracks));
    }

    public void pushFront(Track track){
        Log.i(TAG, "pushFront: added track");
        this.tracks.add(0, track);
    }

    public Track pull(){
        Log.i(TAG, "pulled track");
        return tracks.remove(0);
    }

    public boolean isEmpty(){
        return tracks.isEmpty();
    }

}
