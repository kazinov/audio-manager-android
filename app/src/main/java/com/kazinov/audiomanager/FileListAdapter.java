package com.kazinov.audiomanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FileListAdapter extends BaseAdapter {
    private List<AudioFile> mFileList;
    private Activity mActivity;

    public FileListAdapter(Activity activity, List<AudioFile> fileList) {
        mActivity = activity;
        mFileList = fileList;
    }

    private static class ViewHolder {
        TextView fileName;
        ImageView fileImage;
    }

    void update(List<AudioFile> fileList) {
        mFileList = fileList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mFileList == null) {
            return 0;
        }
        return mFileList.size();
    }

    @Override
    public AudioFile getItem(int i) {
        if (mFileList == null) {
            return null;
        }
        return mFileList.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (mFileList == null) {
            return 0;
        }
        return getItem(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (mFileList == null) {
            return null;
        }

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.file_row, viewGroup, false);

            final ViewHolder holder = new ViewHolder();
            holder.fileName = (TextView) view.findViewById(R.id.fileName);
            holder.fileImage = (ImageView) view.findViewById(R.id.fileImage);
            view.setTag(holder);
        }

        final AudioFile file = getItem(i);
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.fileName.setText(file.title);

        if (file.imagePath != null) {
            Bitmap bmImg = BitmapFactory.decodeFile(file.imagePath);
            holder.fileImage.setImageBitmap(bmImg);
        } else {
            holder.fileImage.setImageBitmap(null);
            holder.fileImage.setImageResource(R.drawable.folder_image_placeholder);
        }
        return view;

    }
}
