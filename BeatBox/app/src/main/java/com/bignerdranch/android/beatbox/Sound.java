package com.bignerdranch.android.beatbox;

import java.io.File;

public class Sound {
    private final String mName;
    private final String mAssetPath;

    public String getName() {
        return mName;
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public Sound(String assetPath) {
        mAssetPath = assetPath;
        mName = new File(mAssetPath).getName();
    }
}
