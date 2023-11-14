package com.example.assignment.controller;

import com.opencsv.CSVReader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExamController {

    @FXML
    private Button btn_back;

    @FXML
    private Label label_name;
    @FXML
    private Label label_email;

    @FXML
    private Label label_timer;

    @FXML
    private Label label_question;

    @FXML
    private Button btn_answer1;

    @FXML
    private Button btn_answer2;

    private Timeline timeline;

    private List<Question> questions = new ArrayList<>();

    private int remainingTimeInSeconds = 5 * 60; // 5 minutes
    private int totalQuestions = 20;
    private int currentQuestionIndex = -1;
    private int correctAnswersCount = 0;
    private int incorrectAnswersCount = 0;

    private String userEmail;
    private String loggedInUserName;

    private static final String CSV_PATH = "src/main/resources/com/example/assignment/userData.csv";

    private ResultController resultController; // Reference to ResultController

    // Default constructor required for FXMLLoader
    public ExamController() {
    }

    public String getLoggedInUserName() {
        return loggedInUserName;
    }
    public void setResultController(ResultController resultController) {
        this.resultController = resultController;
    }

    public void setUserEmail(String userEmail) {
        System.out.println("setUserEmail called with email: " + userEmail);
        this.userEmail = userEmail;
        loadUserProfile();
    }

    @FXML
    public void initialize() {
        // Initialize the countdown timer
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateTimerLabel();
            remainingTimeInSeconds--;

            if (remainingTimeInSeconds < 0) {
                handleTimeUp();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Load questions from file
        loadQuestionsFromFile();

        // Load the first question
        loadNextQuestion();
    }

    private void loadUserProfile() {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_PATH))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String csvEmail = nextLine[1];
                if (userEmail != null && userEmail.equals(csvEmail)) {
                    loggedInUserName = nextLine[0];
                    String birthYear = nextLine[3];
                    String gender = nextLine[4];
                    String nationality = nextLine[5];

                    label_name.setText(loggedInUserName);
                    label_email.setText(userEmail);

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadQuestionsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/assignment/question_list.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String questionText = parts[0];
                    String correctAnswer = parts[1];
                    Question question = new Question(questionText, correctAnswer);
                    questions.add(question);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNextQuestion() {
        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size()) {
            System.out.println("Loading Question #" + currentQuestionIndex);
            Question currentQuestion = questions.get(currentQuestionIndex);

            label_question.setText(currentQuestion.getQuestionText());
            btn_answer1.setText(currentQuestion.getOption1());
            btn_answer2.setText(currentQuestion.getOption2());
        } else if (currentQuestionIndex == questions.size()) {
            // All questions have been answered
            handleEndOfQuestions();
        }
    }

    @FXML
    private void handleAnswerButton(ActionEvent event) {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            Button clickedButton = (Button) event.getSource();
            String selectedAnswer = clickedButton.getText();

            Question currentQuestion = questions.get(currentQuestionIndex);

            System.out.println("Clicked Button: " + clickedButton.getId());
            System.out.println("Selected Answer: " + selectedAnswer);
            System.out.println("Correct Answer: " + currentQuestion.getCorrectAnswer());

            if (Objects.equals(selectedAnswer, currentQuestion.getCorrectAnswer())) {
                correctAnswersCount++;
            }

            loadNextQuestion();
        } else {
            // Handle the case where the currentQuestionIndex is out of bounds
            System.out.println("Error: Index out of bounds");
        }
    }

    private void handleEndOfQuestions() {
        int incorrectAnswersCount = totalQuestions - correctAnswersCount;

        storeResults(totalQuestions, correctAnswersCount, incorrectAnswersCount);

        // Automatically show the result after the exam finishes
        navigateToResultScene(loggedInUserName);
        currentQuestionIndex = -1; // Reset the index for future attempts
    }

    private void handleTimeUp() {
        timeline.stop();
        // Handle time-up scenario
        // For example, navigate to the result scene or show a time-up message
        handleEndOfQuestions();
    }

    private void storeResults(int totalQuestions, int correctAnswersCount, int incorrectAnswersCount) {
        // Implement logic to store results in "test_result.txt"
        // Format: Name,TotalQuestions,CorrectAnswers,IncorrectAnswers,Marks
        // Example: John Doe,10,7,3,70

        String resultFileName = "src/main/resources/com/example/assignment/test_result.txt";

        try (FileWriter writer = new FileWriter(resultFileName, true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {

            // Append the result to the file
            int marks = calculateMarksList(totalQuestions, correctAnswersCount).get(0);
            if (marks >= 50) {
                out.println(loggedInUserName + "," + totalQuestions + "," + correctAnswersCount + "," + incorrectAnswersCount + "," + marks + ",Pass");
            } else {
                out.println(loggedInUserName + "," + totalQuestions + "," + correctAnswersCount + "," + incorrectAnswersCount + "," + marks + ",Fail");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLoggedInUserName(String loggedInUserName) {
        this.loggedInUserName = loggedInUserName;
        label_name.setText(loggedInUserName);
    }

    private List<Integer> calculateMarksList(int totalQuestions, int correctAnswersCount) {
        // You can implement your own logic for calculating marks based on your requirements
        // For example, you might want to assign a certain score for each correct answer
        // and calculate the total marks accordingly.
        List<Integer> marksList = new ArrayList<>();
        marksList.add((correctAnswersCount * 100) / totalQuestions);
        return marksList;
    }

    private void navigateToResultScene(String selectedName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assignment/result.fxml"));
            Parent root = loader.load();
            ResultController resultController = loader.getController();

            // Pass ResultController instance to ExamController
            resultController.setExamController(this);  // Set the reference to ResultController
            resultController.displayResultsForUser(selectedName);

            Scene resultScene = new Scene(root);
            Stage stage = (Stage) btn_answer1.getScene().getWindow();
            stage.setScene(resultScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateTimerLabel() {
        int minutes = remainingTimeInSeconds / 60;
        int seconds = remainingTimeInSeconds % 60;
        label_timer.setText(String.format("%02d:%02d", minutes, seconds));
    }
}


