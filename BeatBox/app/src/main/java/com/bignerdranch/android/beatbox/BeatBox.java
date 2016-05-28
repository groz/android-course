package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";

    private final AssetManager mAssetManager;
    private final List<Sound> mSounds;

    public BeatBox(Context context) {
        mAssetManager = context.getAssets();
        mSounds = loadSounds();
    }

    private List<Sound> loadSounds() {
        ArrayList<Sound> result = new ArrayList<>();

        try {
            String[] filenames = mAssetManager.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + filenames.length + " files.");

            for (String filename : filenames) {
                File f = new File(SOUNDS_FOLDER, filename);
                result.add(new Sound(f.getPath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Sound> getSounds() {
        return mSounds;
    }
}
