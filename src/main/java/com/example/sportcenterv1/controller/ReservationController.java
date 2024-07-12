package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.controllerhelp.DateGenerator;
import com.example.sportcenterv1.dm.DarkModeSingleton;
import com.example.sportcenterv1.entity.Client;
import com.example.sportcenterv1.entity.Offer;
import com.example.sportcenterv1.entity.Reservation;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.OfferType;
import com.example.sportcenterv1.entity.enums.ReservationStatus;
import com.example.sportcenterv1.entity.space.Space;
import com.example.sportcenterv1.service.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

@Component
public class ReservationController {

    @Autowired
    private ApplicationContext springContext;

    private DarkModeSingleton darkModeSingleton = DarkModeSingleton.getInstance();

    @FXML
    private AnchorPane mainPane;

    private boolean isDarkMode = false;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<Integer> comboboxhour;

    @FXML
    private ComboBox<Integer> comboboxminute;

    @FXML
    private TextField textFieldCSearchClient;

    @FXML
    private TextField textFieldSearchOffer;

    @FXML
    private ListView<Reservation> listViewReservation;

    @FXML
    private ListView<Client> listViewClient;

    @FXML
    private ListView<Offer> listViewOffer;

    @FXML
    private ComboBox<ReservationStatus> comboboxStatusReservation;

    @FXML
    private ComboBox<Employee> comboboxEmployee;

    @FXML
    private ComboBox<Space> comboboxSpace;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SpaceService spaceService;

    private ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList();

    private ObservableList<Client> clientObservableList = FXCollections.observableArrayList();

    private ObservableList<Offer> offerObservableList = FXCollections.observableArrayList();

    private ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();

    private ObservableList<Space> spaceObservableList = FXCollections.observableArrayList();

    private ObservableList<ReservationStatus> reservationStatusObservableList = FXCollections.observableArrayList();

    private Client curClient  = null;

    private final ObjectProperty<Offer> curOffer = new SimpleObjectProperty<>(null);

    private DateGenerator dateGenerator;

