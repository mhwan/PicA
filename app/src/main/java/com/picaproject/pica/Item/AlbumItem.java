package com.picaproject.pica.Item;

public class AlbumItem {
    private String album_name;
    private String album_author;
    private int image_id;

    public AlbumItem(String album_name, String album_author, int image_id) {
        this.album_name = album_name;
        this.album_author = album_author;
        this.image_id = image_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public String getAlbum_author() {
        return album_author;
    }

    public int getImage_id() {
        return image_id;
    }
}
