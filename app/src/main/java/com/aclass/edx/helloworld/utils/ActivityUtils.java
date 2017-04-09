package com.aclass.edx.helloworld.utils;

import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.activities.AudioPlayerActivity;
import com.aclass.edx.helloworld.activities.InterviewActivity;
import com.aclass.edx.helloworld.activities.VideoPlayerActivity;
import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Content;
import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.models.Module;
import com.aclass.edx.helloworld.data.models.Topic;

import static com.aclass.edx.helloworld.data.contracts.AppContract.ContentEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.ModuleEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.TopicEntry;

/**
 * Created by tictocproject on 01/04/2017.
 */

public class ActivityUtils {

    /**
     * Generates a random string from a long value
     *
     * @return string containing a random long value
     */
    public static String generateRandomString() {
        Random random = new Random();
        return String.valueOf(random.nextLong());
    }

    /**
     * Returns the next Content
     *
     * @param context Activity or Fragment
     * @return the next Content
     */
    public static Content getNextContent(Context context, Content currentContent) {
        Content nextContent = new Content();
        int nextSeqNum = currentContent.getSeqNum() + 1;

        // Check if there's a succeeding content in the same topic
        Cursor cursor = context.getContentResolver().query(
                ContentEntry.CONTENT_URI,
                ContentEntry.ALL_COLUMN_NAMES,
                ContentEntry.COLUMN_NAME_TOPIC_ID + " = ? AND " +
                        ContentEntry.COLUMN_NAME_SEQ_NUM + " = ?",
                new String[]{
                        currentContent.getTopicId() + "",
                        nextSeqNum + ""
                },
                null
        );

        if (cursor.moveToNext()) {
            nextContent.setValues(cursor);
        }

        return nextContent;
    }

    /**
     * Returns the parent Topic of the given Content
     *
     * @param content
     * @return parent Topic of content
     */
    public static Topic getParentTopic(Context context, Content content) {
        Topic parent = new Topic();

        Cursor cursor = context.getContentResolver().query(
                TopicEntry.CONTENT_URI,
                TopicEntry.ALL_COLUMN_NAMES,
                TopicEntry._ID + " = ?",
                new String[]{content.getTopicId() + ""},
                null
        );

        if (cursor.moveToNext()) {
            parent.setValues(cursor);
        }

        return parent;
    }

    /**
     * Returns the parent Module of the given Topic
     *
     * @param context
     * @param topic
     * @return parent Module of topic
     */
    public static Module getParentModule(Context context, Topic topic) {
        Module parent = new Module();

        Cursor cursor = context.getContentResolver().query(
                ModuleEntry.CONTENT_URI,
                ModuleEntry.ALL_COLUMN_NAMES,
                ModuleEntry._ID + " = ?",
                new String[]{topic.getModuleId() + ""},
                null
        );

        if (cursor.moveToNext()) {
            parent.setValues(cursor);
        }

        return parent;
    }

    // TODO should we put the ff methods in a non-UI fragment?
    // Used by ContentListActivity and NextButtonFragment to figure out
    // which activity to go to based on content type
    public static void goToActivityBasedOnContentType(Context context, Content content) {
        switch (content.getType()) {
            case ContentEntry.TYPE_LESSON_MEDIA:
                ActivityUtils.goToMediaActivity(context, content);
                break;
            case ContentEntry.TYPE_LESSON_PRACTICE_INTERIEW:
                ActivityUtils.goToInterviewActivity(context, content);
                break;
            default:
                Toast.makeText(context, "Content type " + content.getType() +
                        " not supported", Toast.LENGTH_SHORT);
        }

    }

    public static void goToInterviewActivity(Context context, Content content) {
        Cursor cursor = context.getContentResolver().query(
                Uri.parse(AppContract.InterviewEntry.CONTENT_URI + "/" + content.getContentId()),
                AppContract.InterviewEntry.ALL_COLUMN_NAMES,
                null,
                null,
                null
        );

        if (cursor.moveToNext()) {
            Interview interview = new Interview();
            interview.setValues(cursor);

            Intent intent = new Intent(context, InterviewActivity.class);
            intent.putExtra(context.getString(R.string.content_list_selected_video_key), interview);
            Toast.makeText(context, String.format("Go to INTERVIEW with id %d and title %s", interview.getId(), interview.getTitle()), Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
        }
    }


    public static void goToMediaActivity(Context context, Content content) {
        Cursor mediaCursor = context.getContentResolver().query(
                Uri.parse(AppContract.MediaEntry.CONTENT_URI + "/" + content.getContentId()),
                AppContract.MediaEntry.ALL_COLUMN_NAMES,
                null,
                null,
                null
        );

        if (mediaCursor.moveToNext()) {
            Media media = new Media();
            media.setValues(mediaCursor);

            goToMediaActivity(context, content, media);
        } else {
            throw new RuntimeException(context.getString(R.string.all_error_no_media_found_by_id));
        }
    }

    public static void goToMediaActivity(Context context, Content content, Media media) {
        Intent intent;

        switch (media.getType()) {
            case AppContract.MediaEntry.TYPE_VIDEO:
                intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra(context.getString(R.string.content_list_selected_video_key), media);
                break;
            case AppContract.MediaEntry.TYPE_AUDIO:
                intent = new Intent(context, AudioPlayerActivity.class);
                intent.putExtra(context.getString(R.string.content_list_selected_audio_key), media);
                break;
            default:
                throw new RuntimeException("Invalid media type " + media.getType());
        }

        intent.putExtra(context.getString(R.string.content_list_selected_content_key), content);
        context.startActivity(intent);
    }

}
