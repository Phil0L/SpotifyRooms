package com.pl.spotifyrooms.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pl.spotifyrooms.R;
import com.pl.spotifyrooms.util.AsynchronousImageLoader;
import com.pl.spotifyrooms.util.Room;
import com.pl.spotifyrooms.util.SpotifyHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class BottomInfoDialog extends BottomSheetDialogFragment {

  public static final String TAG = "BottomInfoDialog";

  public static final int VIEW = 0;
  public static final int QUEUE = 1;
  public static final int QUEUE_ALL = 2;
  public static final int QUEUE_FRONT = 3;
  public static final int VIEW_ARTIST = 4;
  public static final int VIEW_ALBUM = 5;
  public static final int OPEN_SPOTIFY = 6;
  public static final int SHARE = 7;

  public RelativeLayout view;
  public LinearLayout queue;
  public LinearLayout queueAll;
  public LinearLayout queueFront;
  public LinearLayout viewArtist1;
  public LinearLayout viewArtist2;
  public LinearLayout viewArtist3;
  public TextView viewArtistName1;
  public TextView viewArtistName2;
  public TextView viewArtistName3;
  public LinearLayout viewAlbum;
  public ConstraintLayout openSpotify;
  public ConstraintLayout share;

  public ImageView image;
  public TextView title;
  public TextView artist;
  public TextView info;
  public TextView length;

  private Object o;
  private final List<Integer> contains;

  public BottomInfoDialog(Object o, int... contains) {
    this.o = o;
    List<Integer> out = new ArrayList<>();
    for (int i : contains) {
      out.add(i);
    }
    this.contains = out;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.bottom_info, container, false);
    Log.i(TAG, "Bottom dialog opened with " + o.getClass().getSimpleName());
    initializeCard(v);
    initializeActions(v);
    modifyCard();
    return v;
  }

  private void initializeActions(View v) {
    view = v.findViewById(R.id.view);
    view.setOnClickListener(viewClick);
    if (!contains.contains(VIEW))
      view.setVisibility(View.GONE);
    queue = v.findViewById(R.id.queue);
    queue.setOnClickListener(queueClick);
    if (!contains.contains(QUEUE))
      queue.setVisibility(View.GONE);
    queueAll = v.findViewById(R.id.queue_all);
    queueAll.setOnClickListener(queueAllClick);
    if (!contains.contains(QUEUE_ALL))
      queueAll.setVisibility(View.GONE);
    queueFront = v.findViewById(R.id.queue_front);
    queueFront.setOnClickListener(queueFrontClick);
    if (!contains.contains(QUEUE_FRONT))
      queueFront.setVisibility(View.GONE);
    viewArtist1 = v.findViewById(R.id.view_artist1);
    viewArtistName1 = v.findViewById(R.id.arT1);
    viewArtist1.setOnClickListener(viewArtistClick1);
    viewArtist1.setVisibility(View.GONE);
    viewArtist2 = v.findViewById(R.id.view_artist2);
    viewArtistName2 = v.findViewById(R.id.arT2);
    viewArtist2.setOnClickListener(viewArtistClick2);
    viewArtist2.setVisibility(View.GONE);
    viewArtist3 = v.findViewById(R.id.view_artist3);
    viewArtistName3 = v.findViewById(R.id.arT3);
    viewArtist3.setOnClickListener(viewArtistClick3);
    viewArtist3.setVisibility(View.GONE);
    viewAlbum = v.findViewById(R.id.view_album);
    viewAlbum.setOnClickListener(viewAlbumClick);
    if (!contains.contains(VIEW_ALBUM))
      viewAlbum.setVisibility(View.GONE);
    openSpotify = v.findViewById(R.id.open_in_spotify);
    openSpotify.setOnClickListener(openSpotifyClick);
    if (!contains.contains(OPEN_SPOTIFY))
      openSpotify.setVisibility(View.GONE);
    share = v.findViewById(R.id.share);
    share.setOnClickListener(shareClick);
    if (!contains.contains(SHARE))
      share.setVisibility(View.GONE);

    startInitializeArtists();
    }

  private void startInitializeArtists() {
    if (o instanceof Track) {
      Track track = (Track) o;
      int artistCount = track.artists.size();
      if (artistCount > 0) {
        String artistName = track.artists.get(0).name;
        viewArtist1.setVisibility(View.VISIBLE);
        viewArtistName1.setText(artistName);
      }
      if (artistCount > 1) {
        String artistName = track.artists.get(1).name;
        viewArtist2.setVisibility(View.VISIBLE);
        viewArtistName2.setText(artistName);
      }
      if (artistCount > 2) {
        String artistName = track.artists.get(2).name;
        viewArtist3.setVisibility(View.VISIBLE);
        viewArtistName3.setText(artistName);
      }
    }

    if (o instanceof AlbumSimple){
      AlbumSimple albumS = (AlbumSimple) o;
      new SpotifyHelper().getAlbum(albumS, list -> {
        Album album = list.get(0);
        int artistCount = album.artists.size();
        if (artistCount > 0){
          String artistName = album.artists.get(0).name;
          viewArtist1.setVisibility(View.VISIBLE);
          viewArtistName1.setText(artistName);
        }
        if (artistCount > 1){
          String artistName = album.artists.get(1).name;
          viewArtist2.setVisibility(View.VISIBLE);
          viewArtistName2.setText(artistName);
        }
        if (artistCount > 2){
          String artistName = album.artists.get(2).name;
          viewArtist3.setVisibility(View.VISIBLE);
          viewArtistName3.setText(artistName);
        }
      });
    }

    if (o instanceof PlaylistSimple) {
      PlaylistSimple playlist = (PlaylistSimple) o;
      String ownerName = playlist.owner.display_name;
      viewArtist1.setVisibility(View.VISIBLE);
      viewArtistName1.setText(ownerName);
    }

}

  private void initializeCard(View v) {
    View holder = v.findViewById(R.id.card);
    image = holder.findViewById(R.id.image);
    title = holder.findViewById(R.id.title);
    artist = holder.findViewById(R.id.artist);
    info = holder.findViewById(R.id.info);
    length = holder.findViewById(R.id.length);
  }

  @SuppressLint("SetTextI18n")
  private void modifyCard() {
        /*
          Tracks
         */
    if (o instanceof Track) {
      Track track = (Track) o;
      // title
      if (track.name != null)
        title.setText(track.name);

      // artist
      if (track.artists != null && !track.artists.isEmpty()) {
        List<String> artistNames = new ArrayList<>();
        track.artists.forEach(a -> artistNames.add(a.name));
        String artistString = Arrays.toString(artistNames.toArray()).replaceAll("[\\[\\]]", "");
        artist.setText(artistString);
      }

      // info
      if (track.album != null && track.album.name != null)
        info.setText(track.album.name);

      // length
      if (track.duration_ms != 0) {
        long millis = track.duration_ms;
        String length;
        if (millis > 3600000)
          length = String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                  TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                  TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        else
          length = String.format(Locale.getDefault(), "%02d:%02d",
                  TimeUnit.MILLISECONDS.toMinutes(millis),
                  TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        this.length.setText(length);
      }

      // image
      try {
        image.setVisibility(View.INVISIBLE);
        Image image = track.album.images.get(0);
        URL url = new URL(image.url);
        new AsynchronousImageLoader(this.image).execute(url);
      } catch (IOException | NullPointerException ignored) {
      }
    }

        /*
          Playlists
         */
    if (o instanceof PlaylistSimple) {
      PlaylistSimple playlistSimple = (PlaylistSimple) o;
      // title
      if (playlistSimple.name != null)
        title.setText(playlistSimple.name);

      // artist
      if (playlistSimple.owner != null && playlistSimple.owner.display_name != null) {
        artist.setText(playlistSimple.owner.display_name);
      }

      // length
      if (playlistSimple.tracks != null)
        length.setText(playlistSimple.tracks.total + " Tracks");

      // image
      try {
        image.setVisibility(View.INVISIBLE);
        Image image = playlistSimple.images.get(0);
        URL url = new URL(image.url);
        new AsynchronousImageLoader(this.image).execute(url);
      } catch (IOException | NullPointerException ignored) {
      }
    }

    /*
     * Albums
     */
    if (o instanceof AlbumSimple) {
      AlbumSimple albumSimple = (AlbumSimple) o;
      // title
      if (albumSimple.name != null)
        title.setText(albumSimple.name);

      // type
      if (albumSimple.album_type != null) {
        artist.setText(albumSimple.album_type);
      }

      // image
      try {
        image.setVisibility(View.INVISIBLE);
        Image image = albumSimple.images.get(0);
        URL url = new URL(image.url);
        new AsynchronousImageLoader(this.image).execute(url);
      } catch (IOException | NullPointerException ignored) {
      }
    }

    /*
     * Artists
     */
    if (o instanceof Artist) {
      Artist artist = (Artist) o;
      // name
      if (artist.name != null)
        title.setText(artist.name);

      // follower
      if (artist.followers != null) {
        this.artist.setText(artist.followers.total + " followers");
      }

      // image
      try {
        image.setVisibility(View.INVISIBLE);
        Image image = artist.images.get(0);
        URL url = new URL(image.url);
        new AsynchronousImageLoader(this.image).execute(url);
      } catch (IOException | NullPointerException | IndexOutOfBoundsException ignored) {
      }
    }
  }

  @Override
  public int getTheme() {
    return R.style.BottomSheetDialogTheme;
  }

  private final View.OnClickListener viewClick = v -> {
    Log.i(TAG, "View clicked");
  };

  private final View.OnClickListener queueClick = v -> {
    Log.i(TAG, "Queue clicked");
    if (o instanceof Track) {
      Track track = (Track) o;
      Room.currentRoom.queue.push(track);
      dismiss();
    }

  };

  private final View.OnClickListener queueAllClick = v -> {
    Log.i(TAG, "Queue all clicked");
    if (o instanceof PlaylistSimple) {
      PlaylistSimple playlist = (PlaylistSimple) o;
      new SpotifyHelper().getPlaylistTracks(playlist, tracks -> {
        Room.currentRoom.queue.push(tracks.toArray(new Track[0]));
        dismiss();
      });
    }

    if (o instanceof AlbumSimple) {
      AlbumSimple album = (AlbumSimple) o;
      new SpotifyHelper().getAlbumTracks(album, tracks -> {
        Room.currentRoom.queue.push(tracks.toArray(new Track[0]));
        dismiss();
      });
    }
  };

  private final View.OnClickListener queueFrontClick = v -> {
    Log.i(TAG, "Queue front clicked");
    if (o instanceof Track) {
      Track track = (Track) o;
      Room.currentRoom.queue.pushFront(track);
      dismiss();
    }
  };

  private final View.OnClickListener viewArtistClick1 = v -> {
    Log.i(TAG, "View artist clicked");
  };

  private final View.OnClickListener viewArtistClick2 = v -> {
    Log.i(TAG, "View artist clicked");
  };

  private final View.OnClickListener viewArtistClick3 = v -> {
    Log.i(TAG, "View artist clicked");
  };

  private final View.OnClickListener viewAlbumClick = v -> {
    Log.i(TAG, "View album clicked");
  };

  private final View.OnClickListener openSpotifyClick = v -> {
    Log.i(TAG, "Open spotify clicked");
    String extra = "";
    if (o instanceof Track) {
      Track track = (Track) o;
      extra = track.uri;
    }
    if (o instanceof PlaylistSimple) {
      PlaylistSimple playlist = (PlaylistSimple) o;
      extra = playlist.uri;
    }
    if (o instanceof AlbumSimple) {
      AlbumSimple album = (AlbumSimple) o;
      extra = album.uri;
    }
    if (o instanceof Artist) {
      Artist artist = (Artist) o;
      extra = artist.uri;
    }

    if (extra.equals(""))
      return;
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(extra));
    assert getActivity() != null;
    intent.putExtra(Intent.EXTRA_REFERRER,
            Uri.parse("android-app://" + getActivity().getPackageName())
    );
    dismiss();
    Log.i(TAG, "Spotify started with: " + extra);
    startActivity(intent);
  };

  private final View.OnClickListener shareClick = v -> {
    Log.i(TAG, "Share clicked");
    String extra = "";
    if (o instanceof Track) {
      Track track = (Track) o;
      extra = "https://open.spotify.com/track/" + track.id;
    }
    if (o instanceof PlaylistSimple) {
      PlaylistSimple playlist = (PlaylistSimple) o;
      extra = "https://open.spotify.com/playlist/" + playlist.id;
    }
    if (o instanceof AlbumSimple) {
      AlbumSimple album = (AlbumSimple) o;
      extra = "https://open.spotify.com/album/" + album.id;
    }
    if (o instanceof Artist) {
      Artist artist = (Artist) o;
      extra = "https://open.spotify.com/artist/" + artist.id;
    }

    if (extra.equals(""))
      return;
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, listen to this song from Spotify I found on Spotify Rooms: " + extra);
    sendIntent.setType("text/plain");

    Intent shareIntent = Intent.createChooser(sendIntent, null);
    dismiss();
    Log.i(TAG, "Share started with: " + extra);
    startActivity(shareIntent);
  };
}
