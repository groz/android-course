package com.bignerdranch.android.photogallery;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

public class FlickrFetchr {
    private static final String TAG = "FlickrFetchr";
    private static final String BASE_URL = "https://api.flickr.com/services/rest";
    private static final String API_KEY = "1437d7f279781b63b2cb669d320ac628";


    public String getUrlString(String url) throws IOException {
        return IOUtils.toString(new URL(url));
    }

    public String fetch(String method) throws IOException {
        URL url = new URL(fromMethod(method));
        Log.d(TAG, fromMethod(method));
        return IOUtils.toString(url);
    }

    public Gallery fetchGallery() throws IOException {
        String jsonData = fetch("flickr.photos.getRecent");
        ObjectMapper mapper = new ObjectMapper();
        GalleryRoot gallery = mapper.readValue(jsonData, GalleryRoot.class);
        return gallery.getPhotos();
    }

    private String fromMethod(String method) {
        return String.format("%s?method=%s&api_key=%s&format=json&nojsoncallback=1&extras=url_s",
                BASE_URL,
                method,
                API_KEY);
    }
}
