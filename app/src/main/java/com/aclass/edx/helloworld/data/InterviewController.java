package com.aclass.edx.helloworld.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.InterviewQuestion;
import com.aclass.edx.helloworld.data.models.Media;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tictocproject on 01/04/2017.
 */

public class InterviewController {

    private Interview interview;
    private List<InterviewQuestion> questions;
    private int currentQuestionNo, numQuestions;
    private InterviewQuestion currentQuestion;

    public InterviewController(ContentResolver resolver, Interview interview) {
        this.interview = interview;
        this.questions = initQuestions(resolver);
        this.numQuestions = questions.size();

        // Init media per question
        for (InterviewQuestion question : questions) {
            Uri questionUri = Uri.parse(AppContract.MediaEntry.CONTENT_URI + "/" + question.getMediaId());
            Cursor cursor = resolver.query(questionUri, AppContract.MediaEntry.ALL_COLUMN_NAMES, null, null, null);
            if (cursor.moveToNext()) {
                Media media = new Media();
                media.setValues(cursor);
                question.setMedia(media);
            }
        }

        this.currentQuestionNo = -1;
        this.currentQuestion = null;
    }

    private List<InterviewQuestion> initQuestions(ContentResolver resolver) {
        List<InterviewQuestion> questions = new ArrayList<InterviewQuestion>();

        Cursor cursor = resolver.query(AppContract.InterviewQuestionEntry.CONTENT_URI,
                AppContract.InterviewQuestionEntry.ALL_COLUMN_NAMES,
                AppContract.InterviewQuestionEntry.COLUMN_NAME_INTERVIEW_ID + " = ?",
                new String[]{interview.getId() + ""},
                AppContract.InterviewQuestionEntry.COLUMN_NAME_SEQ_NUM);

        while (cursor.moveToNext()) {
            InterviewQuestion question = new InterviewQuestion();
            question.setValues(cursor);
            questions.add(question);
        }


        return questions;
    }

    public InterviewQuestion nextQuestion() {
        if (currentQuestionNo < numQuestions) {
            currentQuestionNo++;
            currentQuestion = questions.get(currentQuestionNo);
        }
        return currentQuestion;
    }

    public int getCurrentQuestionNo() {
        return currentQuestionNo;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public boolean hasNext() {
        return currentQuestionNo + 1 < numQuestions;
    }
}
