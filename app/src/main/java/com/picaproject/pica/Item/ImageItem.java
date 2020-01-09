package com.picaproject.pica.Item;

public class ImageItem {
    private int image_id;
    private int id;

    public ImageItem(int image_id) {
        this.image_id = image_id;
    }

    public ImageItem(int image_id, int id) {
        this.image_id = image_id;
        this.id = id;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
