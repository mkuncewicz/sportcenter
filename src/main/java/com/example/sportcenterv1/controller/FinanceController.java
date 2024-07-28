package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.dm.DarkModeSingleton;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.service.FinanceService;
import com.example.sportcenterv1.service.SpecializationService;
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
import java.util.List;
import java.util.Locale;

@Component
public class FinanceController {

    @Autowired
    private ApplicationContext springContext;

    private final DarkModeSingleton darkModeSingleton = DarkModeSingleton.getInstance();
    @FXML
    private AnchorPane mainPane;

    private boolean isDarkMode = false;

    @FXML
    private BarChart<String, Number> barChartCostRevenue;

    @Autowired
    private FinanceService financeService;

    @Autowired
    private SpecializationService specializationService;

    @FXML
    private ComboBox<Integer> comboboxMonth;

    @FXML
    private ComboBox<Integer> comboboxYear;

    @FXML
    private BarChart<String, Number> barChartOffer;

    @FXML
    private ComboBox<Specialization> comboboxSpec;

    private final ObservableList<Specialization> specializationObservableList = FXCollections.observableArrayList();
    @FXML
    private Label errorLabel1;

    @FXML
    private void handleBackToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/sportcenterv1/menu.fxml"));
        loader.setControllerFactory(springContext::getBean);

