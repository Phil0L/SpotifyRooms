package com.pl.spotifyrooms.ui.room.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.pl.spotifyrooms.R;

public class HistoryFragment extends Fragment {

    public static final String TAG = "HistoryFragment";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert getActivity() != null;
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.i(TAG, "backPressed");
        if (item.getItemId() == android.R.id.home){
            assert getActivity() != null;
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}