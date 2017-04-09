package com.example.android.quizapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.questionLib.QuestionLibrary;



public class QuizActivity extends AppCompatActivity {

    private int timeLeftPercentage = 100, questionNum = 0, score = 0, correctAnswers = 0;
    private QuestionLibrary quizLib;
    private TextView questionHeader, questionContainer, scoreContainer, timerContainer;
    private Button btnAnswer1, btnAnswer2, btnAnswer3;
    private ProgressBar timeProgress;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // initialize TextViews fields
        questionHeader = (TextView) findViewById(R.id.question_header);
        questionContainer = (TextView) findViewById(R.id.question_container);
        scoreContainer = (TextView) findViewById(R.id.score_container);

        // Initialize Buttons and add OnClickListeners
        // AnswerButtonListener is an internal class of QuizActivity
        btnAnswer1 = (Button) findViewById(R.id.answer1_button);
        btnAnswer1.setOnClickListener(new AnswerButtonListener());
        btnAnswer2 = (Button) findViewById(R.id.answer2_button);
        btnAnswer2.setOnClickListener(new AnswerButtonListener());
        btnAnswer3 = (Button) findViewById(R.id.answer3_button);
        btnAnswer3.setOnClickListener(new AnswerButtonListener());

        // Initialize timer fields.
        timerContainer = (TextView) findViewById(R.id.time_left_TextView);
        timeProgress = (ProgressBar) findViewById(R.id.time_left_ProgressBar);

        // Initialize and configure the countDownTimer
        timer = new CountDownTimer(5000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                // I'm doing all time stuff based on the remaining time.
                // I get the percentage to change the ProgressBar which has 100 steps
                // and I set the text based on the remaining millis to make it look awesome
                timeLeftPercentage = (int) (millisUntilFinished / 50); //   millis / 10000 * 100
                timeProgress.setProgress(timeLeftPercentage);
                timerContainer.setText(String.format("%.2f s", (float) millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                // Display a Toast to signal the player that the time for this question ran out
                Toast.makeText(QuizActivity.this, "You ran out of time..", Toast.LENGTH_SHORT).show();
                // As lon as we have not done all the questions in the current quizLib
                // call nextQuestion method
                if(questionNum < quizLib.getQuizLength() - 1) {
                    nextQuestion(++questionNum);
                } else {  // if done, call openResultActivity method
                    openResultActivity();
                }
            }
        };

        // Set this QuizActivity's QuizLibrary to the BASIC_MATH category
        this.quizLib = new QuestionLibrary(QuestionLibrary.BASIC_MATH);

        // Start quiz by requesting the nextQuestion(0)
        nextQuestion(questionNum);

    }

    /**
     * Updates the Question header, question text and buttonText. <br>
     * It also starts the timer
     * @param i the question number
     */
    private void nextQuestion(int i) {
        Resources res = getResources();

        timeLeftPercentage = 100;
        questionHeader.setText(String.format("%s %d", res.getString(R.string.question_header), (1 + i)));
        questionContainer.setText(quizLib.getQUESTIONS()[i]);
        btnAnswer1.setText(quizLib.getANSWERS()[i][0]);
        btnAnswer2.setText(quizLib.getANSWERS()[i][1]);
        btnAnswer3.setText(quizLib.getANSWERS()[i][2]);
        timer.start();
    }

    /**
     * Stops the timer and checks if the answer is correct.
     * If correct it will update the score. <br>
     * Will so run next question or open the result activity
     * based on the questionNumber. <br>
     * @param answer text from the button that was pressed.
     */
    private void checkAnswer(String answer) {
        timer.cancel();

        if (answer.equals(quizLib.getCORRECT_ANSWER()[questionNum])) {
            ++correctAnswers;
            score += (5 * timeLeftPercentage);
            scoreContainer.setText(String.format("%d", score));
        }

        if (questionNum < quizLib.getQuizLength() - 1)
            nextQuestion(++questionNum);
        else
            openResultActivity();
    }

    /**
     * Kills this QuizActivity and opens the ResultActivity. <br>
     * Sends a bundle of results with the Intent to open the ResultActivity
     */
    private void openResultActivity() {
        Intent i = new Intent(QuizActivity.this, ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        bundle.putInt("correct", correctAnswers);
        bundle.putInt("numOfQuestions", quizLib.getQuizLength());
        i.putExtras(bundle);
        QuizActivity.this.finish();
        startActivity(i);
    }

    /**
     * OnClickListener for the answer buttons.
     * Checks the answer that the button text represent.
     */
    private class AnswerButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            checkAnswer((String) ((Button) v).getText());
        }
    }
}
