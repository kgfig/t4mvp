package com.aclass.edx.helloworld.models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by ertd on 2/18/2017.
 */

public class Interview extends SugarRecord {
    String title;

    public Interview() {
    }

    public Interview(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<InterviewItem> getItems() {
        return InterviewItem.find(InterviewItem.class, "interview = ?", String.format("%d", getId()));
    }

}