        Parent menuView = loader.load();
        Scene menuScene = new Scene(menuView, 1920, 1080);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(menuScene);
    }

    @FXML
    private void setDarkMode() {
        String darkMode = getClass().getResource("/css/DMfinanceManagerStyle.css").toExternalForm();

        if (isDarkMode) {
            mainPane.getStylesheets().remove(darkMode);
            darkModeSingleton.setDarkMode(false);
            isDarkMode = false;
        } else {
            mainPane.getStylesheets().add(darkMode);
            darkModeSingleton.setDarkMode(true);
            isDarkMode = true;
        }
    }

    @FXML
    private void checkActiveDarkMode() {
        String darkMode = getClass().getResource("/css/DMfinanceManagerStyle.css").toExternalForm();

        if (isDarkMode) {
            mainPane.getStylesheets().add(darkMode);
        } else {
            mainPane.getStylesheets().remove(darkMode);
        }
    }

    @FXML
    private void initialize() {
        isDarkMode = darkModeSingleton.isDarkMode();
        checkActiveDarkMode();

        setupBasicBarChart();
        setComboxDate();

        barChartCostRevenue.setAnimated(false);
        barChartOffer.setAnimated(false);
    }

    private void setComboxDate() {
        setComboboxMonth();
        setComboboxYear();
        setComboboxSpec();
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

    private void setComboboxYear() {
        int currentYear = LocalDate.now().getYear();
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int year = 2024; year <= currentYear; year++) {
            years.add(year);
        }
        comboboxYear.setItems(years);
    }

    private void setComboboxSpec() {
        List<Specialization> specializationList = specializationService.getAllSpecializations();

        specializationList.add(0, null);

        specializationObservableList.setAll(specializationList);
        comboboxSpec.setItems(specializationObservableList);

        listenerComboboxSpec();
    }

    private void listenerComboboxSpec() {
        comboboxSpec.setOnAction(event -> {
            Specialization specialization = comboboxSpec.getValue();
            if (specialization != null && comboboxMonth.getValue() != null && comboboxYear.getValue() != null) {
                setBarChartOfferBySpec(specialization);
            } else if (specialization == null && comboboxMonth.getValue() != null && comboboxYear.getValue() != null) {
                setBarChartOfferByDate();
            }
        });
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
        series1.setName("Przychody");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Koszty");

        if (profit != 0) {
            series1.getData().add(new XYChart.Data<>("Przychody", profit));
        }
        if (cost != 0) {
            series2.getData().add(new XYChart.Data<>("Koszty", cost));
        }

        ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList();
        seriesList.add(series1);
        seriesList.add(series2);

        barChartCostRevenue.getData().clear();
        barChartCostRevenue.getData().addAll(seriesList);
        barChartCostRevenue.layout();

        styleBars(series1, Color.GREEN);
        styleBars(series2, Color.RED);

        // Ustawienia osi X
        xAxis.setTickLabelRotation(-45);  // Rotacja etykiet o 45 stopni
        xAxis.setTickLabelGap(10);        // Odstęp między etykietami a osią

        barChartCostRevenue.setCategoryGap(20); // Ustawienie odstępu między kategoriami
    }

    @FXML
    private void setBarChartByDate() {
        System.out.println("Starting setBarChartByDate");

        // Czyszczenie poprzednich danych wykresu
        barChartCostRevenue.getData().clear();
        barChartCostRevenue.layout();
        System.out.println("Cleared previous data");

        // Tworzenie nowych serii danych
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Przychody");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Koszty");

        // Sprawdzanie, czy wybrano datę
        if (comboboxMonth.getValue() == null || comboboxYear.getValue() == null) {
            errorLabel1.setText("Wybierz poprawnie datę");
            return;
        }

        // Pobieranie wartości z comboboxów
        int month = comboboxMonth.getValue();
        int year = comboboxYear.getValue();

        LocalDate choiceDate = LocalDate.of(year, month, 1);
        String monthName = choiceDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("pl", "PL"));

        // Ustawianie tytułu wykresu
        barChartCostRevenue.setTitle("Przychody i Koszty w Miesiącu: " + monthName + " " + choiceDate.getYear());
        System.out.println("Set chart title");

        // Pobieranie danych finansowych
        double costs = financeService.getCostFromAllContractsByMonth(choiceDate);
        double profit = financeService.getProfitFromAllReservationByMonth(choiceDate);
        System.out.println("Fetched data: costs = " + costs + ", profit = " + profit);

        // Dodawanie danych do serii
        if (profit != 0) {
            series1.getData().add(new XYChart.Data<>("Przychody", profit));
            System.out.println("Added profit data: " + profit);
        }
        if (costs != 0) {
            series2.getData().add(new XYChart.Data<>("Koszty", costs));
            System.out.println("Added costs data: " + costs);
        }

        // Tworzenie listy serii i dodawanie jej do wykresu
        ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList();
        seriesList.add(series1);
        seriesList.add(series2);

        barChartCostRevenue.getData().addAll(seriesList);
        System.out.println("Updated chart data");

        // Stylizacja słupków wykresu
        styleBars(series1, Color.GREEN);
        styleBars(series2, Color.RED);
        System.out.println("Styled bars");

        // Ustawienia osi X
        CategoryAxis xAxis = (CategoryAxis) barChartCostRevenue.getXAxis();
        xAxis.setTickLabelRotation(-45);  // Rotacja etykiet o 45 stopni
        xAxis.setTickLabelGap(10);        // Odstęp między etykietami a osią

        barChartCostRevenue.setCategoryGap(20); // Ustawienie odstępu między kategoriami

        // Aktualizacja układu wykresu
        barChartCostRevenue.layout();
        barChartCostRevenue.requestLayout();
        errorLabel1.setText("");

        // Wywołanie metody setBarChartOfferByDate
        setBarChartOfferByDate();
    }

    @FXML
    private void setBarChartOfferByDate() {
        if (comboboxMonth.getValue() == null || comboboxYear.getValue() == null) {
            errorLabel1.setText("Wybierz poprawnie datę");
            return;
        }

        int month = comboboxMonth.getValue();
        int year = comboboxYear.getValue();

        LocalDate choiceDate = LocalDate.of(year, month, 1);

        List<Object[]> topOffers;
        if (comboboxSpec.getValue() == null) {
            topOffers = financeService.getTopOffersByReservationsInMonth(choiceDate);
        } else {
            Specialization specialization = comboboxSpec.getValue();
            topOffers = financeService.getTopOffersBySpecializationInMonth(choiceDate, specialization);
        }

        barChartOffer.getData().clear();

        XYChart.Series<String, Number> offerSeries = new XYChart.Series<>();
        offerSeries.setName("Liczba rezerwacji");

        for (Object[] offerData : topOffers) {
            String offerName = (String) offerData[0];
            Long reservationCount = (Long) offerData[1];
            offerSeries.getData().add(new XYChart.Data<>(offerName, reservationCount));
        }

        ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList();
        seriesList.add(offerSeries);

        barChartOffer.getData().addAll(seriesList);
        styleBars(offerSeries, Color.BLUE);

        // Ustawienia osi X
        CategoryAxis xAxis = (CategoryAxis) barChartOffer.getXAxis();
        xAxis.setTickLabelRotation(-45);  // Rotacja etykiet o 45 stopni
        xAxis.setTickLabelGap(10);        // Odstęp między etykietami a osią

        barChartOffer.setCategoryGap(20); // Ustawienie odstępu między kategoriami

        barChartOffer.layout();
        barChartOffer.requestLayout();
        errorLabel1.setText("");
    }

    private void setBarChartOfferBySpec(Specialization specialization) {
        if (comboboxMonth.getValue() == null || comboboxYear.getValue() == null) {
            errorLabel1.setText("Wybierz poprawnie datę");
            return;
        }

        LocalDate choiceDate = LocalDate.of(comboboxYear.getValue(), comboboxMonth.getValue(), 1);
        List<Object[]> topOffers = financeService.getTopOffersBySpecializationInMonth(choiceDate, specialization);

        barChartOffer.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Liczba rezerwacji");

        for (Object[] offerData : topOffers) {
            String offerName = (String) offerData[0];
            Long count = (Long) offerData[1];
            series.getData().add(new XYChart.Data<>(offerName, count));
        }

        ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList();
        seriesList.add(series);

        barChartOffer.getData().addAll(seriesList);
        styleBars(series, Color.BLUE);

        // Ustawienia osi X
        CategoryAxis xAxis = (CategoryAxis) barChartOffer.getXAxis();
        xAxis.setTickLabelRotation(-45);  // Rotacja etykiet o 45 stopni
        xAxis.setTickLabelGap(10);        // Odstęp między etykietami a osią

        barChartOffer.setCategoryGap(20); // Ustawienie odstępu między kategoriami

        barChartOffer.layout();
        barChartOffer.requestLayout();
        errorLabel1.setText("");
    }

    private void styleBars(XYChart.Series<String, Number> series, Color color) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            data.getNode().setStyle("-fx-bar-fill: " + toRgbString(color) + ";");

            Text dataText = new Text(data.getYValue().toString());
            dataText.setStyle("-fx-font-size: 12px; -fx-fill: white;");

            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    StackPane stackPane = (StackPane) newNode;
                    stackPane.getChildren().add(dataText);
                    StackPane.setAlignment(dataText, Pos.CENTER);
                }
            });
        }
    }

    private String toRgbString(Color color) {
        return "rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255) + ")";
    }
}
