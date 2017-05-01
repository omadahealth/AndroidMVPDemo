package com.mvp.moviedbapi.activities;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mvp.moviedbapi.R;
import com.mvp.moviedbapi.adapters.MovieSearchAdapter;
import com.mvp.moviedbapi.interfaces.MainActivityContract;
import com.mvp.moviedbapi.models.apis.SearchResults;
import com.mvp.moviedbapi.presenters.MainActivityPresenter;

/**
 * The main {@link android.app.Activity}
 * Improvements:
 */
public class MainActivity extends AppCompatActivity implements MainActivityContract.MainActivityView {

    /**
     * {@link Log} tag of this {@link MainActivity}
     */
    private static final String TAG = "MainActivity";

    /**
     * The {@link MainActivityPresenter} for this view
     */
    private MainActivityPresenter mPresenter;

    private EditText mEditTextSearch;
    private RecyclerView mRecyclerView;
    private Button mNextPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainActivityPresenter();
        mPresenter.attach(this);

        mEditTextSearch = (EditText) findViewById(R.id.main_activity_edit_text);
        mNextPageButton = (Button) findViewById(R.id.main_activity_next_page_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_activity_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.main_activity_search_button).setOnClickListener(v -> mPresenter.searchMovie(mEditTextSearch.getText().toString(), 1));
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

    @Override
    public void showToast(@StringRes int idString) {
        Toast.makeText(this, idString, Toast.LENGTH_LONG).show();
    }

    /**
     * Create / update the {@link RecyclerView#getAdapter()}
     *
     * @param searchResults The results obtained in {@link MainActivityPresenter#searchMovie(String, int)}
     */
    @Override
    public void updateMovieAdapter(SearchResults searchResults) {
        if (mRecyclerView.getAdapter() instanceof MovieSearchAdapter) {
            //Already an adapter, just needs to update
            MovieSearchAdapter movieSearchAdapter = (MovieSearchAdapter) mRecyclerView.getAdapter();
            movieSearchAdapter.setSearchResults(searchResults);
            movieSearchAdapter.notifyDataSetChanged();
        } else {
            //Create a new adapter
            mRecyclerView.setAdapter(new MovieSearchAdapter(searchResults));
        }
    }

    @Override
    public void setUpOnNextPageButton(String text, int visibility, int page) {
        mNextPageButton.setVisibility(visibility);
        mNextPageButton.setOnClickListener(v -> mPresenter.searchMovie(text, page));
    }
}
