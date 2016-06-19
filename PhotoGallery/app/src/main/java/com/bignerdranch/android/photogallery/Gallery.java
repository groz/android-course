package com.bignerdranch.android.photogallery;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/*
	"photos": {
		"page": 1,
		"pages": 10,
		"perpage": 100,
		"total": 1000,
		"photo": [{
		}]
    },
    "stat": "ok"
*/
public class Gallery {
    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getTotal() {
        return total;
    }

    public List<GalleryItem> getPhoto() {
        return photo;
    }

    private int page;
    private int pages;

    @JsonProperty("perpage")
    private int perPage;

    private int total;
    private List<GalleryItem> photo;
}
