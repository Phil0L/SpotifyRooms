package com.pl.spotifyrooms.util;

import android.util.Log;

import com.pl.spotifyrooms.LoginActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpotifyHelper {

    public static final String TAG = "SpotifyHelper";

    private final SpotifyService service;

    public SpotifyHelper() {
        this.service = LoginActivity.spotifyService;
    }

    public void getPlaylistTracks(PlaylistSimple playlist, OnSpotifyResults<Track> callback){
        service.getPlaylistTracks(playlist.owner.id, playlist.id, new Callback<Pager<PlaylistTrack>>() {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                Log.i(TAG, "received " + playlistTrackPager.items.size() + " tracks from playlist " + playlist.name);
                List<Track> list = new ArrayList<>();
                playlistTrackPager.items.forEach(playlistTrack -> list.add(playlistTrack.track));
                callback.result(list);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });
    }

    public void getAlbumTracks(AlbumSimple album, OnSpotifyResults<Track> callback){
        service.getAlbumTracks(album.id, new Callback<Pager<Track>>() {
            @Override
            public void success(Pager<Track> trackPager, Response response) {
                Log.i(TAG, "received " + trackPager.items.size()  + " tracks from album " + album.name);
                callback.result(trackPager.items);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });
    }

    public void getPlaylist(PlaylistSimple playlistSimple, OnSpotifyResults<Playlist> callback){
        service.getPlaylist(playlistSimple.owner.id, playlistSimple.id, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                Log.i(TAG, "received playlist " + playlist.name);
                callback.result(Collections.singletonList(playlist));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });
    }

    public void getAlbum(AlbumSimple albumSimple, OnSpotifyResults<Album> callback){
        service.getAlbum(albumSimple.id, new Callback<Album>() {
            @Override
            public void success(Album album, Response response) {
                Log.i(TAG, "received album " + album.name);
                callback.result(Collections.singletonList(album));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });
    }
}
