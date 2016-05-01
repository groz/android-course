package com.bignerdranch.android.criminalintent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class PictureUtils {

    public static Bitmap loadScaledBitmap(File file, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        float ratio = 1;

        if (srcWidth > width || srcHeight > height) {
            ratio = srcWidth > srcHeight
                    ? srcHeight / height
                    : srcWidth / width;
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = Math.round(ratio);
        return BitmapFactory.decodeFile(file.getPath(), options);
    }
}
