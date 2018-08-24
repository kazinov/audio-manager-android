package com.kazinov.audiomanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static com.kazinov.audiomanager.FolderListActivity.LOGCAT_TAG;

public class FileListActivity extends AppCompatActivity {
    Long mFolderId;
    final static String FolderIdBundleTag ="folderId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_list);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mFolderId = bundle.getLong(FolderIdBundleTag);
        }
    }
}
