package com.aclass.edx.helloworld.models;

import com.orm.SugarRecord;

/**
 * Created by ertd on 2/18/2017.
 */

public class InterviewResponse extends SugarRecord {
    private InterviewItem interviewItem;
    private User user;
    private String content;

    public InterviewResponse() {}



    public InterviewItem getInterviewItem() {
        return interviewItem;
    }

    public void setInterviewItem(InterviewItem interviewItem) {
        this.interviewItem = interviewItem;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}