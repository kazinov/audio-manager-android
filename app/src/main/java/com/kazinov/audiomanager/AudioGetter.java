package com.kazinov.audiomanager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AudioGetter {
    ContentResolver contentResolver;

    public AudioGetter(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public List<AudioFile> getFiles() {
        List<AudioFile> list = new ArrayList<AudioFile>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor == null) {
        } else if (!cursor.moveToFirst()) {
        } else {
            int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            do {
                list.add(new AudioFile(
                        cursor.getLong(id),
                        cursor.getString(title)
                ));
            } while (cursor.moveToNext());
        }

        return list;
    }
}
