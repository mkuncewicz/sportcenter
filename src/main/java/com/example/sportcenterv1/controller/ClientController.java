package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.entity.Address;
import com.example.sportcenterv1.entity.Client;
import com.example.sportcenterv1.service.ClientService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class ClientController {

    @Autowired
    private ApplicationContext springContext;

    @Autowired
    private ClientService clientService;

    @FXML
    private ListView<Client> listViewClient;

    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldLastName;

    @FXML
    private TextField textFieldPhone;

    @FXML
    private DatePicker datePickerBirth;

    @FXML
    private TextField textFieldCity;

    @FXML
    private TextField textFieldStreet;

    @FXML
    private TextField textFieldBuildingNumber;

    @FXML
    private TextField textFieldApartmentNumber;

    private ObservableList<Client> clientObservableList = FXCollections.observableArrayList();


    @FXML
    private Label labelFirstName;

    @FXML
    private Label labelLastName;

    @FXML
    private Label labelPhone;

    @FXML
    private Label labelDateBirth;

    @FXML
    private Label labelCity;

    @FXML
    private Label labelStreet;

    @FXML
    private Label labelBuildingNumber;

    @FXML
    private Label labelApartmentNumber;

    @FXML
    private void handleBackToMenu(ActionEvent event) throws IOException {
        // Użyj FXMLLoadera, aby załadować widok menu
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/sportcenterv1/menu.fxml"));
        loader.setControllerFactory(springContext::getBean); // Upewnij się, że kontekst Springa jest dostępny

        Parent menuView = loader.load();
        Scene menuScene = new Scene(menuView, 1920, 1080);

        // Pobierz aktualną scenę (Stage) i ustaw nową scenę z menu
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(menuScene);
    }

    @FXML
    protected void initialize(){
        updateList();
        settingListViewClients();
        listViewClient.setItems(clientObservableList);
        listenToListViewClient();
    }

    private void settingListViewClients(){
        listViewClient.setCellFactory(client -> new ListCell<Client>(){

            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null){
                    setText(null);
                }else {
                    setText(item.getFirstName() + " " + item.getLastName());
                }
            }
        });
    }

    private void updateList(){
        List<Client> allClients = clientService.getAllClients();
        clientObservableList.setAll(allClients);
    }

    private void listenToListViewClient(){
        listViewClient.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(ObservableValue<? extends Client> observable, Client oldValue, Client newValue) {
                if (newValue != null) {
                    setLabels(newValue);
                } else {
                    clearLabels();
                }
            }
        });
    }

    @FXML
    private void addNewClient() {
        Client createClient = new Client();
        Address createAddress = new Address();

        // Odczyt danych z formularza
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String phone = textFieldPhone.getText();
        LocalDate localDate = datePickerBirth.getValue();
        Date birth = localDate != null ? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

        String city = textFieldCity.getText();
        String street = textFieldStreet.getText();
        String buildingNumber = textFieldBuildingNumber.getText();
        String apartmentNumber = textFieldApartmentNumber.getText();

        if (firstName.isBlank() || lastName.isBlank() || phone.isBlank() || birth == null) {
            System.out.println("Wszystkie pola muszą być wypełnione.");
            return;  // Przerwij wykonanie, jeśli jakiekolwiek pole jest puste
        }

        createClient.setFirstName(firstName);
        createClient.setLastName(lastName);
        createClient.setPhoneNumber(phone);
        createClient.setDateOfBirth(birth);

        if (!city.isBlank()) createAddress.setCity(city);
        if (!street.isBlank()) createAddress.setStreet(street);
        if (!buildingNumber.isBlank()) createAddress.setBuildingNumber(buildingNumber);
        if (!apartmentNumber.isBlank()) createAddress.setApartmentNumber(apartmentNumber);

        createClient.setAddress(createAddress);

        System.out.println(createClient);
        try {
            clientService.createClient(createClient);
        } catch (Exception e) {
            System.out.println("Błąd podczas zapisu klienta: " + e.getMessage());
        }
        updateList();
        cleanTextFields();
    }

    @FXML
    private void updateClient(){
        Client client = listViewClient.getSelectionModel().getSelectedItem();

        if (client == null) return;

        Long clientID = client.getId();

        Client createClient = new Client();
        Address createAddress = new Address();

        String firstName = "";
        String lastName = "";
        String phone = "";

        String city = "";
        String street = "";
        String buildingNumber = "";
        String apartmentNumber = "";

        // Odczyt danych z formularza
        firstName = textFieldFirstName.getText();
        lastName = textFieldLastName.getText();
        phone = textFieldPhone.getText();
        LocalDate localDate = datePickerBirth.getValue();
        Date birth = localDate != null ? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

        city = textFieldCity.getText();
        street = textFieldStreet.getText();
        buildingNumber = textFieldBuildingNumber.getText();
        apartmentNumber = textFieldApartmentNumber.getText();

        if (!firstName.isBlank()) createClient.setFirstName(firstName);
        if (!lastName.isBlank()) createClient.setLastName(lastName);
        if (!phone.isBlank()) createClient.setPhoneNumber(phone);
        if (birth != null) createClient.setDateOfBirth(birth);

        if (!city.isBlank()) createAddress.setCity(city);
        if (!street.isBlank()) createAddress.setStreet(street);
        if (!buildingNumber.isBlank()) createAddress.setBuildingNumber(buildingNumber);
        if (!apartmentNumber.isBlank()) createAddress.setApartmentNumber(apartmentNumber);

        if (city.isBlank() && street.isBlank() && buildingNumber.isBlank() && apartmentNumber.isBlank()) {

        }else createClient.setAddress(createAddress);

        clientService.updateClient(clientID,createClient);

        updateList();
        cleanTextFields();
    }

    @FXML
    private void deleteClient(){

        Client selectedItem = listViewClient.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Long clientID = selectedItem.getId();
            clientService.deleteClient(clientID);
            updateList();
        }else return;
    }

    private void cleanTextFields(){

        textFieldFirstName.clear();
        textFieldLastName.clear();
        textFieldPhone.clear();

        textFieldCity.clear();
        textFieldStreet.clear();
        textFieldBuildingNumber.clear();
        textFieldApartmentNumber.clear();
    }

    private void setLabels(Client client){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = formatter.format(client.getDateOfBirth());


        labelFirstName.setText("Imie: " + client.getFirstName());
        labelLastName.setText("Nazwisko: " + client.getLastName());
        labelPhone.setText("Telefon: " + client.getPhoneNumber());
        labelDateBirth.setText("Data urodzenia: " + formattedDate);
        labelCity.setText("Miasto: " + client.getAddress().getCity());
        labelStreet.setText("Ulica: " + client.getAddress().getStreet());
        labelBuildingNumber.setText("Numer budynku: " + client.getAddress().getBuildingNumber());
        labelApartmentNumber.setText("Numer mieszkania: " + client.getAddress().getApartmentNumber());
    }

    private void clearLabels(){

        labelFirstName.setText("Imie: ");
        labelLastName.setText("Nazwisko: ");
        labelPhone.setText("Telefon: ");
        labelDateBirth.setText("Data urodzenia: ");
        labelCity.setText("Miasto: ");
        labelStreet.setText("Ulica: ");
        labelBuildingNumber.setText("Numer budynku: ");
        labelApartmentNumber.setText("Numer mieszkania: ");
    }
}
