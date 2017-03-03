package com.example.manan.bookshelf;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.BaseTransientBottomBar.LENGTH_INDEFINITE;
import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    ImageView imageView;
    TextView emptyText;
    private String Book_Request_Url;
    private ProgressBar progressBar;
    private BookAdapter mBookAdapter;
    private EditText edit_query;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageResource(R.drawable.bakground);
        imageView.setVisibility(View.VISIBLE);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.snackbar);

        emptyText = (TextView) findViewById(R.id.emptyText);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            Snackbar.make(coordinatorLayout, "Internet Connected", Snackbar.LENGTH_LONG).show();
            Log.i("connection", "connection created");
        } else {
            imageView.setImageResource(R.drawable.intenet_connection);
            Snackbar.make(coordinatorLayout, "Intenet Connection Lost", LENGTH_INDEFINITE).setAction("SETTINGS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
            }).setActionTextColor(ContextCompat.getColor(this, R.color.white)).show();
        }

        edit_query = (EditText) findViewById(R.id.edit_query);

        Button searchButton = (Button) findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(BookActivity.this, R.color.white),
                        android.graphics.PorterDuff.Mode.MULTIPLY);
                progressBar.setVisibility(View.VISIBLE);
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {

                    imageView.setVisibility(View.GONE);

                    if (TextUtils.isEmpty(edit_query.getText().toString())) {
                        edit_query.setError("Please Enter Book Name");
                    } else {
                        String query = edit_query.getText().toString().trim();
                        Book_Request_Url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResult=10&orderBy=newest";
                        Log.i("url", Book_Request_Url);
                        LoaderManager loaderManager = getLoaderManager();
                        loaderManager.initLoader(0, null, BookActivity.this);
                        loaderManager.restartLoader(0, null, BookActivity.this);
                        Log.i("Network ", "connected");
                    }
                } else {
                    imageView.setImageResource(R.drawable.intenet_connection);
                    imageView.setVisibility(View.VISIBLE);
                    mBookAdapter.clear();
                    Snackbar.make(coordinatorLayout, "Intenet Connection Lost", LENGTH_INDEFINITE).setAction("SETTINGS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    }).setActionTextColor(ContextCompat.getColor(BookActivity.this, R.color.white)).show();

                }
            }
        });

        ListView bookList = (ListView) findViewById(R.id.list_item);
        mBookAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookList.setAdapter(mBookAdapter);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Book currentBook = mBookAdapter.getItem(position);

                Uri bookUri = Uri.parse(currentBook.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, Book_Request_Url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        mBookAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mBookAdapter.addAll(books);
            emptyText.setVisibility(View.GONE);

        } else {
            emptyText.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mBookAdapter.clear();
    }
}
