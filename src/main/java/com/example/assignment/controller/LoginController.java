package com.example.assignment.controller;

import com.example.assignment.model.Application;
import com.opencsv.CSVReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    @FXML private TextField user_email;
    @FXML private PasswordField userPassword;
    @FXML private Button btn_login;
    @FXML private Label resultLabel;
    @FXML private HBox root;
    @FXML private Button btn_toRegister;

    private static final String CSV_PATH = "src/main/resources/com/example/assignment/userData.csv";
    private Application application;

    @FXML
    public void initialize() {
        // Initialization code, if needed
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @FXML
    public void login(ActionEvent event) {
        String userEmail = user_email.getText();
        String password = userPassword.getText();

        String userName = isValidUser(userEmail, password);
        if (userName != null) {
            resultLabel.setText("Login Successful!");
            loadLoggedInScene(userName);
        } else {
            resultLabel.setText("Invalid email or password");
        }
    }

    private String isValidUser(String userEmail, String password) {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_PATH))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String csvEmail = nextLine[1];
                String csvPassword = nextLine[2];
                String csvName = nextLine[0];

                if (userEmail.equals(csvEmail) && password.equals(csvPassword)) {
                    return csvName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    public void toRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assignment/register-view.fxml"));
            Parent root = loader.load();

            RegisterController registerController = loader.getController();
            registerController.setApplication(application);

            Scene registerScene = new Scene(root);
            Stage stage = (Stage) btn_toRegister.getScene().getWindow();
            stage.setScene(registerScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLoggedInScene(String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assignment/logged-in.fxml"));
            Parent root = loader.load();

            LoggedinController loggedinController = loader.getController();
            loggedinController.setLoggedInUserEmail(user_email.getText());
            loggedinController.setWelcomeLabel(userName);

            Scene loggedInScene = new Scene(root);
            Stage stage = (Stage) btn_login.getScene().getWindow();
            stage.setScene(loggedInScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
