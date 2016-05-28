package com.bignerdranch.android.beatbox;

import android.support.v4.app.Fragment;
import android.os.Bundle;

public class BeatBoxActivity extends SingleFragmentActivity {
    @Override
    protected String getLogTag() {
        return "BeatBoxActivity";
    }

    @Override
    protected Fragment createFragment() {
        return BeatBoxFragment.newInstance();
    }
}
