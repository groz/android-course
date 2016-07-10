package com.bignerdranch.android.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 13;

    private final ConcurrentHashMap<T, String> mRequestMap = new ConcurrentHashMap<>();
    private Handler mRequestHandler;
    private final Handler mResponseHandler;
    private final DownloadCompleteListener<T> mDownloadCompleteListener;
    private final LruCache<String, Bitmap> mCache = new LruCache<>(1000);

    public interface DownloadCompleteListener<T> {
        void onComplete(T target, Bitmap bmp);
    }

    public ThumbnailDownloader(String name, Handler responseHandler, DownloadCompleteListener<T> downloadCompleteListener) {
        super(name);
        mResponseHandler = responseHandler;
        mDownloadCompleteListener = downloadCompleteListener;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mRequestHandler = new RequestHandler<T>(mResponseHandler, mDownloadCompleteListener, mRequestMap, mCache);
    }

    public void queueDownload(T target, String url) {
        Bitmap inCache = mCache.get(url);

        if (inCache != null) {
            mDownloadCompleteListener.onComplete(target, inCache);
        } else {
            Log.d(TAG, "Queued: " + url);
            mRequestMap.put(target, url);
            Message msg = mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target);
            mRequestHandler.sendMessage(msg);
        }
    }

    private static class RequestHandler<T> extends Handler {
        private final Handler mResponseHandler;
        private final DownloadCompleteListener<T> mListener;
        private final ConcurrentHashMap<T, String> mRequestMap;
        private final LruCache<String, Bitmap> mCache;

        public RequestHandler(Handler responseHandler, DownloadCompleteListener<T> listener,
                              ConcurrentHashMap<T, String> requestMap, LruCache<String, Bitmap> cache) {
            mResponseHandler = responseHandler;
            mListener = listener;
            mRequestMap = requestMap;
            mCache = cache;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "RequestHandler.handleMessage");

            if (msg.what == MESSAGE_DOWNLOAD) {
                final T target = (T) msg.obj;
                final String url = mRequestMap.get(target);

                Log.d(TAG, "Url: " + url);

                if (url == null)
                    return;

                final Bitmap bmp = getImage(url);

                mResponseHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "ResponseHandler.handleMessage");
                        if (mRequestMap.get(target) != url) {
                            Log.d(TAG, "Request doesn't equal url");
                            return;
                        }
                        mRequestMap.remove(target);
                        mListener.onComplete(target, bmp);
                    }
                });
            } else {
                Log.e(TAG, "Unknown `what` code: " + msg.what);
            }
        }

        private Bitmap getImage(String url) {
            Bitmap bmp = mCache.get(url);

            if (bmp == null) {
                byte[] imgBytes = new byte[0];
                try {
                    imgBytes = new FlickrFetchr().fetchImage(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bmp = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                mCache.put(url, bmp);
            } else {
                Log.d(TAG, url + " found in cache");
            }

            return bmp;
        }
    }
}
