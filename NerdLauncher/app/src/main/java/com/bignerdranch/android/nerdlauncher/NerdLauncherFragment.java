package com.bignerdranch.android.nerdlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NerdLauncherFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PackageManager mPackageManager;

    public static Fragment newInstance() {
        return new NerdLauncherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nerdlauncher, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_nerdlauncher_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPackageManager = getActivity().getPackageManager();

        List<ResolveInfo> activityInfos = loadActivityInfos();
        mRecyclerView.setAdapter(new ActivityAdapter(activityInfos));
        return view;
    }

    private List<ResolveInfo> loadActivityInfos() {
        Intent i = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        return mPackageManager.queryIntentActivities(i, 0);
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> mActivityInfos;

        public ActivityAdapter(List<ResolveInfo> activityInfos) {
            mActivityInfos = activityInfos;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View listItem = inflater.inflate(R.layout.list_item_activity_info, parent, false);
            return new ActivityHolder(listItem);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            holder.bindActivity(mActivityInfos.get(position));
        }

        @Override
        public int getItemCount() {
            return mActivityInfos.size();
        }
    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ResolveInfo mActivityInfo;
        private TextView mTextView;
        private ImageView mImageView;

        public ActivityHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.list_item_activity_info_text);
            mImageView = (ImageView) itemView.findViewById(R.id.list_item_activity_info_image);

            mTextView.setOnClickListener(this);
        }

        public void bindActivity(ResolveInfo activityInfo) {
            mActivityInfo = activityInfo;

            String name = mActivityInfo.activityInfo.loadLabel(mPackageManager).toString();
            Drawable icon = mActivityInfo.loadIcon(mPackageManager);
            mTextView.setText(name);
            mImageView.setImageDrawable(icon);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(Intent.ACTION_MAIN).setClassName(
                    mActivityInfo.activityInfo.applicationInfo.packageName,
                    mActivityInfo.activityInfo.name
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        }
    }
}
