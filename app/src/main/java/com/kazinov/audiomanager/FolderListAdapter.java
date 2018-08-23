package com.kazinov.audiomanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class FolderListAdapter extends BaseAdapter {
    private List<AudioFolder> mFoldersList;
    private Activity mActivity;

    public FolderListAdapter(Activity activity, List<AudioFolder> foldersList) {
        mActivity = activity;
        mFoldersList = foldersList;
    }

    private static class ViewHolder {
        TextView folderName;
    }

    void update(List<AudioFolder> foldersList) {
        mFoldersList = foldersList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mFoldersList == null) {
            return 0;
        }
        return mFoldersList.size();
    }

    @Override
    public AudioFolder getItem(int i) {
        if (mFoldersList == null) {
            return null;
        }
        return mFoldersList.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (mFoldersList == null) {
            return 0;
        }
        return getItem(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (mFoldersList == null) {
            return null;
        }

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.folder_row, viewGroup, false);

            final ViewHolder holder = new ViewHolder();
            holder.folderName = (TextView) view.findViewById(R.id.folderName);
            view.setTag(holder);
        }

        final AudioFolder folder = getItem(i);
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.folderName.setText(folder.title);

        return view;

    }
}