package com.bignerdranch.android.nerdlauncher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NerdLauncherFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public static Fragment newInstance() {
        return new NerdLauncherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nerdlauncher, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.fragment_nerdlauncher_recycler_view);
        return view;
    }
}
