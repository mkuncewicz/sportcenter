package com.example.sportcenterv1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MenuController {

    @FXML
    private Button buttonChoice1;

    @FXML
    private Button buttonChoice2;

    @FXML
    private Button buttonChoice3;

    @FXML
    private Button buttonChoice4;

    @Autowired
    private ApplicationContext springContext;

    @FXML
    protected void handleButton(ActionEvent event) throws Exception{
        Button clickedButton = (Button) event.getSource();

        String fxmlFile = "";

        if(clickedButton == buttonChoice1) {
            fxmlFile = "/com/example/sportcenterv1/employeeManager.fxml";
        }
        if (!fxmlFile.isEmpty()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlFile));
            loader.setControllerFactory(springContext::getBean); // Musisz przekazać kontekst Springa

            Parent root = loader.load();
            Stage window = (Stage) clickedButton.getScene().getWindow();
            window.setScene(new Scene(root, 1920, 1080));
        }
    }

}
