package com.kazinov.audiomanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import static com.kazinov.audiomanager.FileListActivity.FolderIdBundleTag;

public class FolderListActivity extends AppCompatActivity {
    final int REQUEST_CODE = 123;
    final static String LOGCAT_TAG = "audiomanager";
    final String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

    private TextView mResult;
    private ListView mFoldersListView;
    private FolderListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_list);
        mFoldersListView = (ListView) findViewById(R.id.folders_list);
        mFoldersListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FolderListAdapter adapter = (FolderListAdapter) adapterView.getAdapter();
                AudioFolder folder = adapter.getItem(i);
                goToFilesList(folder.id);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new FolderListAdapter(this, null);
        mFoldersListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOGCAT_TAG, "onResume() called");
        getAudioFiles();
    }

    private void getAudioFiles() {
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE_PERMISSION}, REQUEST_CODE);
            return;
        }

        MediaParser mediaParser = new MediaParser(getContentResolver());
        Map<Long, AudioFolder> foldersMap = mediaParser.getFolders();
        mAdapter.update(new ArrayList<AudioFolder>(foldersMap.values()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAudioFiles();
            }
        }
    }

    public void goToFilesList(Long folderId) {
        Intent intent = new Intent(this, com.kazinov.audiomanager.FileListActivity.class);
        Bundle b = new Bundle();
        b.putLong(FolderIdBundleTag, folderId);
        intent.putExtras(b);
        finish();
        startActivity(intent);
    }

}
