package com.bignerdranch.android.photogallery;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FlickrFetchr {
    private static final String TAG = "FlickrFetchr";
    private static final String BASE_URL = "https://api.flickr.com/services/rest";
    private static final String API_KEY = "1437d7f279781b63b2cb669d320ac628";

    public Gallery fetchGallery(int page, int perPage) throws IOException {
        String jsonData = fetch("flickr.photos.getRecent", page, perPage,
                new HashMap<String, String>() {{
                    put("extras", "url_s");
                }});

        return toGallery(jsonData);
    }

    public Gallery fetchGalleryByTopic(final String topic, int page, int perPage) throws IOException {
        Log.d(TAG, "FlickrFetchr searching for " + topic);
        String jsonData = fetch("flickr.photos.search", page, perPage,
                new HashMap<String, String>() {{
                    put("text", topic);
                    put("extras", "url_s");
                }});

        return toGallery(jsonData);
    }

    private Gallery toGallery(String jsonData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GalleryRoot gallery = mapper.readValue(jsonData, GalleryRoot.class);
        return gallery.getPhotos();
    }

    public Gallery fetchGallery() throws IOException {
        return fetchGallery(1, 100);
    }

    public byte[] fetchImage(String url) throws IOException {
        return IOUtils.toByteArray(new URL(url));
    }

    private String fetch(String method, int page, int perPage) throws IOException {
        return fetch(method, page, perPage, new HashMap<String, String>());
    }

    private String fetch(String method, int page, int perPage, Map<String, String> args) throws IOException {
        String str = String.format("%s?method=%s&api_key=%s&format=json&nojsoncallback=1&page=%s&per_page=%s",
                BASE_URL,
                method,
                API_KEY,
                page,
                perPage);

        StringBuilder result = new StringBuilder(str);

        for (Map.Entry<String, String> entry : args.entrySet()) {
            result.append('&');
            result.append(entry.getKey());
            result.append('=');
            result.append(entry.getValue());
        }

        Log.d(TAG, "Sending request to " + result.toString());

        URL url = new URL(result.toString());
        return IOUtils.toString(url);
    }
}
