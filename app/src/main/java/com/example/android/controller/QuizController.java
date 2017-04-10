package com.example.android.controller;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
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


public class QuizController {

    private QuizActivity activity;
    private QuestionLibrary quizLib;

    private CountDownTimer timer;
    private Resources res;


    private int questionNum = 0,
            score = 0,
            timeLeftPercentage = 100,
            correctAnswers = 0;

    public QuizController(QuizActivity activity, QuestionLibrary quizLib) {
        this.activity = activity;
        this.quizLib = quizLib;
        this.res = activity.getResources();
        timer = createTimer(10000, 10);


        nextQuestion(questionNum);
    }


    private void checkAnswer(String answer) {
        timer.cancel();

        boolean answerIsCorrect = false;
        String correctAnswer = quizLib.getCorrectAnswers()[questionNum][0].trim();
        // I couldn't figure out why I kept getting the wrong result when I typed in the correct answer.
        // I tried "answer == correctAnswer", "answer.equals(correctAnswer)",
        // I ended up using this string function instead, it is usually used for sorting
        // and will return -1 if answer(String 1)'s first letter is before correctAnswer(String 2)'s first letter
        // in the alphabet
        if (answer.compareToIgnoreCase(correctAnswer) == 0) {   // there is only one answer to check in this case
            if (!answer.equals(res.getString(R.string.open_answer_placeholder))) {
                updateScore();
                answerIsCorrect = true;
            }
        }

        showQuestionResult(answerIsCorrect);
    }

    private void showQuestionResult(boolean answerIsCorrect) {

        String result = "";
        if (answerIsCorrect)
            result += res.getString(R.string.correct_answer) + "\n";
        else
            result += res.getString(R.string.incorrect_answer) + "\n";

        if (questionNum < quizLib.getQuizLength() - 1) {
            result += res.getString(R.string.next_question);
        } else {
            result += res.getString(R.string.show_results);
        }
        activity.setQuestionContainerText(result);

        activity.setQuestionContainerOnClickListener(new NextQuestionOnClickListener());

    }

    private void checkAnswer(ArrayList<String> suppliedAnswers) {
        timer.cancel();

        String[] correctAnswers = quizLib.getCorrectAnswers()[questionNum];

        if (correctAnswers.length == suppliedAnswers.size()) {
            int correctCounter = 0;
            for (String a : correctAnswers)
                for (String b : suppliedAnswers)
                    if (a.equals(b))
                        correctCounter++;

            if (correctCounter == correctAnswers.length) {
                updateScore();
                showQuestionResult(true);
            } else {
                showQuestionResult(false);
            }
        }
    }


    private void nextQuestion(int i) {

        String questionHeadline = res.getString(R.string.question_header) + " " + (1 + i);
        activity.setQuestionHeaderText(questionHeadline);
        activity.setQuestionContainerText(quizLib.getQuestions()[i]);

        fillAnswerPane(quizLib.getQuestionType()[i]);

        resetTimer();

    }

    private void updateScore() {
        ++correctAnswers;
        score += (5 * timeLeftPercentage);
        activity.setScoreContainerText("" + score);
    }

    private void resetTimer() {
        timer.cancel();
        activity.setTimerProgress(timeLeftPercentage = 100);
        activity.setTimeContainerText(res.getString(R.string.default_time_text));
        timer.start();
    }

    private void fillAnswerPane(int questionType) {
        activity.clearAnswerContainer();
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


    private void openResultActivity() {
        Intent i = new Intent(activity, ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        bundle.putInt("correct", correctAnswers);
        bundle.putInt("numOfQuestions", quizLib.getQuizLength());
        i.putExtras(bundle);
        activity.finish();
        activity.startActivity(i);
    }

    /* ---------------------------------------------------------- */
    /* ------------ Answer container filler methods ------------- */
    /* ---------------------------------------------------------- */


    private void fillWithRadioButtons() {
        RadioGroup radioGroup = new RadioGroup(activity);
        radioGroup.setGravity(Gravity.CENTER);
        String currentAnswers[] = quizLib.getAnswers()[questionNum];

        for (int i = 0; i < currentAnswers.length; i++) {
            RadioButton radioButton = new RadioButton(activity);
            radioButton.setId(i + 1000);
            radioButton.setPadding(4, 4, 4, 4);
            radioButton.setText(currentAnswers[i]);
            radioGroup.addView(radioButton);
        }
        activity.addAnswers(radioGroup);

        // Initialize and configure answer button
        activity.addAnswerButtonListener(new SingleAnswerButtonListener(radioGroup));
    }

    private void fillWithCheckBoxes() {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        String currentAnswers[] = quizLib.getAnswers()[questionNum];

        for (int i = 0; i < currentAnswers.length; i++) {
            CheckBox checkBox = new CheckBox(activity);
            checkBox.setId(i + 2000);
            checkBox.setPadding(4, 4, 4, 4);
            checkBox.setText(currentAnswers[i]);
            checkBoxes.add(checkBox);
            activity.addAnswers(checkBox);
        }

        activity.addAnswerButtonListener(new MultipleAnswerButtonListener(checkBoxes));
    }

    private void fillWithEditTextField() {
        final EditText editText = new EditText(activity);
        editText.setText(res.getString(R.string.open_answer_placeholder));
        editText.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setHeight(150);
        editText.setPadding(4, 4, 4, 4);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) v;
                if (editText.hasFocus())
                    editText.setText("");
            }
        });
        activity.addAnswers(editText);

        activity.addAnswerButtonListener(new OpenAnswerButtonListener(editText));
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
                if (questionNum < quizLib.getQuizLength() - 1) {
                    nextQuestion(++questionNum);
                } else {  // if done, call openResultActivity method
                    openResultActivity();
                }
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

            activity.setQuestionContainerOnClickListener(null);
        }
    }
}
