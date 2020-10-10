package com.pl.spotifyrooms.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pl.spotifyrooms.R;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

import static com.pl.spotifyrooms.ui.main.BottomInfoDialog.OPEN_SPOTIFY;
import static com.pl.spotifyrooms.ui.main.BottomInfoDialog.QUEUE;
import static com.pl.spotifyrooms.ui.main.BottomInfoDialog.QUEUE_ALL;
import static com.pl.spotifyrooms.ui.main.BottomInfoDialog.QUEUE_FRONT;
import static com.pl.spotifyrooms.ui.main.BottomInfoDialog.SHARE;
import static com.pl.spotifyrooms.ui.main.BottomInfoDialog.VIEW;
import static com.pl.spotifyrooms.ui.main.BottomInfoDialog.VIEW_ALBUM;
import static com.pl.spotifyrooms.ui.main.BottomInfoDialog.VIEW_ARTIST;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResultFragment extends Fragment {

    public static final String TAG = "ResultFragment";

    public PageViewModel pageViewModel;
    public ResultAdapter listAdapter;
    public SectionsPagerAdapter.Tabs tabType;

    public ResultFragment(SectionsPagerAdapter.Tabs tabType) {
        this.tabType = tabType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        pageViewModel.addObserver(objects -> {
            Log.i(TAG, "updated list of results");
            listAdapter.items = objects;
            listAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.list);
        listAdapter = new ResultAdapter(getContext());
        listAdapter.setItemClickListener((position, object) -> openBottomInfo(object));
        recyclerView.setAdapter(listAdapter);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        return root;
    }

    private void openBottomInfo(Object o) {
        BottomInfoDialog bottomInfoDialog = null;
        if (o instanceof Track)
            bottomInfoDialog = new BottomInfoDialog(o, QUEUE, QUEUE_FRONT, VIEW_ARTIST, VIEW_ALBUM, OPEN_SPOTIFY, SHARE);
        if (o instanceof PlaylistSimple)
            bottomInfoDialog = new BottomInfoDialog(o, VIEW, QUEUE_ALL, OPEN_SPOTIFY, SHARE);
        if (o instanceof AlbumSimple)
            bottomInfoDialog = new BottomInfoDialog(o, VIEW, QUEUE_ALL, VIEW_ARTIST, OPEN_SPOTIFY, SHARE);
        if (o instanceof Artist)
            bottomInfoDialog = new BottomInfoDialog(o, VIEW, OPEN_SPOTIFY, SHARE);
        if (bottomInfoDialog != null)
            bottomInfoDialog.show(requireActivity().getSupportFragmentManager(), "Bottom_Info");
    }
}