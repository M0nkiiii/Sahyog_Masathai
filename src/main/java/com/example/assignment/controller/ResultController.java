package com.example.assignment.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ResultController {

    @FXML
    private Label label_userName;

    @FXML
    private Label label_result;

    @FXML
    private Label label_statistics;

    @FXML
    private ComboBox<String> comboBoxNames;  // Add ComboBox

    private String loggedInUserName;
    private String loggedInUserGender;
    private String loggedInUserNation;

    private static final String RESULT_PATH = "src/main/resources/com/example/assignment/test_result.txt";

    private ExamController examController; // Reference to ExamController

    public void setExamController(ExamController examController) {
        this.examController = examController;
    }

    @FXML
    public void initialize() {

        List<String> names = readUniqueNamesFromResultFile();
        comboBoxNames.getItems().addAll(names);


        if (examController != null) {
            displayResultsForUser(examController.getLoggedInUserName());
        }
    }

    private List<String> readUniqueNamesFromResultFile() {
        Set<String> uniqueNames = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RESULT_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String name = parts[0];
                    uniqueNames.add(name.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(uniqueNames);
    }

    @FXML
    private void handleNameSelection() {
        String selectedName = comboBoxNames.getValue();
        if (selectedName != null) {
            // Load and display results for the selected name
            displayResultsForUser(selectedName);
        }
    }

    public void setLoggedInUserName(String loggedInUserName) {
        this.loggedInUserName = loggedInUserName;
        label_userName.setText(loggedInUserName);
    }

    public void setLoggedInUserGender(String gender) {
        this.loggedInUserGender = gender;
    }

    public void setLoggedInUserNation(String nation) {
        this.loggedInUserNation = nation;
    }


    public void setResults(String name, int totalQuestions, int correctAnswersCount, int incorrectAnswersCount, List<Integer> marks) {
        double mean = calculateMean(marks);
        int mode = calculateMode(marks);
        int median = calculateMedian(marks);


        setLoggedInUserName(name);
        setLoggedInUserGender(loggedInUserGender);
        setLoggedInUserNation(loggedInUserNation);

        // Display results
        if (correctAnswersCount >= 10) {
            label_result.setText("Result: " + correctAnswersCount + " out of " + totalQuestions + " correct. You have passed successfully.");
        } else {
            label_result.setText("Result: " + correctAnswersCount + " out of " + totalQuestions + " correct. You have failed successfully.");
        }
        label_statistics.setText("Statistics:\nMean: " + mean + "\nMode: " + mode + "\nMedian: " + median);
    }

    private double calculateMean(List<Integer> marks) {
        return marks.isEmpty() ? 0.0 : marks.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    private int calculateMode(List<Integer> marks) {
        if (marks.isEmpty()) {
            return 0;
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
            return 0;
        }

        int middle = size / 2;

        if (size % 2 == 0) {

            return (marks.get(middle - 1) + marks.get(middle)) / 2;
        } else {

            return marks.get(middle);
        }
    }

    private List<String> readNamesFromResultFile() {
        List<String> names = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RESULT_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String name = parts[0];
                    names.add(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }

    private List<String> loadResultsForName(String selectedName) {
        List<String> results = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RESULT_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(selectedName)) {
                    for (String part : parts) {
                        results.add(part.trim());
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private List<Integer> calculateMarksList(int totalQuestions, int correctAnswersCount) {
        List<Integer> marksList = new ArrayList<>();
        marksList.add((correctAnswersCount * 100) / totalQuestions);
        return marksList;
    }
    public void displayResults(List<String> results) {
        // Extract individual parameters from the list
        String name = results.get(0);
        int totalQuestions = Integer.parseInt(results.get(1));
        int correctAnswers = Integer.parseInt(results.get(2));
        int incorrectAnswers = Integer.parseInt(results.get(3));
        int marks = Integer.parseInt(results.get(4));
        String result = results.get(5);

        label_userName.setText(name);
        label_result.setText("Result: " + correctAnswers + " out of " + totalQuestions + " correct. " + result);


        List<Integer> marksList = calculateMarksList(totalQuestions, correctAnswers);
        double mean = calculateMean(marksList);
        int mode = calculateMode(marksList);
        int median = calculateMedian(marksList);
        double standardDeviation = calculateStandardDeviation(marksList);


        label_statistics.setText("Statistics:\nMean: " + mean + "\nMode: " + mode + "\nMedian: " + median + "\nStandard Deviation: " + standardDeviation);
    }
    private double calculateStandardDeviation(List<Integer> marks) {
        double mean = calculateMean(marks);
        double sumSquaredDiff = marks.stream().mapToDouble(mark -> Math.pow(mark - mean, 2)).sum();
        return Math.sqrt(sumSquaredDiff / marks.size());
    }



        public void displayResultsForUser(String userName) {

        List<String> results = loadResultsForName(userName);
        displayResults(results);
    }
}