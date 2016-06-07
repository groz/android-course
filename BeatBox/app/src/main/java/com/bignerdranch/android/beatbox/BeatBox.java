package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_STREAMS = 5;

    private final AssetManager mAssetManager;
    private final List<Sound> mSounds;
    private final SoundPool mSoundPool;

    public BeatBox(Context context) {
        mAssetManager = context.getAssets();
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        mSounds = loadSounds();
    }

    private List<Sound> loadSounds() {
        ArrayList<Sound> result = new ArrayList<>();

        try {
            String[] filenames = mAssetManager.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + filenames.length + " files.");

            for (String filename : filenames) {
                Sound sound = load(filename);
                result.add(sound);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private Sound load(String filename) throws IOException {
        File f = new File(SOUNDS_FOLDER, filename);
        String assetPath = f.getPath();

        AssetFileDescriptor afd = mAssetManager.openFd(assetPath);

        int id = mSoundPool.load(afd, 1);
        return new Sound(assetPath, id);
    }

    public void play(Sound sound) {
        Integer id = sound.getId();
        if (id != null) {
            mSoundPool.play(id, 1, 1, 1, 0, 1);
        }
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    public void release() {
        mSoundPool.release();
    }
}
