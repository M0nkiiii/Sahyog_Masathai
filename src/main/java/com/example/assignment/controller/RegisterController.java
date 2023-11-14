package com.example.assignment.controller;

import com.example.assignment.model.Application;
import com.opencsv.CSVWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class RegisterController {

    @FXML private TextField userName;
    @FXML private TextField user_email;
    @FXML private DatePicker dateOfBirth;
    @FXML private ComboBox<String> userGender;
    @FXML private ComboBox<String> userNationality;
    @FXML private PasswordField userPassword;
    @FXML private Button btn_register;
    @FXML private Label emailError;
    @FXML private HBox root;
    @FXML private Button btn_toLogin;

    private Application application;

    private boolean isFormValid;

    @FXML
    public void initialize() {
        // Initialization code, if needed
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @FXML
    public void register(ActionEvent event) throws IOException {
        String name = userName.getText();
        String userEmail = user_email.getText();
        String birthYear = dateOfBirth.getValue().toString();
        String gender = userGender.getValue();
        String nationality = userNationality.getValue();
        String password = userPassword.getText();
        String pathToCSV = "src/main/resources/com/example/assignment/userData.csv";

        if (userEmail.isEmpty()) {
            emailError.setText("Email can't be empty");
            isFormValid = false;
        } else {
            isFormValid = true;
        }

        if (isFormValid) {
            try {
                FileWriter fileWriter = new FileWriter(pathToCSV, true);
                CSVWriter csvWriter = new CSVWriter(fileWriter);

                String[] csvData = {name, userEmail, password, birthYear, gender, nationality};
                csvWriter.writeNext(csvData);
                csvWriter.close();
                clearFormElements();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception as needed, e.g., show an error message to the user
            }
            application.loginScene();
        }
    }

    public void clearFormElements() {
        userName.clear();
        user_email.clear();
        userPassword.clear();
        dateOfBirth.setValue(null);
        userGender.getSelectionModel().clearSelection();
        userNationality.getSelectionModel().clearSelection();
    }

    @FXML
    public void toLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/assignment/login-view.fxml"));
            Parent root = loader.load();

            Scene registerScene = new Scene(root);
            Stage stage = (Stage) btn_toLogin.getScene().getWindow();
            stage.setScene(registerScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
