package com.melons.melon.guitarguide;

public class Song {
    String songname;
    String songartist;
    int songAddress;

    public Song(String songname,String songartist, int songAddress) {
        this.songname = songname;
        this.songartist=songartist;
        this.songAddress = songAddress;
    }

    public String getSongname() {
        return songname;
    }

    public String getSongartist() {
        return songartist;
    }

    public int getSongAddress() {
        return songAddress;
    }
}
