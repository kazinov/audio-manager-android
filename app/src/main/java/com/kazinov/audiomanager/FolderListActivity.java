package com.kazinov.audiomanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kazinov.audiomanager.utils.MediaParser;

import java.util.ArrayList;
import java.util.Map;

import static com.kazinov.audiomanager.FileListActivity.FolderIdBundleTag;

public class FolderListActivity extends AppCompatActivity {
    final static int REQUEST_READ_INTERNAL_STORAGE_PERMISSIONS_ID = 123;
    final static String LOGCAT_TAG = "audiomanager";
    final static String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

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
                AudioAlbum folder = adapter.getItem(i);
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
        getAlbums();
    }

    private void getAlbums() {
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE_PERMISSION}, REQUEST_READ_INTERNAL_STORAGE_PERMISSIONS_ID);
            return;
        }

        MediaParser mediaParser = new MediaParser(getContentResolver());
        Map<Long, AudioAlbum> foldersMap = mediaParser.getAlbums();
        mAdapter.update(new ArrayList<AudioAlbum>(foldersMap.values()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_INTERNAL_STORAGE_PERMISSIONS_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAlbums();
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
