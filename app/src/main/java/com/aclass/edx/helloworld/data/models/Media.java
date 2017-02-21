package com.aclass.edx.helloworld.data.models;

/**
 * Created by ertd on 2/21/2017.
 */

public class Media {
    private long id;
    private String title, filename;
    private int type;

    public Media() {}

    public Media(String title, String filename, int type) {
        this.title = title;
        this.filename = filename;
        this.type = type;
    }

    public Media(long id, String title, String filename, int type) {
        this.id = id;
        this.title = title;
        this.filename = filename;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isEmpty() {
        return title == null && filename == null;
    }
}

