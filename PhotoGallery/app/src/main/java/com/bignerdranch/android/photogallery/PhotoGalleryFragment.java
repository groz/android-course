package com.bignerdranch.android.photogallery;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    private List<GalleryItem> mGalleryItems;
    private GridLayoutManager mLayoutManager;

    public static Fragment newInstance() {
        return new PhotoGalleryFragment();
    }

    private RecyclerView mRecyclerView;
    private AsyncTask mFetchTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mFetchTask = new FetchItemsTask().execute();
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
                Gallery gallery = fetcher.fetchGallery(mPage, mPerPage);
                Log.i(TAG, gallery.toString());
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
                mRecyclerView.getAdapter().notifyDataSetChanged();
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
            holder.bindDrawable(drawable, item.getTitle());
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
            mImageView = (ImageView)itemView.findViewById(R.id.gallery_item_image);
        }

        public void bindDrawable(Drawable drawable, String text) {
            mImageView.setImageDrawable(drawable);
            mImageView.setContentDescription(text);
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

                Log.d(TAG, String.format(
                   "Visible items: %s, past items: %s, total items: %s, gallery size: %s",
                        visibleItemCount, pastVisiblesItems, totalItemCount, mGalleryItems.size()
                ));

                if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                    int perPage = 100;
                    int page = mGalleryItems.size() / perPage + 1;

                    if (mFetchTask.getStatus() == AsyncTask.Status.FINISHED) {
                        mFetchTask = new FetchItemsTask(page, perPage).execute();
                    }
                }
            }
        }
    }
}
