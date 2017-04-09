package com.example.android.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ResultActivity extends AppCompatActivity {

    private TextView resultView, scoreView;
    private Button retryButton, menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Save the extras supplied by the QuizActivity
        Bundle bundle = getIntent().getExtras();
        int correctAnswers = bundle.getInt("correct");
        int score = bundle.getInt("score");
        int numOfQuestions = bundle.getInt("numOfQuestions");

        // Initialize the Buttons and TextViews
        resultView = (TextView) findViewById(R.id.final_result_TextView);
        scoreView = (TextView) findViewById(R.id.final_score_TextView);
        retryButton = (Button) findViewById(R.id.retry_button);
        menuButton = (Button) findViewById(R.id.menu_button_resultscreen);

        // Set the text of result and score TextViews based on the Extra values from QuizActivity
        resultView.setText(String.format("%d out of %d", correctAnswers, numOfQuestions));
        scoreView.setText(String.format("%d", score));


        // Add OnClickListeners to the retry and menu button.
        // I assume the anon method syntax was added by Android Studio
        // when I used CTRL + ALT + L to format the code.
        retryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ResultActivity.this.finish();
                startActivity(new Intent(ResultActivity.this, QuizActivity.class));
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultActivity.this.finish();
                startActivity(new Intent(ResultActivity.this, MenuActivity.class));
            }
        });


    }
}
