package com.mvp.moviedbapi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mvp.moviedbapi.R;
import com.mvp.moviedbapi.models.apis.SearchResults;
import com.mvp.moviedbapi.constants.Urls;
import com.squareup.picasso.Picasso;

import static com.mvp.moviedbapi.constants.Urls.IMAGE_SIZE_HD;

/**
 * Created by olivier.goutay on 4/18/17.
 * The adapter for {@link com.mvp.moviedbapi.activities.MainActivity#mRecyclerView}
 */
public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.ViewHolder> {

    /**
     * Stores the {@link com.mvp.moviedbapi.models.apis.MovieResult}
     */
    private SearchResults mSearchResults;

    /**
     * Constructor of this adapter
     *
     * @param searchResults The results queried on {@link com.mvp.moviedbapi.activities.MainActivity}
     */
    public MovieSearchAdapter(SearchResults searchResults) {
        this.mSearchResults = searchResults;
    }

    /**
     * The {@link ViewHolder} that keeps references towards the views to improve performances
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        ViewHolder(View view) {
            super(view);
            this.mImageView = (ImageView) view.findViewById(R.id.view_movie_cell_image_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_movie_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder == null || holder.mImageView == null || holder.mImageView.getContext() == null) {
            return;
        }

        Picasso.with(holder.mImageView.getContext()).load(getUrlFromResults(position)).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (mSearchResults == null || mSearchResults.getResults() == null) {
            return 0;
        }
        return mSearchResults.getResults().size();
    }

    /**
     * Returns the formatted Url from {@link #mSearchResults}
     *
     * @param position The position we are querying for
     */
    private String getUrlFromResults(int position) {
        if (mSearchResults == null || mSearchResults.getResults() == null || mSearchResults.getResults().size() < position) {
            return "";
        }

        //TODO handle different phone resolutions (download images which sizes are close to the device resolution, for better performances)
        StringBuilder stringBuilder = new StringBuilder(Urls.IMAGE_BASE_URL);
        stringBuilder.append(IMAGE_SIZE_HD).append(mSearchResults.getResults().get(position).getPosterPath());
        return stringBuilder.toString();
    }

    public void setSearchResults(SearchResults searchResults) {
        this.mSearchResults = searchResults;
    }
}
