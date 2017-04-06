package com.aclass.edx.helloworld.controllers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.InterviewQuestion;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.utils.PrefUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tictocproject on 01/04/2017.
 */

public class InterviewController {

    private Interview interview;
    private List<InterviewQuestion> questions;
    private int currentQuestionNo, numQuestions;
    private InterviewQuestion currentQuestion;
    private HashMap<Long, String> answerFilenames;
    private HashMap<Long, Boolean> answersSaved;

    public InterviewController(Context context, Interview interview) {
        this.interview = interview;
        this.questions = initQuestions(context.getContentResolver());
        this.numQuestions = questions.size();
        this.answerFilenames = new HashMap<Long, String>();
        this.answersSaved = new HashMap<Long, Boolean>();

        // Init media per question and init answersSaved map values to false
        for (InterviewQuestion question : questions) {
            Uri mediaUri = Uri.parse(AppContract.MediaEntry.CONTENT_URI + "/" + question.getMediaId());
            Log.d("InterviewController", "getMediaUri:" + mediaUri.toString());
            Cursor cursor = context.getContentResolver().query(mediaUri, AppContract.MediaEntry.ALL_COLUMN_NAMES, null, null, null);
            if (cursor.moveToNext()) {
                Media media = new Media();
                media.setValues(cursor);
                question.setMedia(media);
                Log.d("InterviewController", "Set media to question " + question.getText());
            }
            answersSaved.put(question.getId(), false);
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

    public boolean hasAnswerForCurrentQuestion() {
        return answerFilenames.containsKey(currentQuestion.getId()) && answersSaved.containsKey(currentQuestion);
    }

    public void saveAnswerForCurrentQuestion(String filename) {
        answerFilenames.put(currentQuestion.getId(), filename);
    }

    public void markCurrentQuestionAnswered() {
        answersSaved.put(currentQuestion.getId(), true);
    }

    public String getAnswerFilename() {
        return answerFilenames.containsKey(currentQuestion.getId()) ? answerFilenames.get(currentQuestion.getId()) : "";
    }

    public long getInterviewId() {
        return interview.getId();
    }

    public String getTitle() {
        return interview.getTitle();
    }

    public long getCurrentQuestionId() {
        return currentQuestion.getId();
    }

    public Collection<String> getAnswerFilenames() {
        return answerFilenames.values();
    }

    public void resetQuestionTracker() {
        currentQuestionNo = -1;
        currentQuestion = null;
    }
}
