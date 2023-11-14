package com.example.assignment.controller;

import java.util.Random;

public class Question {

    private String questionText;
    private String correctAnswer;
    private String option1;
    private String option2;

    public Question(String questionText, String correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.option1 = correctAnswer;
        this.option2 = generateIncorrectOption(correctAnswer, option1);
    }


    private String generateIncorrectOption(String correctAnswer, String option1) {
        Random random = new Random();
        String incorrectOption;

        do {
            // Ensure the incorrect option is not equal to correctAnswer or option1
            incorrectOption = "" + random.nextInt(100) ;
        } while (incorrectOption.equals(correctAnswer) || incorrectOption.equals(option1));

        return incorrectOption;
    }




    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public boolean isCorrectAnswer(String selectedAnswer) {
        return correctAnswer.equals(selectedAnswer);
    }
}
