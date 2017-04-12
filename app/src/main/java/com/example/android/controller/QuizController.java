package com.example.android.controller;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.questionLib.QuestionLibrary;
import com.example.android.quizapp.QuizActivity;
import com.example.android.quizapp.R;
import com.example.android.quizapp.ResultActivity;

import java.util.ArrayList;

/**
 * A controller for handling most of the QuizActivity's logic. <br>
 */
public class QuizController {

    private QuizActivity activity;
    private QuestionLibrary quizLib;

    private CountDownTimer timer;
    private Resources res;


    private int viewBackground;
    private int questionNum = 0,
                score = 0,
                timeLeftPercentage = 100,
                correctAnswers = 0,
                quizTheme;

    private long timeRemaining = 10000;

    private int activityState;

    public static final int QUESTIONING_STATE = 0;
    private static final int RESULT_STATE = 1;

    /**
     * A controller for handling most of the QuizActivity's logic. <br>
     *
     * @param activity the QuizActivity to be controlled.
     * @param quizTheme int that specifies the Model to populate the activity with.
     */
    public QuizController(QuizActivity activity, int quizTheme) {
        this.activity = activity;
        this.quizLib = new QuestionLibrary(quizTheme);
        this.res = activity.getResources();
        this.quizTheme = quizTheme;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            viewBackground = res.getColor(R.color.viewBackground, null);
        else
            viewBackground = res.getColor(R.color.viewBackground);

        nextQuestion(questionNum);
    }

    public void pauseActivity() {
        timer.cancel();
    }
    public void resumeActivity() {
        timer.cancel();
        timer = createTimer(timeRemaining, 10);
        timer.start();
    }

