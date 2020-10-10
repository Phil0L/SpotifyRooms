package com.pl.spotifyrooms.ui.main;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PageViewModel extends ViewModel {

    public static final String TAG = "PageViewModel";

    private final List<Observer> observers = new ArrayList<>();
    private List<?> list = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
        Log.i(TAG, "Observer added");
    }

    public void setList(List<?> newList) {
        list = newList;
        notifyObservers();
    }

    private void notifyObservers() {
        observers.forEach(o -> o.changed(list));
        Log.i("Observer", "notifyObservers");
    }

    public interface Observer {

        void changed(List<?> objects);

    }
}