package com.kazinov.audiomanager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.HashMap;
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

                AudioFolder folder = foldersMap.get(albumId);
                if (folder != null) {
                    folder.filesMap.put(audioFile.id, audioFile);
                } else {
                    String albumTitle = cursor.getString(albumTitleColumnIndex);
                    folder = new AudioFolder(albumId, albumTitle);

                    Cursor albumArtCursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                            MediaStore.Audio.Albums._ID+ "=?",
                            new String[] {String.valueOf(albumId)},
                            null);

                    if (albumArtCursor.moveToFirst()) {
                        String albumArtPath = albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                        folder.albumArtPath = albumArtPath;
                        if (folder.albumArtPath != null) {
                            Log.d("album-art", folder.albumArtPath);
                        }
                    }

                    folder.filesMap.put(audioFile.id, audioFile);
                    foldersMap.put(folder.id, folder);
                }

            } while (cursor.moveToNext());
        }

        return foldersMap;
    }
}
