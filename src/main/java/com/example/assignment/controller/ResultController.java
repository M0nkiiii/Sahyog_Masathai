package com.example.assignment.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ResultController {

    @FXML
    private Label label_userName;

    @FXML
    private Label label_result;

    @FXML
    private Label label_statistics;

    private String loggedInUserName;
    private String loggedInUserGender;
    private String loggedInUserNation;

    public void setLoggedInUserName(String loggedInUserName) {
        this.loggedInUserName = loggedInUserName;
        // Set the label or perform any other actions you need with the username
        label_userName.setText(loggedInUserName);  // Assuming you have a label named label_userName
    }

    public void setLoggedInUserGender(String gender) {
        this.loggedInUserGender = gender;
    }

    public void setLoggedInUserNation(String nation) {
        this.loggedInUserNation = nation;
    }

    @FXML
    public void initialize(String name, String gender, String nation) {
        setLoggedInUserName(name);
        setLoggedInUserGender(gender);
        setLoggedInUserNation(nation);

        // Other initialization code
        //label_name.setText(" " + loggedInUserName);
        // Set other labels here
    }

    public void setResults(String name, int totalQuestions, int correctAnswersCount, int incorrectAnswersCount, List<Integer> marks) {
        double mean = calculateMean(marks);
        int mode = calculateMode(marks);
        int median = calculateMedian(marks);
//        double standardDeviation = calculateStandardDeviation(marks);

        initialize(name, loggedInUserGender, loggedInUserNation);
//        label_name.setText(" " + name);

        if(correctAnswersCount<=10){
            label_result.setText("Result: " + correctAnswersCount + " out of " + totalQuestions + " correct. You have failed successfully.");
        }
        else{
            label_result.setText("Result: " + correctAnswersCount + " out of " + totalQuestions + " correct. You have passed successfully.");
        }
//        label_result.setText("Result: " + correctAnswersCount + " out of " + totalQuestions + " correct");
        label_statistics.setText("Statistics:\nMean: " + mean + "\nMode: " + mode + "\nMedian: " + median );
        initialize(name, loggedInUserGender, loggedInUserNation);
    }

    private double calculateMean(List<Integer> marks) {
        return marks.isEmpty() ? 0.0 : marks.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    private int calculateMode(List<Integer> marks) {
        if (marks.isEmpty()) {
            return 0; // or handle the case in a way that makes sense for your application
        }

        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int mark : marks) {
            frequencyMap.put(mark, frequencyMap.getOrDefault(mark, 0) + 1);
        }

        int mode = 0;
        int maxFrequency = 0;

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                mode = entry.getKey();
                maxFrequency = entry.getValue();
            }
        }

        return mode;
    }

    private int calculateMedian(List<Integer> marks) {
        int size = marks.size();

        if (size == 0) {
            return 0; // or handle the case in a way that makes sense for your application
        }

        int middle = size / 2;

        if (size % 2 == 0) {
            // If the size is even, calculate the average of the two middle elements
            return (marks.get(middle - 1) + marks.get(middle)) / 2;
        } else {
            // If the size is odd, return the middle element
            return marks.get(middle);
        }
    }
}

//    private double calculateStandardDeviation(List<Integer> marks) {
//        double mean = calculateMean(marks);
//
//        if (mean == 0.0) {
//            return 0.0; // or handle the case in a way that makes sense for your application
//        }
//
//        double sumOfSquares = marks.stream().mapToDouble(mark -> Math.pow(mark - mean, 2)).sum();
//        return Math.sqrt(sumOfSquares / marks.size());
//    }
//}