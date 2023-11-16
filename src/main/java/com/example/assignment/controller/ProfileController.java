package com.example.assignment.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;
import javafx.stage.Stage;

public class ProfileController {

    @FXML
    private Label label_name;
    @FXML
    private Label label_email;
    @FXML
    private Label label_dob;
    @FXML
    private Label label_gender;
    @FXML
    private Label label_nation;
    @FXML
    private ImageView image_nation;
    @FXML
    private Button btn_back;

    private String userEmail;

    private String loggedInUserEmail;

    private static final String CSV_PATH = "src/main/resources/com/example/assignment/userData.csv";

    public void setUserEmail(String userEmail) {
        System.out.println("setUserEmail called with email: " + userEmail);
        this.userEmail = userEmail;
        loadUserProfile();
    }

    @FXML
    private void initialize() {

    }

    private void loadUserProfile() {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_PATH))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String csvEmail = nextLine[1];
                if (userEmail != null && userEmail.equals(csvEmail)) {
                    String csvName = nextLine[0];
                    String birthYear = nextLine[3];
                    String gender = nextLine[4];
                    String nationality = nextLine[5];

                    label_name.setText(csvName);
                    label_email.setText(userEmail);
                    label_dob.setText(birthYear);
                    label_gender.setText(gender);
                    label_nation.setText(nationality);

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assignment/logged-in.fxml"));
            Parent root = loader.load();


            Scene loggedInScene = new Scene(root);


            Stage stage = (Stage) btn_back.getScene().getWindow();


            stage.setScene(loggedInScene);


            LoggedinController loggedinController = loader.getController();
            if (loggedinController != null) {
                loggedinController.setLoggedInUserEmail(userEmail);
            }


            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }



}
