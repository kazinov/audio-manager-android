package com.kazinov.audiomanager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_CODE = 123;
    final String LOGCAT_TAG = "audiomanager";
    final String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

    private TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResult = findViewById(R.id.logContainer);

        // TODO: needed?
        // mResult.setMovementMethod(new ScrollingMovementMethod());
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

        AudioGetter audioGetter = new AudioGetter(getContentResolver());
        List<AudioFile> list = audioGetter.getFiles();
        for (AudioFile audioFile : list) {
            mResult.append("\n" + audioFile.title);
        }

//        ContentResolver contentResolver = getContentResolver();
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Cursor cursor = contentResolver.query(uri, null, null, null, null);
//
//        mResult.setText("");
//        if (cursor == null) {
//            // query failed, handle error.
//        } else if (!cursor.moveToFirst()) {
//            // no media on the device
//        } else {
//            int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
//            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
////            int title = cursor.getColumnIndex(MediaStore.);
//
//            do {
////                // Get the current audio file id
////                long thisId = cursor.getLong(id);
////                mResult.append("\n\n" + thisId);
//
//                // Get the current audio title
//                String thisTitle = cursor.getString(title);
//                mResult.append("\n" + thisTitle);
//                // Process current music here
//            } while (cursor.moveToNext());
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(LOGCAT_TAG, "onRequestPermissionsResult(): Permission granted!");
                getAudioFiles();
            } else {
                Log.d(LOGCAT_TAG, "Permission denied =( ");
            }
        }
    }

}
