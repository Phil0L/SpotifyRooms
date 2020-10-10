package com.pl.spotifyrooms.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pl.spotifyrooms.R;
import com.pl.spotifyrooms.util.AsynchronousImageLoader;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    public static final String TAG = "ResultAdapter";

    public List<?> items;
    private final Context context;
    private OnItemClickListener clickListener;

    public ResultAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }

    public void setItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.result_element, parent, false
                ));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object o = items.get(position);
        setAnimation(holder.itemView);
        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(position, o));

        /*
          Tracks
         */
        if (o instanceof Track) {
            Track track = (Track) o;
            // title
            if (track.name != null)
                holder.title.setText(track.name);

            // artist
            if (track.artists != null && !track.artists.isEmpty()) {
                List<String> artistNames = new ArrayList<>();
                track.artists.forEach(a -> artistNames.add(a.name));
                String artistString = Arrays.toString(artistNames.toArray()).replaceAll("[\\[\\]]", "");
                holder.artist.setText(artistString);
            }

            // info
            if (track.album != null && track.album.name != null)
                holder.info.setText(track.album.name);

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
                holder.length.setText(length);
            }

            // image
            try {
                holder.image.setVisibility(View.INVISIBLE);
                Image image = track.album.images.get(0);
                URL url = new URL(image.url);
                new AsynchronousImageLoader(holder.image).execute(url);
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
                holder.title.setText(playlistSimple.name);

            // artist
            if (playlistSimple.owner != null && playlistSimple.owner.display_name != null) {
                holder.artist.setText(playlistSimple.owner.display_name);
            }

            // length
            if (playlistSimple.tracks != null)
                holder.length.setText(playlistSimple.tracks.total + " Tracks");

            // image
            try {
                holder.image.setVisibility(View.INVISIBLE);
                Image image = playlistSimple.images.get(0);
                URL url = new URL(image.url);
                new AsynchronousImageLoader(holder.image).execute(url);
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
                holder.title.setText(albumSimple.name);

            // type
            if (albumSimple.album_type != null) {
                holder.artist.setText(albumSimple.album_type);
            }

            // image
            try {
                holder.image.setVisibility(View.INVISIBLE);
                Image image = albumSimple.images.get(0);
                URL url = new URL(image.url);
                new AsynchronousImageLoader(holder.image).execute(url);
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
                holder.title.setText(artist.name);

            // follower
            if (artist.followers != null) {
                holder.artist.setText(artist.followers.total + " followers");
            }

            // image
            try {
                holder.image.setVisibility(View.INVISIBLE);
                Image image = artist.images.get(0);
                URL url = new URL(image.url);
                new AsynchronousImageLoader(holder.image).execute(url);
            } catch (IOException | NullPointerException | IndexOutOfBoundsException ignored) {
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void setAnimation(@NotNull View viewToAnimate) {
        // If the bound view wasn't previously displayed on screen, it's animated
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_fall_in);
        viewToAnimate.startAnimation(animation);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView image;
        public final TextView title;
        public final TextView artist;
        public final TextView info;
        public final TextView length;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            artist = itemView.findViewById(R.id.artist);
            info = itemView.findViewById(R.id.info);
            length = itemView.findViewById(R.id.length);
        }
    }
}
