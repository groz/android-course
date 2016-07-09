package com.bignerdranch.android.photogallery;

import android.os.HandlerThread;
import android.util.Log;

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";

    public ThumbnailDownloader(String name) {
        super(name);
    }

    public void queueDownload(T target, String url) {
        Log.d(TAG, "Queued: "+url);
    }
}
