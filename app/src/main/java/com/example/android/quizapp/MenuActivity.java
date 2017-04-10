package com.example.android.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.questionLib.QuestionLibrary;


public class MenuActivity extends AppCompatActivity {

    Button startButton;
    Spinner quizThemeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        quizThemeSpinner = (Spinner) findViewById(R.id.quiz_theme_spinner);
        fillQuizThemeSpinner();

        startButton = (Button) findViewById(R.id.start_quiz_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(quizThemeSpinner.getSelectedItemPosition() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("quizTheme", quizThemeSpinner.getSelectedItemPosition());
                    Intent intent = new Intent(MenuActivity.this, QuizActivity.class);
                    intent.putExtras(bundle);

                    MenuActivity.this.finish();
                    startActivity(intent);
                } else
                    Toast.makeText(MenuActivity.this, getString(R.string.theme_none_chosen), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void fillQuizThemeSpinner() {
        String[] themes = QuestionLibrary.getThemes();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, themes);

        quizThemeSpinner.setAdapter(adapter);
    }
}
