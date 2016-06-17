package com.bignerdranch.android.photogallery;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

public class FlickrFetchr {

    public String getUrlString(String url) throws IOException {
        return IOUtils.toString(new URL(url));
    }
}
