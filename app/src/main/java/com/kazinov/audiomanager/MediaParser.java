package com.kazinov.audiomanager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaParser {
    ContentResolver contentResolver;

    public MediaParser(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public Map<Long, AudioFolder> getFolders() {
        Map<Long, AudioFolder> foldersMap = new HashMap();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor == null) {
        } else if (!cursor.moveToFirst()) {
        } else {
            Integer idColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            Integer titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            Integer albumIdColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            Integer albumTitleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            do {
                Long audioId = cursor.getLong(idColumnIndex);
                String audioTitle = cursor.getString(titleColumnIndex);
                AudioFile audioFile = new AudioFile(audioId, audioTitle);

                Long albumId = cursor.getLong(albumIdColumnIndex);
                String albumTitle = cursor.getString(albumTitleColumnIndex);
                AudioFolder folder = foldersMap.get(albumId);
                if (folder != null) {
                    folder.filesMap.put(audioFile.id, audioFile);
                } else {
                    folder = new AudioFolder(albumId, albumTitle);
                    folder.filesMap.put(audioFile.id, audioFile);
                    foldersMap.put(folder.id, folder);
                    Log.d("audiomanager", folder.id + "-" + folder.title);
                }

            } while (cursor.moveToNext());
        }

        return foldersMap;
    }
}
