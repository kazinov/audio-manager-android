package com.kazinov.audiomanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

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
        mResult.setMovementMethod(new ScrollingMovementMethod());
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
        mResult.setText("");
        for (Map.Entry<Long, AudioFolder> entry : foldersMap.entrySet()) {
            AudioFolder folder = entry.getValue();
            mResult.append("\n" + folder.title);
        }
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
