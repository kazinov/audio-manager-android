package com.kazinov.audiomanager;

public class AudioFile {
    public long id;
    public String title;
    public long albumId;
    public String albumTitle;
    public String imagePath;

    public AudioFile(
            long id,
            String title
    ) {
        this.id = id;
        this.title = title;
    }
}
