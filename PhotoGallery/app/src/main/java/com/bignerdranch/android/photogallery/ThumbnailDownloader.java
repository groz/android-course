package com.bignerdranch.android.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 13;

    private final ConcurrentHashMap<T, String> mRequestMap = new ConcurrentHashMap<>();
    private Handler mRequestHandler;
    private final Handler mResponseHandler;
    private final DownloadCompleteListener<T> mDownloadCompleteListener;

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
        mRequestHandler = new RequestHandler<T>(mResponseHandler, mDownloadCompleteListener, mRequestMap);
    }

    public void queueDownload(T target, String url) {
        Log.d(TAG, "Queued: "+url);
        mRequestMap.put(target, url);
        Message msg = mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target);
        mRequestHandler.sendMessage(msg);
    }

    private static class RequestHandler<T> extends Handler {
        private final Handler mResponseHandler;
        private final DownloadCompleteListener<T> mListener;
        private final ConcurrentHashMap<T, String> mRequestMap;

        public RequestHandler(Handler responseHandler, DownloadCompleteListener<T> listener,
                              ConcurrentHashMap<T, String> requestMap) {
            mResponseHandler = responseHandler;
            mListener = listener;
            mRequestMap = requestMap;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "RequestHandler.handleMessage");

            if (msg.what == MESSAGE_DOWNLOAD) {
                final T target = (T)msg.obj;
                final String url = mRequestMap.get(target);

                Log.d(TAG, "Url: " + url);

                if (url == null)
                    return;

                try {
                    byte[] imgBytes = new FlickrFetchr().fetchImage(url);
                    final Bitmap bmp = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);

                    mResponseHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "ResponseHandler.handleMessage");
                            if (!mRequestMap.get(target).equals(url)) {
                                return;
                            }
                            mRequestMap.remove(target);
                            mListener.onComplete(target, bmp);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "Unknown `what` code: " + msg.what);
            }
        }
    }
}
