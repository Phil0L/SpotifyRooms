package com.pl.spotifyrooms.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

@SuppressLint("StaticFieldLeak")
public class AsynchronousImageLoader extends AsyncTask<URL, Void, Bitmap> {

    public static final String TAG = "AsynchronousImageLoader";

    private final ImageView image;

    public AsynchronousImageLoader(ImageView view){
        this.image = view;
    }

    @Override
    protected Bitmap doInBackground(URL... urls) {
        try {
            return BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
        image.setVisibility(View.VISIBLE);
    }
}
