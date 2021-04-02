package com.czdxwx.test.model;

public class Music {
    private String music_name;
    private String music_path;

    public Music(String music_name, String music_path) {
        this.music_name = music_name;
        this.music_path = music_path;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public String getMusic_path() {
        return music_path;
    }

    public void setMusic_path(String music_path) {
        this.music_path = music_path;
    }
}
