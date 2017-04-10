package com.example.android.questionLib;

/**
 * Created by SSon 4/7/2017.
 * This class contains a number of Questions with
 */

public class QuestionLibrary {

    // Fields
    private String questions[];
    private String answers[][];
    private String correctAnswers[][];
    private int questionType[];
    private int quizLength;

    // THEMES
    private static final int BASIC_MATH = 1;
    private static final int HARRY_POTTER = 2;

    // QUESTION TYPES
    /**
     * Question type int
     */
    public static final int SINGLE_CHOICE = 0;
    /**
     * Question type int
     */
    public static final int MULTIPLE_CHOICE = 1;
    /**
     * Question type int
     */
    public static final int OPEN_ANSWER = 2;


    public QuestionLibrary(int THEME) {

        if (THEME == BASIC_MATH)
            basicMathChosen();
        else if (THEME == HARRY_POTTER)
            harryPotterChosen();


    }

    private void basicMathChosen() {
        this.questions = basicMathQuestions;
        this.answers = basicMathAnswers;
        this.correctAnswers = basicMathCorrectAnswers;
        this.questionType = basicMathQuestionType;
        this.quizLength = this.questions.length;
    }

    private void harryPotterChosen() {
        this.questions = harryPotterQuestions;
        this.answers = harryPotterAnswers;
        this.correctAnswers = harryPotterCorrectAnswers;
        this.questionType = harryPotterQuestionType;
        this.quizLength = this.questions.length;
    }


    // Getters
    public String[] getQuestions() {
        return questions;
    }

    public String[][] getAnswers() {
        return answers;
    }

    public String[][] getCorrectAnswers() {
        return correctAnswers;
    }

    public int[] getQuestionType() {
        return questionType;
    }

    public int getQuizLength() {
        return quizLength;
    }

    public static String[] getThemes() {
        return new String[]{
                "Choose a theme..",
                "Basic math",
                "Harry Potter"
        };
    }

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

    private static final String basicMathCorrectAnswers[][] = new String[][]{
            {"42"}, {"42"}, {"42"}, {"42"}, {"42"}, {"42"}, {"42"}, {"42"}, {"42"}, {"-42", "42"}
    };

    private static final int basicMathQuestionType[] = new int[]{
            SINGLE_CHOICE,  // 0
            SINGLE_CHOICE,  // 1
            SINGLE_CHOICE,  // 2
            SINGLE_CHOICE,  // 3
            SINGLE_CHOICE,  // 4
            SINGLE_CHOICE,  // 5
            SINGLE_CHOICE,  // 6
            SINGLE_CHOICE,  // 7
            OPEN_ANSWER,  // 8
            MULTIPLE_CHOICE,  // 9
    };


    private static final String harryPotterQuestions[] = new String[]{
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

    private static final String harryPotterAnswers[][] = new String[][]{
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


    private static final String harryPotterCorrectAnswers[][] = new String[][]{
            {"42"}, {"42"}, {"42"}, {"42"}, {"42"}, {"42"}, {"42"}, {"42"}, {"42"}, {"-42", "42"}
    };

    private static final int harryPotterQuestionType[] = new int[]{
            SINGLE_CHOICE,  // 0
            SINGLE_CHOICE,  // 1
            SINGLE_CHOICE,  // 2
            SINGLE_CHOICE,  // 3
            SINGLE_CHOICE,  // 4
            SINGLE_CHOICE,  // 5
            SINGLE_CHOICE,  // 6
            SINGLE_CHOICE,  // 7
            OPEN_ANSWER,  // 8
            MULTIPLE_CHOICE,  // 9
    };


}
