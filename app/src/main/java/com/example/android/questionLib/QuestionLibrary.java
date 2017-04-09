package com.example.android.questionLib;


/**
 * Created by oddis on 4/7/2017.
 * This class contains a number of Questions with
 */

public class QuestionLibrary {

    // Fields
    private final String QUESTIONS[];
    private final String ANSWERS[][];
    private final String CORRECT_ANSWER[];
    private int quizLength;

    // THEMES
    public static final int BASIC_MATH = 0;

    public QuestionLibrary(int THEME) {

        if (THEME == BASIC_MATH) {
            this.QUESTIONS = basicMathQuestions;
            this.ANSWERS = basicMathAnswers;
            this.CORRECT_ANSWER = basicMathCorrectAnswers;
            this.quizLength = this.QUESTIONS.length;
        } else {
            this.QUESTIONS = new String[]{};
            this.ANSWERS = new String[][]{};
            this.CORRECT_ANSWER = new String[]{};
            this.quizLength = this.QUESTIONS.length;
        }
    }

    // Getters
    public String[] getQUESTIONS() { return QUESTIONS; }
    public String[][] getANSWERS() {
        return ANSWERS;
    }
    public String[] getCORRECT_ANSWER() {
        return CORRECT_ANSWER;
    }
    public int getQuizLength() { return quizLength; }


    /* ------------------------------------------------------ */
    /* ----------------  QUESTION ARRAYS -------------------- */
    /* ------------------------------------------------------ */


    private static final String basicMathQuestions[] = new String[]{
            "40 + 2 = x\nx = ?",  // 0
            "3 * 15 - 3 = x\nx = ?",  // 1
            "72 / 12 * 7= x\nx = ?", // 2
            "14 * 3 = x\nx = ?", // 3
            "(64 / 4) * 7 - 14 = x\nx = ?",  // 4
            "2^5 + 10 = x\nx = ?",   // 5
            "6 * (-14) / (-2) = x\nx = ?", // 6
            "121 / 11 * 4 - 2 = x\nx = ?", // 7
            "4^3 - 22 = x\nx = ?", // 8
            "squareRoot(42 * 42) = x\nx = ?" // 9
    };

    private static final String basicMathAnswers[][] = new String[][]{
            {"24", "38", "42"}, // 0
            {"42", "36", "-135"}, // 1
            {"12", "42", "69"}, // 2
            {"32", "52", "42"}, // 3
            {"12", "42", "90"}, // 4
            {"26", "74", "42"}, // 5
            {"42", "-42", "0"}, // 6
            {"37", "42", "51"}, // 7
            {"95", "42", "23"}, // 8
            {"-42", "76", "42"} // 9
    };

    private static final String basicMathCorrectAnswers[] = new String[]{
            "42", "42", "42", "42", "42", "42", "42", "42", "42", "42"
    };


}
