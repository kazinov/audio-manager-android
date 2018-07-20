package com.kazinov.audiomanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudioFolder {
    long id;
    String title;

    Map<Long, AudioFile> filesMap = new HashMap<Long, AudioFile>();

    public AudioFolder(
            long id,
            String title
    ) {
        this.id = id;
        this.title = title;
    }

    public void add(AudioFile file) {
        this.filesMap.put(file.id, file);
    }
}
