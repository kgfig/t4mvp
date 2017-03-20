package com.aclass.edx.helloworld;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aclass.edx.helloworld.utils.AppUtils;

public class GetNameActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText nicknameField;
    private Button submit, skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_name);

        nicknameField = (EditText) findViewById(R.id.enter_nickname_field);
        submit = (Button) findViewById(R.id.submit_nickname);
        skip = (Button) findViewById(R.id.skip_nickname);

        submit.setOnClickListener(this);
        skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch(viewId) {
            case R.id.skip_nickname:
                saveChoiceToSkipAndProceed();
                break;
            case R.id.submit_nickname:
                saveNameAndProceed();
                break;
        }
    }

    private void saveChoiceToSkipAndProceed() {
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.all_prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(getString(R.string.prefs_skip_nickname), true);
        editor.commit();

        goToDashboardActivity();
    }

    private void saveNameAndProceed() {
        String nickname = nicknameField.getText().toString();

        if (!TextUtils.isEmpty(nickname)) {
            SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.all_prefs_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(getString(R.string.prefs_nickname), nickname);
            editor.commit();
            //prompt that it was saved then go to Activity

            goToDashboardActivity();
        }
    }

    private void goToDashboardActivity() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
