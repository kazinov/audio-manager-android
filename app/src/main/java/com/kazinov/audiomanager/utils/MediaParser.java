package com.kazinov.audiomanager.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.kazinov.audiomanager.AudioFile;
import com.kazinov.audiomanager.AudioAlbum;

import java.util.HashMap;
import java.util.Map;

public class MediaParser {
    ContentResolver contentResolver;

    public MediaParser(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public Map<Long, AudioAlbum> getAlbums() {
        Map<Long, AudioAlbum> albumsMap = new HashMap();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst() != false) {

            Integer idColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            Integer titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            Integer albumIdColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            Integer albumTitleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

            do {
                Long audioId = cursor.getLong(idColumnIndex);
                String audioTitle = cursor.getString(titleColumnIndex);
                Long albumId = cursor.getLong(albumIdColumnIndex);

                AudioAlbum album = albumsMap.get(albumId);
                if (album == null) {
                    String albumTitle = cursor.getString(albumTitleColumnIndex);
                    album = new AudioAlbum(albumId, albumTitle);

                    Cursor albumArtCursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                            MediaStore.Audio.Albums._ID+ "=?",
                            new String[] {String.valueOf(albumId)},
                            null);

                    if (albumArtCursor.moveToFirst()) {
                        String albumArtPath = albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                        album.albumArtPath = albumArtPath;
                        if (album.albumArtPath != null) {
                            Log.d("album-art", album.albumArtPath);
                        }
                    }


                    albumsMap.put(album.id, album);
                }

            } while (cursor.moveToNext());
        }

        return albumsMap;
    }

    public Map<Long, AudioFile> getFiles(Long folderId) {
        Map<Long, AudioFile> filesMap = new HashMap();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String albumArtPath = "";
        String albumTitle = "";
        Cursor albumCursor = contentResolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ALBUM_ART,
                        MediaStore.Audio.Albums.ALBUM
                },
                MediaStore.Audio.Albums._ID+ "=?",
                new String[] {String.valueOf(folderId)},
                null);

        if (albumCursor.moveToFirst()) {
            albumArtPath = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            albumTitle = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
        }

        Cursor cursor = contentResolver.query(
                uri,
                null,
                MediaStore.Audio.Media.ALBUM_ID+ "=?",
                new String[] {String.valueOf(folderId)},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            Integer idColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            Integer titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            do {
                Long audioId = cursor.getLong(idColumnIndex);
                String audioTitle = cursor.getString(titleColumnIndex);
                AudioFile audioFile = new AudioFile(audioId, audioTitle);
                audioFile.albumTitle = albumTitle;
                audioFile.imagePath = albumArtPath;

                filesMap.put(audioFile.id, audioFile);

            } while (cursor.moveToNext());
        }

        return filesMap;
    }
}
