package com.pl.spotifyrooms.ui.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.pl.spotifyrooms.LoginActivity;
import com.pl.spotifyrooms.R;
import com.pl.spotifyrooms.RoomActivity;
import com.pl.spotifyrooms.util.Room;
import com.spotify.android.appremote.api.SpotifyAppRemote;

public class QuickControls {

  public static final String TAG = "QuickControls";

  private Context context;

  private final CardView card;
  private final ImageView skipBack;
  private final ImageView pause;
  private final ImageView skip;

  public QuickControls(Context context, View view) {
    this.context = context;
    this.card = (CardView) view;
    this.card.setOnClickListener(roomClick);
    this.skipBack = view.findViewById(R.id.back_skip);
    this.skipBack.setOnClickListener(skipBackwards);
    this.pause = view.findViewById(R.id.play_pause);
    this.pause.setOnClickListener(pauseOrPlay);
    this.skip = view.findViewById(R.id.skip);
    this.skip.setOnClickListener(skipForwards);
  }

  private final View.OnClickListener roomClick = v -> {
    Log.i(TAG, "opened room");
    assert context != null;
    Intent intent = new Intent(context, RoomActivity.class);
    context.startActivity(intent);
  };

  private final View.OnClickListener skipBackwards = v -> {
    Log.i(TAG, "pressed skip backwards");
  };

  private final View.OnClickListener pauseOrPlay = v -> {
    Log.i(TAG, "pressed " + (Room.currentRoom.paused ? "resume" : "pause"));
    SpotifyAppRemote remote = LoginActivity.spotifyController;
    if (Room.currentRoom.paused)
      remote.getPlayerApi().resume();
    else
      remote.getPlayerApi().pause();
    Room.currentRoom.paused = !Room.currentRoom.paused;
  };

  private final View.OnClickListener skipForwards = v -> {
    Log.i(TAG, "pressed skip");
  };

}
