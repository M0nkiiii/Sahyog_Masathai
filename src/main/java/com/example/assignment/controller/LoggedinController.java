package com.example.assignment.controller;

import com.example.assignment.model.Application;
import com.opencsv.CSVReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoggedinController {

    @FXML
    private Label label_welcome;
    @FXML
    private Button btn_profile;
    @FXML
    private Button btn_takeexam;

    private String loggedInUserEmail;
    private String loggedInUserName;
    private String loggedInUserGender;
    private String loggedInUserNation;
    private static final String CSV_PATH = "src/main/resources/com/example/assignment/userData.csv";

    private Application application;

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setWelcomeLabel(String userName) {
        label_welcome.setText("Welcome, " + userName + "!");
    }

    public void setLoggedInUserEmail(String email) {
        System.out.println("DEBUG: setLoggedInUserEmail: " + email);
        this.loggedInUserEmail = email;
        loadUserDetails(); // Ensure userDetails are loaded when email is set
    }

    public void loadUserDetails() {
        if (loggedInUserEmail != null) {
            Map<String, String> userDetails = readUserDetailsFromCSV(loggedInUserEmail);
            loggedInUserName = userDetails.get("name");
            loggedInUserGender = userDetails.get("gender");
            loggedInUserNation = userDetails.get("nation");
        }
    }

    @FXML
    private void handleProfileButton() {
        System.out.println("DEBUG: handleProfileButton - loggedInUserEmail: " + loggedInUserEmail);

        if (loggedInUserEmail != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assignment/profile.fxml"));
                Parent root = loader.load();

                ProfileController profileController = loader.getController();
                profileController.setUserEmail(loggedInUserEmail); // Set the loggedInUserEmail

                Scene profileScene = new Scene(root);

                Stage stage = (Stage) btn_profile.getScene().getWindow();
                stage.setScene(profileScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error: loggedInUserEmail is null");
        }
    }

    @FXML
    private void handletakeexambtn() {
        if (loggedInUserEmail != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assignment/exam.fxml"));
                Parent root = loader.load();

                ExamController examController = loader.getController();
                examController.setUserEmail(loggedInUserEmail);

                Scene profileScene = new Scene(root);

                Stage stage = (Stage) btn_takeexam.getScene().getWindow();
                stage.setScene(profileScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error: loggedInUserEmail is null");
        }
    }

    // Method to read user details from CSV
    private Map<String, String> readUserDetailsFromCSV(String email) {
        Map<String, String> userDetails = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(CSV_PATH))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equalsIgnoreCase(email)) {
                    userDetails.put("name", nextLine[1]);
                    userDetails.put("gender", nextLine[2]);
                    userDetails.put("nation", nextLine[3]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userDetails;
    }
}
