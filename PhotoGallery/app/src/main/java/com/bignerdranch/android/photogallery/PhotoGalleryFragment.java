package com.bignerdranch.android.photogallery;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    private List<GalleryItem> mGalleryItems;
    private GridLayoutManager mLayoutManager;
    private ThumbnailDownloader<GalleryViewHolder> mThumbnailDownloader;
    private String mSearchQuery = null;

    public static Fragment newInstance() {
        return new PhotoGalleryFragment();
    }

    private RecyclerView mRecyclerView;
    private AsyncTask mFetchTask;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                mSearchQuery = null;
                PersistedConfig.setQuery(getActivity(), mSearchQuery);
                mGalleryItems.clear();
                updateItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchQuery = query;
                PersistedConfig.setQuery(getActivity(), mSearchQuery);

                if (mGalleryItems != null) {
                    mGalleryItems.clear();
                }

                updateItems();
                Log.d(TAG, "Searching for " + mSearchQuery + "...");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setQuery(PersistedConfig.getQuery(getActivity()), true);
    }

    void updateItems() {
        mFetchTask = new FetchItemsTask().execute();
    }

    void updateItems(int page, int perPage) {
        mFetchTask = new FetchItemsTask(page, perPage).execute();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        updateItems();
        mThumbnailDownloader = new ThumbnailDownloader<>("ThumbnailDownloaderThread",
                new Handler(),
                new ThumbnailDownloader.DownloadCompleteListener<GalleryViewHolder>() {
                    @Override
                    public void onComplete(GalleryViewHolder target, Bitmap bmp) {
                        Drawable img = new BitmapDrawable(getResources(), bmp);
                        target.bindDrawable(img);
                    }
                });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mFetchTask != null) {
            mFetchTask.cancel(true);
            mFetchTask = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photogallery, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photogallery_recycler_view);

        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new GalleryScrollListener());

        if (mGalleryItems != null) {
            setupAdapter(mGalleryItems);
        }

        return v;
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, Gallery> {

        private final int mPage;
        private final int mPerPage;

        public FetchItemsTask(int page, int perPage) {
            mPage = page;
            mPerPage = perPage;
        }

        public FetchItemsTask() {
            this(1, 100);
        }

        @Override
        protected Gallery doInBackground(Void... voids) {
            FlickrFetchr fetcher = new FlickrFetchr();

            try {
                Gallery gallery;

                if (mSearchQuery == null || mSearchQuery.equals("")) {
                    gallery = fetcher.fetchGallery(mPage, mPerPage);
                } else {
                    gallery = fetcher.fetchGalleryByTopic(mSearchQuery, mPage, mPerPage);
                }

                return gallery;
            } catch (Exception ex) {
                Log.e(TAG, "Fetching failed", ex);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Gallery gallery) {
            setupAdapter(gallery.getPhotos());
        }
    }

    private void setupAdapter(List<GalleryItem> galleryItems) {
        if (isAdded()) {
            if (mGalleryItems == null) {
                mGalleryItems = galleryItems;
                mRecyclerView.setAdapter(new GalleryAdapter(mGalleryItems));
            } else {
                // TODO: filter out duplicate items
                mGalleryItems.addAll(galleryItems);

                if (mRecyclerView.getAdapter() == null) {
                    mRecyclerView.setAdapter(new GalleryAdapter(mGalleryItems));
                } else {
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }
    }

    private class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

        private final List<GalleryItem> mGalleryItems;

        public GalleryAdapter(List<GalleryItem> gallery) {
            mGalleryItems = gallery;
        }

        @Override
        public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.gallery_item, parent, false);
            return new GalleryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GalleryViewHolder holder, int position) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_gallery_image_placeholder);
            GalleryItem item = mGalleryItems.get(position);

            if (item.getUrl_s() != null) {
                mThumbnailDownloader.queueDownload(holder, item.getUrl_s());
            }
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    private class GalleryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.gallery_item_image);
        }

        public void bindDrawable(Drawable drawable) {
            mImageView.setImageDrawable(drawable);
        }
    }

    private class GalleryScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (dy > 0) {
                int visibleItemCount = mRecyclerView.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                    Log.d(TAG, String.format(
                            "Visible items: %s, past items: %s, total items: %s, gallery size: %s",
                            visibleItemCount, pastVisiblesItems, totalItemCount, mGalleryItems.size()
                    ));

                    int perPage = 100;
                    int page = mGalleryItems.size() / perPage + 1;

                    if (mFetchTask == null || mFetchTask.getStatus() == AsyncTask.Status.FINISHED) {
                        updateItems(page, perPage);
                    }
                }
            }
        }
    }
}
