package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.dm.DarkModeSingleton;
import com.example.sportcenterv1.entity.Offer;
import com.example.sportcenterv1.service.FinanceService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@Component
public class FinanceController {

    @Autowired
    private ApplicationContext springContext;

    private DarkModeSingleton darkModeSingleton = DarkModeSingleton.getInstance();
    @FXML
    private AnchorPane mainPane;

    private boolean isDarkMode = false;

    @FXML
    private BarChart<String, Number> barChartCostRevenue;

    @Autowired
    private FinanceService financeService;

    @FXML
    private ComboBox<Integer> comboboxMonth;

    @FXML
    private ComboBox<Integer> comboboxYear;


    @FXML
    private BarChart<String, Number> barChartOffer;

    @FXML
    private ComboBox<Offer> comboboxSpec;



    @FXML
    private Label errorLabel1;

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
        // Dark mode
        isDarkMode = darkModeSingleton.isDarkMode();
        checkActiveDarkMode();

        // Load custom CSS for bar chart
        barChartCostRevenue.getStylesheets().add(getClass().getResource("/css/chart-styles.css").toExternalForm());

        setupBasicBarChart();

        // Combobox ustawienia
        setComboxDate();
    }


    private void setComboxDate(){
        setComboboxMonth();
        setComboboxYear();
    }

    private void setComboboxMonth() {
        ObservableList<Integer> months = FXCollections.observableArrayList();
        for (int month = 1; month <= 12; month++) {
            months.add(month);
        }

        comboboxMonth.setItems(months);
        comboboxMonth.setConverter(new StringConverter<>() {
            @Override
            public String toString(Integer month) {
                if (month == null) return null;
                return Month.of(month).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pl-PL"));
            }

            @Override
            public Integer fromString(String string) {
                return null;
            }
        });
    }

    private void setComboboxYear(){
        int currentYear = LocalDate.now().getYear();
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int year = 2024; year <= currentYear; year++) {
            years.add(year);
        }
        comboboxYear.setItems(years);
    }

    private void setComboboxOffer(){


    }

    private void setupBasicBarChart() {

        LocalDate localDate = LocalDate.now();
        String monthName = localDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("pl", "PL"));

        double cost = financeService.getCostFromAllContractsByMonth(localDate);
        double profit = financeService.getProfitFromAllReservationByMonth(localDate);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        barChartCostRevenue.setTitle("Przychody i Koszty w Miesiącu: " + monthName + " " + localDate.getYear());
        xAxis.setLabel("Kategoria");
        yAxis.setLabel("Kwota");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Przychody: " + profit);
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Koszty: " + cost);


        if (profit != 0) {
            series1.getData().add(new XYChart.Data<>("Przychody", profit));
        }
        if (cost != 0) {
            series2.getData().add(new XYChart.Data<>("Koszty", cost));
        }

        barChartCostRevenue.getData().addAll(series1, series2);

        styleBars(series1, Color.GREEN);
        styleBars(series2, Color.RED);
    }

    @FXML
    private void setBarChartByDate(){
        System.out.println("Starting setBarChartByDate");

        // Clear the chart
        barChartCostRevenue.getData().clear();
        barChartCostRevenue.layout();
        System.out.println("Cleared previous data");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Przychody");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Koszty");

        if (comboboxMonth.getValue() == null || comboboxYear.getValue() == null){
            errorLabel1.setText("Wybierz poprawnie date");
            return;
        }
        int month = comboboxMonth.getValue();
        int year = comboboxYear.getValue();

        LocalDate choiceDate = LocalDate.of(year, month, 1);
        String monthName = choiceDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("pl", "PL"));

        barChartCostRevenue.setTitle("Przychody i Koszty w Miesiącu: " + monthName + " " + choiceDate.getYear());
        System.out.println("Set chart title");

        double costs = financeService.getCostFromAllContractsByMonth(choiceDate);
        double profit = financeService.getProfitFromAllReservationByMonth(choiceDate);
        System.out.println("Fetched data: costs = " + costs + ", profit = " + profit);

        if (profit != 0) {
            series1.getData().add(new XYChart.Data<>("Przychody", profit));
            System.out.println("Added profit data: " + profit);
        }
        if (costs != 0) {
            series2.getData().add(new XYChart.Data<>("Koszty", costs));
            System.out.println("Added costs data: " + costs);
        }

        barChartCostRevenue.getData().addAll(series1, series2);
        System.out.println("Updated chart data");

        styleBars(series1, Color.GREEN);
        styleBars(series2, Color.RED);
        System.out.println("Styled bars");

        barChartCostRevenue.layout();
        errorLabel1.setText("");
    }

    private void styleBars(XYChart.Series<String, Number> series, Color color) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            // Ustawienie koloru słupka
            data.getNode().setStyle("-fx-bar-fill: " + toRgbString(color) + ";");

            // Dodanie etykiety do słupka
            Text dataText = new Text(data.getYValue().toString());
            dataText.setStyle("-fx-font-size: 12px; -fx-fill: white;");

            // Pozycjonowanie etykiety na słupku
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    StackPane stackPane = (StackPane) newNode;
                    stackPane.getChildren().add(dataText);

                    // Ustawienie pozycji etykiety na środku słupka
                    StackPane.setAlignment(dataText, Pos.CENTER);
                }
            });
        }
    }

    private String toRgbString(Color color) {
        return "rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255) + ")";
    }

}