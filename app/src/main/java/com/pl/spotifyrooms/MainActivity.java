package com.pl.spotifyrooms;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pl.spotifyrooms.ui.main.PageViewModel;
import com.pl.spotifyrooms.ui.main.ResultFragment;
import com.pl.spotifyrooms.ui.main.SectionsPagerAdapter;
import com.pl.spotifyrooms.ui.main.QuickControls;
import com.pl.spotifyrooms.util.Room;
import com.pl.spotifyrooms.util.SpotifySearcher;

import static com.pl.spotifyrooms.ui.main.SectionsPagerAdapter.Tabs.ALBUMS;
import static com.pl.spotifyrooms.ui.main.SectionsPagerAdapter.Tabs.ARTISTS;
import static com.pl.spotifyrooms.ui.main.SectionsPagerAdapter.Tabs.PLAYLISTS;
import static com.pl.spotifyrooms.ui.main.SectionsPagerAdapter.Tabs.SONGS;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        View controls = findViewById(R.id.controls);
        new QuickControls(this, controls);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Room.currentRoom = new Room();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem menuItemSearch = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setOnQueryTextListener(searchListener);
        return true;
    }

    private final SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.d("Search", "Searched: " + query);
            searchInSpotify(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.d("Search", "Typed: " + newText);
            return false;
        }
    };

    public void searchInSpotify(String query) {
        SpotifySearcher searcher = new SpotifySearcher(query);
        searcher.getTracks(tracks -> {
            ResultFragment fragment = (ResultFragment) sectionsPagerAdapter.fragments[SONGS.id];
            PageViewModel model = fragment.pageViewModel;
            model.setList(tracks);
        });
        searcher.getPlaylists(playlists -> {
            ResultFragment fragment = (ResultFragment) sectionsPagerAdapter.fragments[PLAYLISTS.id];
            PageViewModel model = fragment.pageViewModel;
            model.setList(playlists);
        });
        searcher.getAlbums(albums -> {
            ResultFragment fragment = (ResultFragment) sectionsPagerAdapter.fragments[ALBUMS.id];
            PageViewModel model = fragment.pageViewModel;
            model.setList(albums);
        });
        searcher.getArtists(artists -> {
            ResultFragment fragment = (ResultFragment) sectionsPagerAdapter.fragments[ARTISTS.id];
            PageViewModel model = fragment.pageViewModel;
            model.setList(artists);
        });

    }
}