package com.aclass.edx.helloworld.models;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ertd on 2/18/2017.
 */

public class Video extends SugarRecord {

    private String title;
    private String filename;

    public Video() {
    }

    public Video(String title, String filename) {
        this.title = title;
        this.filename = filename;
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

    public static List<String> getAllTitles() {
        Log.i("VIDEO", Video.count(Video.class) + " rows found in Video table. is sugar entity? " + Video.isSugarEntity(Video.class));
        Video first = Video.first(Video.class);
        List<Video> videos = new ArrayList<Video>();
        videos.add(first);
        List<String> titles = new ArrayList<String>();

        for (Video video : videos) {
            titles.add(video.getTitle());
        }

        return titles;
    }

    public static void prepopulate() {
        String[] titles = new String[]{"Courtesy", "Warmth", "Initiative", "Teamwork", "Knowledge"};
        String[] filenames = new String[]{"video1.mp4", "video2.mp4", "video3.mp4", "video4.mp4", "video5.mp4"};

        for (int  i = 0 ; i < titles.length && i < filenames.length ; i++) {
            Video video = new Video();
            video.setTitle(titles[i]);
            video.setFilename(filenames[i]);
            video.save();
        }
    }
}
