package com.example.assignment.model;

import com.example.assignment.controller.LoginController;
import com.example.assignment.controller.RegisterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    private Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        this.stage  = stage;
        //loginScene();


        registerScene();
    }

    public void loginScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/assignment/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Login!");
        stage.setScene(scene);
        stage.show();

        LoginController loginController = fxmlLoader.getController();
        loginController.setApplication(this);
//        loginController.setStage(stage);

    }



    public void registerScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/assignment/register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("Register!");
        stage.setScene(scene);
        stage.show();

        RegisterController registerController = fxmlLoader.getController();
        registerController.setApplication(this);
    }

    public void loggedInScene(String userName) {
    }
}