    @FXML
    private Label errorLabel;

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
    private void checkActiveDarkMode(){

        String darkMode = getClass().getResource("/css/DMreservationManager.css").toExternalForm();

        if (isDarkMode){
            mainPane.getStylesheets().add(darkMode);
        }else {
            mainPane.getStylesheets().remove(darkMode);
        }
    }
    @FXML
    private void setDarkMode(){
        String darkMode = getClass().getResource("/css/DMreservationManager.css").toExternalForm();

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
    protected void initialize(){
        //DarkMode
        isDarkMode = darkModeSingleton.isDarkMode();
        checkActiveDarkMode();

        //Domyslne ustawienia dla elementow
        setObservableLists();
        setAllListView();
        setCombobox();
        setTime();


        checkEnableCombobox();

        curOfferListener();


        curClient = null;
        curOffer.set(null);

        dateGenerator = new DateGenerator(datePicker,comboboxhour,comboboxminute);
    }

    private void setObservableLists(){

        List<Reservation> reservationList = reservationService.getAllReservation();
        List<Client> clientList = clientService.getAllClients();
        List<Offer> offerList = offerService.getAllOffers();

        List<ReservationStatus> reservationStatusList = Arrays.asList(null, ReservationStatus.PENDING, ReservationStatus.PAID);


        reservationObservableList.setAll(reservationList);
        clientObservableList.setAll(clientList);
        offerObservableList.setAll(offerList);

        reservationStatusObservableList.setAll(reservationStatusList);

    }

    private void setAllListView(){

        settingReservationListView();
        settingClientListView();
        settingOfferListView();

        listenerToClientListView();
        listenerToOfferListView();

        listenerToSearchClient();
        listenerToSearchOffer();
    }

    private void settingReservationListView(){

        listViewReservation.setCellFactory(reservation -> new ListCell<Reservation>(){

            @Override
            protected void updateItem(Reservation reservation, boolean b) {
                super.updateItem(reservation, b);

                if (reservation == null || b){
                    setText(null);
                }else {
                    String clientName = reservation.getClient().getFirstName() + " " + reservation.getClient().getLastName();
                    setText(clientName + ", " + reservation.getOffer().getName() + ", " + reservation.getDate());
                }
            }
        });
        listViewReservation.setItems(reservationObservableList);
    }

    private void settingClientListView(){

        listViewClient.setCellFactory(client -> new ListCell<Client>(){

            @Override
            protected void updateItem(Client client, boolean b) {
                super.updateItem(client, b);

                if (client == null || b){
                    setText(null);
                }else {
                    setText(client.getFirstName() + " " + client.getLastName() + ", " + client.getPhoneNumber());
                }
            }
        });
        listViewClient.setItems(clientObservableList);
    }

    private void settingOfferListView(){

        listViewOffer.setCellFactory(offer -> new ListCell<Offer>(){

            @Override
            protected void updateItem(Offer offer, boolean b) {
                super.updateItem(offer, b);
                if (offer == null || b){
                    setText(null);
                }else {
                    setText(offer.getName() + ", " + offer.getPrice());
                }
            }
        });
        listViewOffer.setItems(offerObservableList);
    }

    private void listenerToClientListView(){

        listViewClient.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(ObservableValue<? extends Client> observableValue, Client client, Client t1) {

                curClient = t1;
            }
        });
    }

    private void listenerToOfferListView() {
        listViewOffer.getSelectionModel().selectedItemProperty().addListener((observable, oldOffer, newOffer) -> {
            if (newOffer != null) {
                curOffer.set(newOffer);

                List<Specialization> specializationList = curOffer.get().getSpecializations().stream().toList();

                Set<Employee> employeeSet =  employeeService.getAllEmployeesBySpecialization(specializationList);
                Set<Space> spaceSet = spaceService.getAllSpaceBySpecialization(specializationList);

                employeeObservableList.setAll(employeeSet.stream().toList());
                spaceObservableList.setAll(spaceSet.stream().toList());

                checkEnableCombobox();
            }
        });
    }
    private void listenerToSearchClient(){

        textFieldCSearchClient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchName = "";
                searchName = textFieldCSearchClient.getText();

                List<Client> clientList = clientService.getAllClientsByName(searchName);
                clientObservableList.setAll(clientList);
            }
        });
    }

    private void listenerToSearchOffer(){

        textFieldSearchOffer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchName = "";
                searchName = textFieldSearchOffer.getText();

               List<Offer> offerList = offerService.getAllOffersByName(searchName);
               offerObservableList.setAll(offerList);
            }
        });
    }

    private void setCombobox(){

        settingComboboxStatusReservation();
        settingComboboxEmployee();
        settingComboboxSpace();
    }

    private void settingComboboxStatusReservation() {
        comboboxStatusReservation.setItems(reservationStatusObservableList);

        // Customizacja wyświetlania elementów, aby pokazać "Nie wybrano" dla null
        comboboxStatusReservation.setCellFactory(param -> new ListCell<ReservationStatus>() {
            @Override
            protected void updateItem(ReservationStatus item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(" ");
                } else {
                    setText(item.getDisplayName());
                }
            }
        });

        comboboxStatusReservation.setButtonCell(new ListCell<ReservationStatus>() {
            @Override
            protected void updateItem(ReservationStatus item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Wybierz status");
                } else {
                    setText(item.getDisplayName());
                }
            }
        });
    }

    private void settingComboboxEmployee(){
        comboboxEmployee.setItems(employeeObservableList);

        comboboxEmployee.setCellFactory(param -> new ListCell<Employee>(){

            @Override
            protected void updateItem(Employee employee, boolean b) {
                super.updateItem(employee, b);
                if (employee == null || b){
                    setText(null);
                }else {
                    setText(employee.getFirstName() + " " + employee.getLastName());
                }
            }
        });
    }

    private void settingComboboxSpace(){
        comboboxSpace.setItems(spaceObservableList);

        comboboxSpace.setCellFactory(param -> new ListCell<Space>(){

            @Override
            protected void updateItem(Space space, boolean b) {
                super.updateItem(space, b);
                if (space == null || b){
                    setText(null);
                }else{
                    setText(space.getName());
                }
            }
        });
    }

    private void checkEnableCombobox(){
        Offer offerSelect = curOffer.get();

        if (offerSelect == null){
            comboboxEmployee.setDisable(true);
            comboboxSpace.setDisable(true);

            comboboxhour.setDisable(true);
            comboboxminute.setDisable(true);
        }else {
            if (offerSelect.getOfferType() == OfferType.ONE_TIME) {
                comboboxEmployee.setDisable(false);
                comboboxSpace.setDisable(false);

                comboboxhour.setDisable(false);
                comboboxminute.setDisable(false);
            }else {
                comboboxEmployee.setDisable(true);
                comboboxSpace.setDisable(true);

                comboboxhour.setDisable(true);
                comboboxminute.setDisable(true);
            }
        }
    }

    private void curOfferListener(){
        curOffer.addListener((observable, oldValue, newValue) -> {

            checkEnableCombobox();
        });
    }
    @FXML
    private void clearDate(){

        datePicker.setValue(null);
        comboboxhour.setValue(null);
        comboboxminute.setValue(null);
    }

    private void setTime(){

        comboboxhour.getItems().addAll(IntStream.rangeClosed(6, 21).boxed().toArray(Integer[]::new));
        comboboxminute.getItems().addAll(IntStream.iterate(0, n -> n + 5).limit(12).boxed().toArray(Integer[]::new));

        comboboxhour.setPromptText("Hour");
        comboboxminute.setPromptText("Minute");



        comboboxminute.setCellFactory(comboBox -> new javafx.scene.control.ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(String.format("%02d", item));
                }
            }
        });


        comboboxminute.setButtonCell(new javafx.scene.control.ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(String.format("%02d", item));
                }
            }
        });
    }

    private void reload(){

        curClient = null;
        curOffer.set(null);

        List<Reservation> reservationList = reservationService.getAllReservation();
        reservationObservableList.setAll(reservationList);
        checkEnableCombobox();
    }

    @FXML
    private void createReservation(){

        Reservation createReservation = new Reservation();

        LocalDateTime localDateTime = dateGenerator.getLocalDateTime();
        if (localDateTime == null){
            errorLabel.setText("Wybierz poprawnie date");
            return;
        }

        ReservationStatus reservationStatus = comboboxStatusReservation.getValue();

        if (reservationStatus == null){
            errorLabel.setText("Wybierz status rezerwacji");
            return;
        }

        Long clientId = curClient.getId();
        Optional<Client> optionalClient = clientService.getClient(clientId);
        Client client;
        if (optionalClient.isEmpty()){
            errorLabel.setText("Wybierz klienta");
            return;
        }else {
            client = optionalClient.get();
        }

        Long offerId = curOffer.get().getId();
        Optional<Offer> optionalOffer = offerService.getOffer(offerId);
        Offer offer;
        if (optionalOffer.isEmpty()){
            errorLabel.setText("Wybierz oferte/usługe");
            return;
        }else {
            offer = optionalOffer.get();
        }

        if (comboboxEmployee.getSelectionModel().getSelectedItem() != null){
            createReservation.setEmployee(comboboxEmployee.getSelectionModel().getSelectedItem());
        }else {
            errorLabel.setText("Wybierz pracownika");
            return;
        }

        if (comboboxSpace.getSelectionModel().getSelectedItem() != null){
            createReservation.setSpace(comboboxSpace.getSelectionModel().getSelectedItem());
        }else {
            errorLabel.setText("Wybierz sale/hale");
            return;
        }

        createReservation.setReservationStatus(reservationStatus);
        createReservation.setDate(localDateTime);
        createReservation.setClient(client);
        createReservation.setOffer(offer);

        reservationService.createReservation(createReservation);

        errorLabel.setText("");
        reload();
    }

    @FXML
    private void updateReservation() {
        Reservation selectedReservation = listViewReservation.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) {
            errorLabel.setText("Wybierz rezerwację do aktualizacji");
            return;
        }

        // Pobierz wartości, które użytkownik wprowadził (jeśli w ogóle)
        LocalDateTime localDateTime = dateGenerator.getLocalDateTime();
        ReservationStatus reservationStatus = comboboxStatusReservation.getValue();
        Client selectedClient = curClient;
        Offer selectedOffer = curOffer.get();
        Employee selectedEmployee = comboboxEmployee.getSelectionModel().getSelectedItem();
        Space selectedSpace = comboboxSpace.getSelectionModel().getSelectedItem();


        // Aktualizuj tylko te pola, które zostały zmienione przez użytkownika
        if (localDateTime != null) {
            selectedReservation.setDate(localDateTime);
        }

        if (reservationStatus != null) {
            selectedReservation.setReservationStatus(reservationStatus);
        }

        if (selectedClient != null) {
            selectedReservation.setClient(selectedClient);
        }

        if (selectedOffer != null) {
            selectedReservation.setOffer(selectedOffer);
        }

        if (selectedEmployee != null) {
            selectedReservation.setEmployee(selectedEmployee);
        }

        if (selectedSpace != null) {
            selectedReservation.setSpace(selectedSpace);
        }

        reservationService.updateReservation(selectedReservation.getId(), selectedReservation);
        errorLabel.setText("");
        reload();
    }

    @FXML
    private void deleteReservation() {
        Reservation selectedReservation = listViewReservation.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) {
            errorLabel.setText("Wybierz rezerwację do usunięcia");
            return;
        }

        reservationService.deleteReservationById(selectedReservation.getId());
        errorLabel.setText("");
        reload();
    }
}
