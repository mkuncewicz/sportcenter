package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.controllerhelp.DateGenerator;
import com.example.sportcenterv1.controllerhelp.ReservationComponent;
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
import java.time.format.DateTimeFormatter;
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
    private TextField textFieldSearchReservation;

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

    @FXML
    private ComboBox<Specialization> comboboxSpecForOffer;

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

    @Autowired
    private SpecializationService specializationService;

    private ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList();

    private ObservableList<Client> clientObservableList = FXCollections.observableArrayList();

    private ObservableList<Offer> offerObservableList = FXCollections.observableArrayList();

    private ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();

    private ObservableList<Space> spaceObservableList = FXCollections.observableArrayList();

    private ObservableList<ReservationStatus> reservationStatusObservableList = FXCollections.observableArrayList();

    private ObservableList<Specialization> specializationObservableList = FXCollections.observableArrayList();

    private final ObjectProperty<Reservation> curReservation = new SimpleObjectProperty<>(null);

    private Client curClient  = null;

    private final ObjectProperty<Offer> curOffer = new SimpleObjectProperty<>(null);

    private DateGenerator dateGenerator;

    @FXML
    private Label errorLabel;

    @FXML
    private Label labelClient;

    @FXML
    private Label labelOffer;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelEmployee;

    @FXML
    private Label labelSpace;

    @FXML
    private Label labelStatus;

    private ReservationComponent reservationComponent;

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

        //Komponent do obslugi tworzenia rezerwacji
        dateGenerator = new DateGenerator(datePicker,comboboxhour,comboboxminute);

        reservationComponent = new ReservationComponent(reservationService,errorLabel,clientService,offerService,dateGenerator,comboboxStatusReservation,
                comboboxEmployee,comboboxSpace);

        //Domyslne ustawienia dla elementow
        setObservableLists();
        setAllListView();
        setCombobox();
        setTime();
        setLabelsForReservation();

        checkEnableCombobox();

        curOfferListener();

        //Ustawienie listy dla specjalizacji
        List<Specialization> specializationList = specializationService.getAllSpecializations();
        specializationList.add(0,null);
        specializationObservableList.setAll(specializationList);

        curReservation.set(null);
        curClient = null;
        curOffer.set(null);

    }

    private void setObservableLists(){

        List<Reservation> reservationList = reservationService.getAllReservation();
        List<Client> clientList = clientService.getAllClients();
        List<Offer> offerList = offerService.getAllOffers();

        List<ReservationStatus> reservationStatusList = Arrays.asList(null, ReservationStatus.PENDING, ReservationStatus.PAID);

        //Sortowanie wedlug daty
        reservationList = reservationList.stream()
                .sorted((r1, r2) -> r2.getDate().compareTo(r1.getDate()))
                .toList();

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
        listenerListViewReservation();

        listenerToSearchClient();
        listenerToSearchOffer();
        listenerToSearchReservation();
        listenerToComboboxSpecForOffer();
    }

    private void settingReservationListView(){

        listViewReservation.setCellFactory(reservation -> new ListCell<Reservation>(){

            @Override
            protected void updateItem(Reservation reservation, boolean b) {
                super.updateItem(reservation, b);

                if (reservation == null || b){
                    setText(null);
                }else {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm");

                    String clientName = reservation.getClient().getFirstName() + " " + reservation.getClient().getLastName();
                    setText(clientName + ", " + reservation.getOffer().getName() + ", " + dateTimeFormatter.format(reservation.getDate()));
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

    private void listenerToSearchReservation(){

        textFieldSearchReservation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchName = "";
                searchName = textFieldSearchReservation.getText();

                List<Reservation> reservationList = reservationService.getAllReservationByName(searchName);

                reservationList = reservationList.stream()
                        .sorted((r1, r2) -> r2.getDate().compareTo(r1.getDate()))
                        .toList();

                reservationObservableList.setAll(reservationList);
            }
        });
    }

    private void listenerListViewReservation(){

        listViewReservation.getSelectionModel().selectedItemProperty().addListener((observableValue, reservation, t1) -> {

            curReservation.set(t1);
            setLabelsForReservation();
        });
    }

    private void listenerToComboboxSpecForOffer() {
        comboboxSpecForOffer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Specialization specialization = comboboxSpecForOffer.getValue();
                List<Offer> offerList;

                if (specialization == null || specialization.getId() == null) {
                    offerList = offerService.getAllOffers();
                } else {
                    offerList = offerService.getAllOffersBySpecialization(specialization);
                }

                offerObservableList.setAll(offerList);
            }
        });
    }

    private void setCombobox(){

        settingComboboxStatusReservation();
        settingComboboxEmployee();
        settingComboboxSpace();
        setComboboxSpecForOffer();
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

        comboboxEmployee.setButtonCell(new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (employee == null || empty) {
                    setText(null);
                } else {
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

        comboboxSpace.setButtonCell(new ListCell<Space>(){
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

    private void setComboboxSpecForOffer(){

        comboboxSpecForOffer.setItems(specializationObservableList);
        comboboxSpecForOffer.setCellFactory(param -> new ListCell<Specialization>(){

            @Override
            protected void updateItem(Specialization specialization, boolean b) {
                super.updateItem(specialization, b);
                if (specialization == null || b){
                    setText("Wszystkie");
                }else {
                    setText(specialization.getName());
                }
            }
        });

        comboboxSpecForOffer.setButtonCell(new ListCell<Specialization>(){

            @Override
            protected void updateItem(Specialization specialization, boolean b) {
                super.updateItem(specialization, b);
                if(specialization == null || b){
                    setText("Wszystkie");
                }else {
                    setText(specialization.getName());
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

        reservationList = reservationList.stream()
                .sorted((r1, r2) -> r2.getDate().compareTo(r1.getDate()))
                .toList();

        reservationObservableList.setAll(reservationList);
        checkEnableCombobox();
    }

    private void setLabelsForReservation() {
        boolean isSelected = curReservation.get() != null;

        if (isSelected) {
            Reservation reservation = curReservation.get();
            OfferType offerType = reservation.getOffer().getOfferType();

            // Formatowanie daty
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm");

            labelClient.setText("Klient: " + reservation.getClient().getFirstName() + " " + reservation.getClient().getLastName());
            labelOffer.setText("Oferta: " + reservation.getOffer().getName());
            labelDate.setText("Data: " + reservation.getDate().format(formatter));

            if (offerType == OfferType.ONE_TIME) {
                labelEmployee.setText("Pracownik: " + reservation.getEmployee().getFirstName() + " " + reservation.getEmployee().getLastName());
                labelSpace.setText("Miejsce: " + reservation.getSpace().getName());
            } else {
                labelEmployee.setText("Pracownik: ");
                labelSpace.setText("Miejsce: ");
            }
            labelStatus.setText("Status: " + reservation.getReservationStatus());
        } else {
            labelClient.setText("Klient: ");
            labelOffer.setText("Oferta: ");
            labelDate.setText("Data: ");
            labelEmployee.setText("Pracownik: ");
            labelSpace.setText("Miejsce: ");
            labelStatus.setText("Status: ");
        }
    }

    @FXML
    private void createReservation(){

        Client client = curClient;
        Offer offer = curOffer.get();
        OfferType offerType = offer.getOfferType();

        boolean isCreated = reservationComponent.createReservation(offerType,client,offer);

       if (isCreated) {
           errorLabel.setText("");
           reload();
       }
    }

    @FXML
    private void updateReservation() {
        Reservation selectedReservation = listViewReservation.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) {
            errorLabel.setText("Wybierz rezerwację do aktualizacji");
            return;
        }


        Client client = curClient;
        Offer offer = curOffer.get();

       boolean isUpdated = reservationComponent.updateReservation(selectedReservation,client,offer);

        if (isUpdated) {
            errorLabel.setText("");
            reload();
        }
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
