package com.example.sportcenterv1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MenuController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button buttonEmployee;

    @FXML
    private Button buttonSpace;

    @FXML
    private Button buttonClient;

    @FXML
    private Button buttonContracts;

    @FXML
    private Button buttonFinances;

    @FXML
    private Button buttonOffer;

    @FXML
    private Button buttonReservation;

    @Autowired
    private ApplicationContext springContext;

    private boolean isDarkMode = false;

    @FXML
    protected void handleButton(ActionEvent event) throws Exception {
        Button clickedButton = (Button) event.getSource();

        String fxmlFile = "";

        if (clickedButton == buttonEmployee) {
            fxmlFile = "/com/example/sportcenterv1/employeeManager.fxml";
        } else if (clickedButton == buttonSpace) {
            fxmlFile = "/com/example/sportcenterv1/spaceManager.fxml";
        } else if (clickedButton == buttonClient) {
            fxmlFile = "/com/example/sportcenterv1/clientManager.fxml";
        } else if (clickedButton == buttonContracts) {
            fxmlFile = "/com/example/sportcenterv1/contractManager.fxml";
        } else if (clickedButton == buttonFinances) {
            fxmlFile = "/com/example/sportcenterv1/financeManager.fxml";
        } else if (clickedButton == buttonOffer) {
            fxmlFile = "/com/example/sportcenterv1/offerManager.fxml";
        } else if (clickedButton == buttonReservation) {
            fxmlFile = "/com/example/sportcenterv1/reservationManager.fxml";
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

    @FXML
    private void setDarkMode(){

        String darkMode = getClass().getResource("/css/DMmenuStyle.css").toExternalForm();

        if (isDarkMode){
            mainPane.getStylesheets().remove(darkMode);
            isDarkMode = false;
        }else {
            mainPane.getStylesheets().add(darkMode);
            isDarkMode = true;
        }
    }


}

