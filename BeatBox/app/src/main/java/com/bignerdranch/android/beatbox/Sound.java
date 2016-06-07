package com.bignerdranch.android.beatbox;

import java.io.File;

public class Sound {
    private final String mName;
    private final String mAssetPath;

    private final Integer mId;

    public String getName() {
        return mName;
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public Integer getId() {
        return mId;
    }

    public Sound(String assetPath) {
        this(assetPath, null);
    }

    public Sound(String assetPath, Integer id) {
        mAssetPath = assetPath;
        mName = new File(mAssetPath).getName();
        mId = id;
    }
}
