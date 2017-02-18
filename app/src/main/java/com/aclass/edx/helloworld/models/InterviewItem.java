package com.aclass.edx.helloworld.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by ertd on 2/18/2017.
 */

public class InterviewItem extends SugarRecord {

    @Ignore
    public static final String COLUMN_ID = "interview_item_id";

    private Interview interview;
    private String description;
    private String filename;
    private int type;
    private int seqNum;

    public InterviewItem() {
    }

    public InterviewItem(Interview interview, String description, String filename, int type, int seqNum) {
        this.interview = interview;
        this.description = description;
        this.filename = filename;
        this.type = type;
        this.seqNum = seqNum;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

}
