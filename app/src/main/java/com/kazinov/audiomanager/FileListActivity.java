package com.kazinov.audiomanager;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.kazinov.audiomanager.utils.MediaParser;

import java.util.ArrayList;
import java.util.Map;

import static com.kazinov.audiomanager.FolderListActivity.LOGCAT_TAG;
import static com.kazinov.audiomanager.FolderListActivity.READ_EXTERNAL_STORAGE_PERMISSION;
import static com.kazinov.audiomanager.FolderListActivity.REQUEST_READ_INTERNAL_STORAGE_PERMISSIONS_ID;

public class FileListActivity extends AppCompatActivity {
    Long mFolderId;
    final static String FolderIdBundleTag ="folderId";

    private ListView mFoldersListView;
    private FileListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_list);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mFolderId = bundle.getLong(FolderIdBundleTag);
        }

        mFoldersListView = (ListView) findViewById(R.id.filesList);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new FileListAdapter(this, null);
        mFoldersListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAudioFiles();
    }

    private void getAudioFiles() {
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE_PERMISSION}, REQUEST_READ_INTERNAL_STORAGE_PERMISSIONS_ID);
            return;
        }

        MediaParser mediaParser = new MediaParser(getContentResolver());
        Map<Long, AudioFile> filesMap = mediaParser.getFiles(mFolderId);
        mAdapter.update(new ArrayList<AudioFile>(filesMap.values()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_INTERNAL_STORAGE_PERMISSIONS_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAudioFiles();
            }
        }
    }
}
