package com.aclass.edx.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aclass.edx.helloworld.utils.PrefUtils;

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
        PrefUtils.saveBoolean(this, R.string.prefs_skip_nickname, true);
        goToDashboardActivity();
    }

    private void saveNameAndProceed() {
        String nickname = nicknameField.getText().toString();

        if (!TextUtils.isEmpty(nickname)) {
            PrefUtils.saveString(this, R.string.prefs_nickname, nickname);
            //prompt that it was saved then go to Activity
            goToDashboardActivity();
        }
    }

    private void goToDashboardActivity() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
