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

        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ALBUM_ART,
                        MediaStore.Audio.Albums.ALBUM},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {

            Integer idColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            Integer titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            Integer albumArtColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

            do {
                Long albumId = cursor.getLong(idColumnIndex);
                String albumTitle = cursor.getString(titleColumnIndex);
                String albumArt = cursor.getString(albumArtColumnIndex);

                AudioAlbum album = new AudioAlbum(albumId, albumTitle);
                album.albumArtPath = albumArt;
                albumsMap.put(album.id, album);
            } while (cursor.moveToNext());
        }

        return albumsMap;
    }

    public Map<Long, AudioFile> getFiles(Long albumId) {
        Map<Long, AudioFile> filesMap = new HashMap();

        AudioAlbum album = getAlbum(albumId);

        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ALBUM_ID
                },
                MediaStore.Audio.Media.ALBUM_ID + "=?",
                new String[]{String.valueOf(albumId)},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            Integer idColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            Integer titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            do {
                Long audioId = cursor.getLong(idColumnIndex);
                String audioTitle = cursor.getString(titleColumnIndex);
                AudioFile audioFile = new AudioFile(audioId, audioTitle);
                audioFile.albumTitle = album.title;
                audioFile.imagePath = album.albumArtPath;

                filesMap.put(audioFile.id, audioFile);

            } while (cursor.moveToNext());
        }

        return filesMap;
    }

    public AudioAlbum getAlbum(Long albumId) {
        String albumArtPath = "";
        String albumTitle = "";
        Cursor albumCursor = contentResolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ALBUM_ART,
                        MediaStore.Audio.Albums.ALBUM
                },
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{String.valueOf(albumId)},
                null);

        if (albumCursor != null && albumCursor.moveToFirst()) {
            albumArtPath = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            albumTitle = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
        }

        AudioAlbum album = new AudioAlbum(albumId, albumTitle);
        album.albumArtPath = albumArtPath;
        return album;
    }
}
