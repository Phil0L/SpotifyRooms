package com.pl.spotifyrooms.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pl.spotifyrooms.R;

import org.jetbrains.annotations.NotNull;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = "SectionsPagerAdapter";

    public enum Tabs{
        SONGS(R.string.tab_text_1, "Songs", 0),
        PLAYLISTS(R.string.tab_text_2, "Playlists", 1),
        ALBUMS(R.string.tab_text_3, "Albums", 2),
        ARTISTS(R.string.tab_text_4, "Artists", 3);

        @StringRes
        public int title;
        public String helpName;
        public int id;

        Tabs(@StringRes int title, String helpName, int id) {
            this.title = title;
            this.helpName = helpName;
            this.id = id;
        }
    }

    public Fragment[] fragments = new Fragment[Tabs.values().length];

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        ResultFragment fragment = new ResultFragment(Tabs.values()[position]);
        fragments[position] = fragment;
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(Tabs.values()[position].title);
    }

    @Override
    public int getCount() {
        return Tabs.values().length;
    }
}