package com.bignerdranch.android.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";

    public static Fragment newInstance() {
        return new PhotoGalleryFragment();
    }

    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new FetchItemsTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photogallery, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photogallery_recycler_view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return v;
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, Gallery> {

        @Override
        protected Gallery doInBackground(Void... voids) {
            FlickrFetchr fetcher = new FlickrFetchr();

            try {
                Gallery gallery = fetcher.fetchGallery();
                Log.i(TAG, gallery.toString());
                return gallery;
            } catch (Exception ex) {
                Log.e(TAG, "Fetching failed", ex);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Gallery gallery) {
            setupAdapter(gallery);
        }
    }

    private void setupAdapter(Gallery gallery) {
        mRecyclerView.setAdapter(new GalleryAdapter(gallery));
    }

    private class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

        private final Gallery mGallery;

        public GalleryAdapter(Gallery gallery) {
            mGallery = gallery;
        }

        @Override
        public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = new TextView(getActivity());
            return new GalleryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GalleryViewHolder holder, int position) {
            holder.bindItem(mGallery.getPhotos().get(position));
        }

        @Override
        public int getItemCount() {
            return mGallery.getPhotos().size();
        }
    }

    private class GalleryViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;
        private GalleryItem mGalleryItem;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView;
        }

        public void bindItem(GalleryItem galleryItem) {
            mGalleryItem = galleryItem;
            mTextView.setText(galleryItem.getTitle());
        }
    }
}
