package com.pl.spotifyrooms.util;

import java.util.Timer;
import java.util.TimerTask;

public class Delayed {

    public static final String TAG = "Delayed";

    public Delayed(OnDelayed callback, int delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                callback.execute();
            }
        }, delay);
    }

    public interface OnDelayed{

        void execute();

    }

}
