package com.aclass.edx.helloworld.data.models;

/**
 * Created by tictocproject on 20/03/2017.
 */

public class MediaLesson {

    private Media media;
    private Content content;

    public MediaLesson() {

    }

    public MediaLesson(Media media, Content content) {
        this.media = media;
        this.content = content;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
