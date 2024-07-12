package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.dm.DarkModeSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javafx.stage.Stage;

import java.io.IOException;

@Component
public class FinanceController {

    @Autowired
    private ApplicationContext springContext;

    private DarkModeSingleton darkModeSingleton = DarkModeSingleton.getInstance();
    @FXML
    private AnchorPane mainPane;

    private boolean isDarkMode = false;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private void handleBackToMenu(ActionEvent event) throws IOException {
        // Użyj FXMLLoadera, aby załadować widok menu
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/sportcenterv1/menu.fxml"));
        loader.setControllerFactory(springContext::getBean);

        Parent menuView = loader.load();
        Scene menuScene = new Scene(menuView, 1920, 1080);

        // Pobierz aktualną scenę (Stage) i ustaw nową scenę z menu
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(menuScene);
    }

    @FXML
    private void setDarkMode(){
        String darkMode = getClass().getResource("/css/DMemployeeManagerStyle.css").toExternalForm();

        if (isDarkMode){
            mainPane.getStylesheets().remove(darkMode);
            darkModeSingleton.setDarkMode(false);
            isDarkMode = false;
        }else {
            mainPane.getStylesheets().add(darkMode);
            darkModeSingleton.setDarkMode(true);
            isDarkMode = true;
        }
    }

    @FXML
    private void checkActiveDarkMode(){

        String darkMode = getClass().getResource("/css/DMemployeeManagerStyle.css").toExternalForm();

        if (isDarkMode){
            mainPane.getStylesheets().add(darkMode);
        }else {
            mainPane.getStylesheets().remove(darkMode);
        }
    }

    @FXML
    private void initialize() {
        //Darkmode
        isDarkMode = darkModeSingleton.isDarkMode();
        checkActiveDarkMode();

        setupBarChart();
    }

    private void setupBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        barChart.setTitle("Przychody i Koszty w Miesiącu");
        xAxis.setLabel("Kategoria");
        yAxis.setLabel("Kwota");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Przychody");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Koszty");

        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        series2.setName("Test");

        series1.getData().add(new XYChart.Data<>("Przychody", 2000)); // Przykładowe dane
        series2.getData().add(new XYChart.Data<>("Koszty", 3000)); // Przykładowe dane

        barChart.getData().addAll(series1, series2);

        styleBars(series1, Color.GREEN);
        styleBars(series2, Color.RED);
    }

    private void styleBars(XYChart.Series<String, Number> series, Color color) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            data.getNode().setStyle("-fx-bar-fill: " + toRgbString(color) + ";");
            // Wyśrodkowanie słupków względem etykiet
            ((Node) data.getNode()).setStyle("-fx-bar-fill: " + toRgbString(color) + "; -fx-bar-gap: 10; -fx-bar-width: 30;");
        }
    }

    private String toRgbString(Color color) {
        return "rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255) + ")";
    }
}