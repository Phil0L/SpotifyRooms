package com.pl.spotifyrooms.util;

import android.util.Log;

import com.pl.spotifyrooms.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpotifySearcher {

    public static final String TAG = "SpotifySearcher";

    private final String query;
    private final SpotifyService service;

    public SpotifySearcher(String query) {
        this.query = query;
        this.service = LoginActivity.spotifyService;
    }

    public void getTracks(OnSpotifyResults<Track> callback) {
        service.searchTracks(query, new Callback<TracksPager>() {
            @Override
            public void success(TracksPager tracks, Response response) {
                List<Track> list = tracks.tracks.items;
                Log.i(TAG, "Found " + list.size() + " tracks");
                callback.result(list);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });
    }

    public void getPlaylists(OnSpotifyResults<PlaylistSimple> callback) {
        service.searchPlaylists(query, new Callback<PlaylistsPager>() {
            @Override
            public void success(PlaylistsPager playlists, Response response) {
                List<PlaylistSimple> list = playlists.playlists.items;
                Log.i(TAG, "Found " + list.size() + " playlists");
                callback.result(list);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });
    }

    public void getAlbums(OnSpotifyResults<AlbumSimple> callback) {
        service.searchAlbums(query, new Callback<AlbumsPager>() {
            @Override
            public void success(AlbumsPager albums, Response response) {
                List<AlbumSimple> list = albums.albums.items;
                Log.i(TAG, "Found " + list.size() + " albums");
                callback.result(albums.albums.items);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });
    }

    public void getArtists(OnSpotifyResults<Artist> callback) {
        service.searchArtists(query, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artists, Response response) {
                List<Artist> list = artists.artists.items;
                Log.i(TAG, "Found " + list.size() + " artists");
                callback.result(list);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });
    }

}
