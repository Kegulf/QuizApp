package com.example.android.quizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.controller.QuizController;
import com.example.android.questionLib.QuestionLibrary;


public class QuizActivity extends AppCompatActivity {


    private TextView questionHeader, questionContainer, scoreContainer, timerContainer;
    private Button answerButton;
    private ProgressBar timerProgress;
    private LinearLayout answerContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // initialize TextViews fields
        questionHeader = (TextView) findViewById(R.id.question_header);
        questionContainer = (TextView) findViewById(R.id.question_container);
        scoreContainer = (TextView) findViewById(R.id.score_container);


        answerContainer = (LinearLayout) findViewById(R.id.answer_container);
        // Initialize Buttons and add OnClickListeners
        answerButton = (Button) findViewById(R.id.answer_button);

        // Initialize timer fields.
        timerContainer = (TextView) findViewById(R.id.time_left_TextView);
        timerProgress = (ProgressBar) findViewById(R.id.time_left_ProgressBar);

        // Set this QuizActivity's QuizLibrary to the chosen quiz category
        int quizTheme = getIntent().getExtras().getInt("quizTheme");
        new QuizController(this, new QuestionLibrary(quizTheme));
    }

    public void setQuestionHeaderText(String text) {
        questionHeader.setText(text);
    }
    public void setQuestionContainerText(String text) {
        questionContainer.setText(text);
    }
    public void setQuestionContainerOnClickListener(View.OnClickListener listener) {
        questionContainer.setOnClickListener(listener);
    }
    public void setScoreContainerText(String text) {
        scoreContainer.setText(text);
    }
    public void setTimeContainerText(String text) {
        timerContainer.setText(text);
    }
    public void setTimerProgress (int progress) {
        timerProgress.setProgress(progress);
    }
    public void clearAnswerContainer() {
        answerContainer.removeAllViews();
    }
    public void addAnswers(View v) {
        answerContainer.addView(v);
    }
    public void addAnswerButtonListener(View.OnClickListener listener) {
        answerButton.setOnClickListener(listener);
    }
}
