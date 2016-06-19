package com.bignerdranch.android.photogallery;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
    "id": "27151451474",
    "owner": "134231216@N06",
    "secret": "28b0b108d5",
    "server": "7422",
    "farm": 8,
    "title": "SEC 330 Week 2 Industrial Safety Improvement Strategy Presentation",
    "ispublic": 1,
    "isfriend": 0,
    "isfamily": 0,
    "url_s": "https:\/\/farm8.staticflickr.com\/7422\/27151451474_28b0b108d5_m.jpg",
    "height_s": "116",
    "width_s": "240"
 */
public class GalleryItem {
    public long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public int getServer() {
        return server;
    }

    public int getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public boolean isFamily() {
        return isFamily;
    }

    public String getUrl_s() {
        return url_s;
    }

    public Integer getHeight_s() {
        return height_s;
    }

    public Integer getWidth_s() {
        return width_s;
    }

    private long id;
    private String owner;
    private String secret;
    private int server;
    private int farm;
    private String title;
    @JsonProperty("ispublic")
    private boolean isPublic;
    @JsonProperty("isfriend")
    private boolean isFriend;
    @JsonProperty("isfamily")
    private boolean isFamily;
    private String url_s;
    private Integer height_s;
    private Integer width_s;
}
