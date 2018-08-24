package com.kazinov.audiomanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudioAlbum {
    public long id;
    public String title;
    public String albumArtPath;

    public AudioAlbum(
            long id,
            String title
    ) {
        this.id = id;
        this.title = title;
    }
}
