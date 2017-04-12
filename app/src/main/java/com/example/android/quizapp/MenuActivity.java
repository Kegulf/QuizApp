package com.example.android.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.questionLib.QuestionLibrary;


public class MenuActivity extends AppCompatActivity {

    EditText nameEditText;
    Spinner quizThemeSpinner;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        nameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameEditText.getText().toString().equalsIgnoreCase(getString(R.string.fill_in_name)))
                    nameEditText.setText("");
            }
        });


        quizThemeSpinner = (Spinner) findViewById(R.id.quiz_theme_spinner);
        fillQuizThemeSpinner();


        startButton = (Button) findViewById(R.id.start_quiz_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeholderText = getString(R.string.fill_in_name);
                String inputName = nameEditText.getText().toString();
                if(inputName.length() > 1 && !inputName.equalsIgnoreCase(placeholderText)) {
                    if (quizThemeSpinner.getSelectedItemPosition() != 0) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("quizTheme", quizThemeSpinner.getSelectedItemPosition());
                        bundle.putString("playerName", nameEditText.getText().toString());
                        Intent intent = new Intent(MenuActivity.this, QuizActivity.class);
                        intent.putExtras(bundle);

                        MenuActivity.this.finish();
                        startActivity(intent);
                    } else
                        Toast.makeText(MenuActivity.this, getString(R.string.theme_none_chosen), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MenuActivity.this, getString(R.string.name_none_supplied), Toast.LENGTH_SHORT).show();
                }
        });
    }
    private void fillQuizThemeSpinner() {
        String[] themes = QuestionLibrary.getThemes();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, themes);

        quizThemeSpinner.setAdapter(adapter);
    }
}
