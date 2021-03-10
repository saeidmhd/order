package com.mahak.order.szzt_pos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.mahak.order.BaseActivity;
import com.mahak.order.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ResultActivity extends BaseActivity {

    public static Intent getIntent(Context context, String... resultValues) {
        Intent intent = new Intent(context, ResultActivity.class);
        ArrayList<String> resultValuesList = new ArrayList<>(Arrays.asList(resultValues));
        intent.putStringArrayListExtra("resultValues", resultValuesList);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ArrayList<String> resultReturns = new ArrayList<>();
        Intent intent = getIntent();
        if (intent.hasExtra("resultValues"))
            resultReturns.addAll(intent.getStringArrayListExtra("resultValues"));

        StringBuilder resultDescription = new StringBuilder("\n");
        for (String resultReturn : resultReturns) {
            resultDescription.append(resultReturn).append("\n");
        }

        TextView resultTextView = (TextView) findViewById(R.id.tv_result);
        resultTextView.setText(resultDescription.toString());

        Button returnButton = (Button) findViewById(R.id.btn_return);

        returnButton.setOnClickListener(v -> finish());

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