    private int dipToPix(int dip){
        float scale = res.getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
    /**
     * Checks if the supplied answer is the same as the corresponding <br>
     * correct answer. Will update the score and run method showQuestionResult(boolean answerIsCorrect);
     *
     * @param answer supplied answer from user.
     */
    private void checkAnswer(String answer) {
        timer.cancel();

        boolean answerIsCorrect = false;
        String correctAnswer = quizLib.getCorrectAnswers()[questionNum][0].trim();
        // I couldn't figure out why I kept getting the wrong result when I typed in the correct answer.
        // I tried "answer == correctAnswer", "answer.equals(correctAnswer)",
        // I ended up using this string function instead, it is usually used for sorting
        // and will return -1 if answer(String 1)'s first letter is before correctAnswer(String 2)'s first letter
        // in the alphabet. It returns 1 if oposite, and returns 0 if the words are the same.
        if (answer.compareToIgnoreCase(correctAnswer) == 0) {   // there is only one answer to check in this case
            if (!answer.equals(res.getString(R.string.open_answer_placeholder))) {
                updateScore();
                answerIsCorrect = true;
            }
        }

        showQuestionResult(answerIsCorrect);
    }

    /**
     * Checks if the supplied answers is the same as the corresponding <br>
     * correct answers. Will update the score and run method showQuestionResult(boolean answerIsCorrect);
     *
     * @param suppliedAnswers supplied answers from user.
     */
    private void checkAnswer(ArrayList<String> suppliedAnswers) {
        timer.cancel();

        String[] correctAnswers = quizLib.getCorrectAnswers()[questionNum];
        int correctCounter = 0;

        // Check if there is as many supplied answers as correctAnswers.
        // If not, the answer is not correct.
        if (correctAnswers.length == suppliedAnswers.size()) {
            for (String a : correctAnswers)
                for (String b : suppliedAnswers)
                    if (a.equals(b))
                        correctCounter++;

            if (correctCounter == correctAnswers.length) {
                updateScore();
                showQuestionResult(true);
            }
        } else {
            showQuestionResult(false);
        }
    }


    /**
     * Changes the display text of the question container. <br>
     * Text is changed to a result description. It also adds <br>
     * a NextQuestionOnClickListener to the question container <br>
     * which is removed OnClick. <br>
     *
     * @param answerIsCorrect boolean to determine what to print (correct/incorrect).
     */
    private void showQuestionResult(boolean answerIsCorrect) {

        String result = "";
        activityState = RESULT_STATE;

        if (answerIsCorrect)
            result += res.getString(R.string.correct_answer) + "\n";
        else
            result += res.getString(R.string.incorrect_answer) + "\n";

        if (questionNum < quizLib.getQuizLength() - 1) {
            activity.setAnswerButtonText(res.getString(R.string.next_question));
        } else {
            activity.setAnswerButtonText(res.getString(R.string.show_results));
        }

        String correctAnswer = "";
        String[] correctAnswersList = quizLib.getCorrectAnswers()[questionNum];
        int counter = 0;
        for(String s : correctAnswersList){
            correctAnswer += (correctAnswersList.length == ++counter) ? s + "." : s + ", ";
        }
        result += res.getString(R.string.correct_answer_is) + " " + correctAnswer;
        activity.setQuestionContainerText(result);

        activity.addAnswerButtonListener(new NextQuestionOnClickListener());

    }

    /**
     * Updates the question container and header, as well as <br>
     * calling the method that populates the answerContainer. <br>
     * In the end it resets the Timer
     *
     * @param questionNumber the number representing what question to display.
     */
    private void nextQuestion(int questionNumber) {

        activityState = QUESTIONING_STATE;
        String questionHeadline = res.getString(R.string.question_header) + " " + (1 + questionNumber);
        activity.setQuestionHeaderText(questionHeadline);
        activity.setQuestionContainerText(quizLib.getQuestions()[questionNumber]);

        fillAnswerPane(quizLib.getQuestionType()[questionNumber]);

        resetTimer();
    }

    /**
     * Updates the score by increasing correctAnswer counter, <br>
     * adding 5 * timeLeftPercentage to the score and update the <br>
     * score container text.
     */
    private void updateScore() {
        ++correctAnswers;
        score += (5 * timeLeftPercentage);
        activity.setScoreContainerText("" + score);
    }

    /**
     * Resets the time related component. Sets timeLeftPercentage to 100 <br>
     * and sets the progressBarProgress to 100, as well as setting the <br>
     * time container text to the default time text.
     */
    private void resetTimer() {
        timer = createTimer(10000, 10);
        activity.setTimerProgress(timeLeftPercentage = 100);
        activity.setTimeContainerText(res.getString(R.string.default_time_text));
        timer.start();
    }

    /**
     * Checks which questionType the question is, and fills the answerContainer <br>
     * accordingly.
     *
     * @param questionType an int with value between 0 - 2
     */
    private void fillAnswerPane(int questionType) {
        activity.clearAnswerContainer();
        activity.setAnswerButtonText(res.getString(R.string.answer));
        switch (questionType) {
            case QuestionLibrary.SINGLE_CHOICE:
                fillWithRadioButtons();
                break;
            case QuestionLibrary.MULTIPLE_CHOICE:
                fillWithCheckBoxes();
                break;
            case QuestionLibrary.OPEN_ANSWER:
                fillWithEditTextField();
                break;
        }
    }

    /**
     * Opens the resultActivity by using an Intent, and kills the current activity. <br>
     * Also sends the result related fields along in a Bundle <br>
     * for use in the ResultActivity.
     */
    private void openResultActivity() {
        Intent i = new Intent(activity, ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        bundle.putInt("correct", correctAnswers);
        bundle.putInt("numOfQuestions", quizLib.getQuizLength());
        bundle.putInt("quizTheme", quizTheme);
        bundle.putString("playerName", activity.getIntent().getExtras().getString("playerName"));
        i.putExtras(bundle);
        activity.finish();
        activity.startActivity(i);
    }

    /* ---------------------------------------------------------- */
    /* ------------ Answer container filler methods ------------- */
    /* ---------------------------------------------------------- */


    private void fillWithRadioButtons() {
        RadioGroup radioGroup = new RadioGroup(activity);
        LinearLayout.LayoutParams groupParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        radioGroup.setLayoutParams(groupParams);

        String currentAnswers[] = quizLib.getAnswers()[questionNum];
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        params.setMargins(dipToPix(4), dipToPix(4), dipToPix(4), dipToPix(4));
        for (int i = 0; i < currentAnswers.length; i++) {
            RadioButton radioButton = new RadioButton(activity);
            radioButton.setId(i + 1000);
            radioButton.setPadding(8, 8, 8, 8);
            radioButton.setText(currentAnswers[i]);
            radioButton.setLayoutParams(params);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setBackgroundColor(viewBackground);
                radioGroup.addView(radioButton);
        }
        activity.addAnswers(radioGroup);

        // Initialize and configure answer button
        activity.addAnswerButtonListener(new SingleAnswerButtonListener(radioGroup));
    }

    private void fillWithCheckBoxes() {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        String currentAnswers[] = quizLib.getAnswers()[questionNum];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 150
        );
        params.setMargins(dipToPix(4), dipToPix(4), dipToPix(4), dipToPix(4));
        for (int i = 0; i < currentAnswers.length; i++) {
            CheckBox checkBox = new CheckBox(activity);
            checkBox.setId(i + 2000);
            checkBox.setPadding(8, 8, 8, 8);
            checkBox.setText(currentAnswers[i]);
            checkBox.setLayoutParams(params);
            checkBox.setBackgroundColor(viewBackground);
            checkBoxes.add(checkBox);
            activity.addAnswers(checkBox);
        }

        activity.addAnswerButtonListener(new MultipleAnswerButtonListener(checkBoxes));
    }

    private void fillWithEditTextField() {
        final EditText editText = new EditText(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 150
        );
        params.setMargins(dipToPix(4), dipToPix(4), dipToPix(4), dipToPix(4));


        editText.setText(res.getString(R.string.open_answer_placeholder));
        editText.setLayoutParams(params);
        editText.setPadding(dipToPix(4),dipToPix(4),dipToPix(4),dipToPix(4));
        editText.setBackgroundColor(viewBackground);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setSingleLine(true);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) v;
                String answer = editText.getText().toString();
                if (editText.hasFocus() && answer.equalsIgnoreCase(res.getString(R.string.open_answer_placeholder)))
                    editText.setText("");
            }
        });
        activity.addAnswers(editText);

        activity.addAnswerButtonListener(new OpenAnswerButtonListener(editText));
    }

    public int getActivityState () {
        return activityState;
    }


    /* ---------------------------------------------------------- */
    /* ------------------ Creatable components ------------------ */
    /* ---------------------------------------------------------- */

    /**
     * @param duration length of countdown in millis
     * @param tick     how often the timer should tick, millis
     * @return CountDownTimer
     */
    private CountDownTimer createTimer(final long duration, int tick) {

        return new CountDownTimer(duration, tick) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished <= duration) {
                    // I'm doing all time stuff based on the remaining time.
                    // I get the percentage to change the ProgressBar which has 100 steps
                    // and I set the text based on the remaining millis to make it look awesome
                    timeRemaining = millisUntilFinished;
                    float timeLeftFloat = (float) millisUntilFinished / 1000;
                    String timeLeftString = String.format("%.1f s", timeLeftFloat);
                    timeLeftPercentage = (int) (millisUntilFinished / 100); //   millis / 10000 * 100
                    activity.setTimerProgress(timeLeftPercentage);
                    activity.setTimeContainerText(timeLeftString);
                }
            }

            @Override
            public void onFinish() {
                // Display a Toast to signal the player that the time for this question ran out
                Toast.makeText(activity, res.getString(R.string.out_of_time), Toast.LENGTH_SHORT).show();
                // As lon as we have not done all the questions in the current quizLib
                // call nextQuestion method
                showQuestionResult(false);
            }
        };

    }


    private class SingleAnswerButtonListener implements View.OnClickListener {
        private RadioGroup group;

        private SingleAnswerButtonListener(RadioGroup group) {
            this.group = group;
        }

        @Override
        public void onClick(View v) {
            RadioButton checkedButton;
            String answer;
            if ((checkedButton = (RadioButton) activity.findViewById(group.getCheckedRadioButtonId())) != null) {
                answer = checkedButton.getText().toString();
                checkAnswer(answer);

            } else {
                Toast.makeText(activity, "Please choose an answer", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MultipleAnswerButtonListener implements View.OnClickListener {

        private ArrayList<CheckBox> checkBoxes;
        private ArrayList<String> answers;

        private MultipleAnswerButtonListener(ArrayList<CheckBox> checkBoxes) {
            this.checkBoxes = checkBoxes;
            this.answers = new ArrayList<>();
        }

        private void registerAnswers() {
            for (CheckBox cb : checkBoxes) {
                if (cb.isChecked())
                    answers.add(cb.getText().toString());
            }
        }

        @Override
        public void onClick(View v) {
            registerAnswers();
            if (answers.size() > 0)
                checkAnswer(answers);
            else
                Toast.makeText(activity, res.getString(R.string.multiple_answer_none_chosen), Toast.LENGTH_SHORT).show();
        }
    }


    private class OpenAnswerButtonListener implements View.OnClickListener {
        private EditText editText;

        private OpenAnswerButtonListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onClick(View v) {
            String answer = editText.getText().toString().trim().toLowerCase();
            if (answer.length() > 0) {
                checkAnswer(answer);
            } else {
                Toast.makeText(activity, res.getString(R.string.open_answer_none_supplied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class NextQuestionOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (questionNum < quizLib.getQuizLength() - 1)
                nextQuestion(++questionNum);
            else
                openResultActivity();
        }
    }
}
