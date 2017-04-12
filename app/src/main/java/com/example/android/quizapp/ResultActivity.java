package com.example.android.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView resultView, scoreView, resultHeadline;
    private Button retryButton, menuButton;

    private int correctAnswers, score, numOfQuestions;

    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Save the extras supplied by the QuizActivity
        Bundle bundle = getIntent().getExtras();
        correctAnswers = bundle.getInt("correct");
        score = bundle.getInt("score");
        numOfQuestions = bundle.getInt("numOfQuestions");
        playerName = bundle.getString("playerName");

        // Initialize the Buttons and TextViews
        resultView = (TextView) findViewById(R.id.final_result_TextView);
        scoreView = (TextView) findViewById(R.id.final_score_TextView);
        resultHeadline = (TextView) findViewById(R.id.result_headline_TextView);
        retryButton = (Button) findViewById(R.id.retry_button);
        menuButton = (Button) findViewById(R.id.menu_button_resultscreen);

        // Set the text of result and score TextViews based on the Extra values from QuizActivity
        resultView.setText(String.format("%d out of %d", correctAnswers, numOfQuestions));
        scoreView.setText(String.format("%d", score));


        // Add OnClickListeners to the retry and menu button.
        retryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ResultActivity.this.finish();
                Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
                intent.putExtras(getIntent().getExtras()); // This one I liked x)
                startActivity(intent);
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultActivity.this.finish();
                startActivity(new Intent(ResultActivity.this, MenuActivity.class));
            }
        });

        setHeadline();

    }

    private void setHeadline() {

        String headline = "";

        int correctAnswersPercentage = correctAnswers / numOfQuestions * 100;

        if( correctAnswersPercentage == 100)
            headline += getString(R.string.congratulations);
        else if (correctAnswersPercentage < 75)
            headline += getString(R.string.well_done);
        else if(correctAnswersPercentage < 50)
            headline += getString(R.string.you_need_practice);

        headline += " " + playerName;


        resultHeadline.setText(headline);
    }
}
