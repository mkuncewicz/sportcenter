package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.dm.DarkModeSingleton;
import com.example.sportcenterv1.entity.Offer;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.OfferType;
import com.example.sportcenterv1.service.OfferService;
import com.example.sportcenterv1.service.SpecializationService;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class OfferController {

    @Autowired
    private ApplicationContext springContext;
    private DarkModeSingleton darkModeSingleton = DarkModeSingleton.getInstance();

    @FXML
    private AnchorPane mainPane;

    private boolean isDarkMode = false;


    @Autowired
    private OfferService offerService;
    @FXML
    private ListView<Offer> listViewOffer;

    private ObservableList<Offer> offerObservableList = FXCollections.observableArrayList();

    @Autowired
    private SpecializationService specializationService;
    @FXML
    private ListView<Specialization> listViewSpecOfOffer;

    private ObservableList<Specialization> specializationOfOfferObservableList = FXCollections.observableArrayList();
    @FXML
    private ListView<Specialization> listViewSpecialization;

    private ObservableList<Specialization> specializationObservableList = FXCollections.observableArrayList();

    private Offer curOffer = null;

    @FXML
    private TextField textfieldsearch;

    @FXML
    private ComboBox<Specialization> comboboxSpec;

    @FXML
    private ObservableList<Specialization> specializationToSearchObservableList = FXCollections.observableArrayList();

    @FXML
    private Label nameLabel;

    @FXML
    private TextFlow descriptionTextFlow;

    @FXML
    private Label offerTypeLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField priceTextField;

    @FXML
    private Label errorLabel1;

    @FXML
    private Label errorLabel2;

    @FXML
    private ComboBox<OfferType> comboboxOfferType;

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

        String darkMode = getClass().getResource("/css/DMofferManagerStyle.css").toExternalForm();

        if (isDarkMode){
            mainPane.getStylesheets().add(darkMode);
        }else {
            mainPane.getStylesheets().remove(darkMode);
        }
    }
    @FXML
    private void setDarkMode(){
        String darkMode = getClass().getResource("/css/DMofferManagerStyle.css").toExternalForm();

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

        comboboxSpec.setItems(specializationToSearchObservableList);

        updateLists();
        setAllListView();
        setCurOfferListener();
        textFormatter();
        setComboboxOfferType();
        listenerToTextFieldSearch();

        setComboboxSpecToSearch();
        listenerToComboboxSpecSearch();
    }

    private void setComboboxOfferType(){

        // Inicjalizacja ComboBox z wartościami enum OfferType oraz null
        List<OfferType> offerTypes = new ArrayList<>(Arrays.asList(OfferType.values()));
        offerTypes.add(null); // Dodanie null jako opcja
        comboboxOfferType.setItems(FXCollections.observableArrayList(offerTypes));
        comboboxOfferType.setConverter(new StringConverter<OfferType>() {
            @Override
            public String toString(OfferType offerType) {
                return offerType == null ? "Typ usługi" : offerType.toString();
            }

            @Override
            public OfferType fromString(String s) {
                return OfferType.valueOf(s);
            }
        });
    }

    private void textFormatter(){
        descriptionTextArea.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() > 510) {
                return null; //
            } else {
                return change;
            }
        }));
    }

    private void updateLists(){

        List<Offer> offerList = offerService.getAllOffers();
        List<Specialization> specializationList = specializationService.getAllSpecializations();

        offerObservableList.setAll(offerList);
        specializationObservableList.setAll(specializationList);

        setComboboxSpecToSearch();
    }

    private void setAllListView(){
        settingListViewOffer();
        settingListViewSpecialization();

        listViewSpecOfOffer.setItems(specializationOfOfferObservableList);
    }

    private void settingListViewOffer(){

        listViewOffer.setCellFactory(offer -> new ListCell<Offer>(){

            @Override
            protected void updateItem(Offer offer, boolean b) {
                super.updateItem(offer, b);
                if (offer == null || b){
                    setText(null);
                }else {
                    setText(offer.getName()+ ", " + offer.getPrice() + "zł");
                }
            }
        });
        listViewOffer.setItems(offerObservableList);
    }

    private void settingListViewSpecialization(){

        listViewSpecialization.setCellFactory(specialization -> new ListCell<Specialization>(){

            @Override
            protected void updateItem(Specialization specialization, boolean b) {
                super.updateItem(specialization, b);
                if (specialization == null || b){
                    setText(null);
                }else {
                    setText(specialization.getName());
                }
            }
        });
        listViewSpecialization.setItems(specializationObservableList);
    }

    private void setCurOfferListener(){
        listViewOffer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Offer>() {
            @Override
            public void changed(ObservableValue<? extends Offer> observableValue, Offer offer, Offer t1) {
                if (t1 == null){
                    curOffer = null;
                    setLabels(curOffer);

                    Set<Specialization> setSpec = new HashSet<>();
                    specializationOfOfferObservableList.setAll(setSpec);
                }else {
                    curOffer = t1;
                    setLabels(curOffer);

                    Set<Specialization> setSpec = curOffer.getSpecializations();
                    specializationOfOfferObservableList.setAll(setSpec);
                }
            }
        });
    }

    private void setLabels(Offer offer){

        if (offer == null) {
            nameLabel.setText("Nazwa: ");
            descriptionTextFlow.getChildren().clear();
            offerTypeLabel.setText("Typ oferty: ");
            priceLabel.setText("Cena: ");;
        } else {
            nameLabel.setText("Nazwa: " + offer.getName());

            Text text = new Text("Opis: " + offer.getDescription());
            descriptionTextFlow.getChildren().clear();
            descriptionTextFlow.getChildren().add(text);
            offerTypeLabel.setText("Typ oferty: " + offer.getOfferType());
            priceLabel.setText("Cena: " + offer.getPrice() + "zł");
        }
    }

    private void clearTextField(){

        nameTextField.clear();
        descriptionTextArea.clear();
        priceTextField.clear();
        comboboxOfferType.setValue(null);
    }

    private void clearErrorLabels(){
        errorLabel1.setText("");
        errorLabel2.setText("");
    }

    private void listenerToTextFieldSearch(){

        textfieldsearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String searchName = "";
                searchName = textfieldsearch.getText();

                List<Offer> offerList = offerService.getAllOffersByName(searchName);

                offerObservableList.setAll(offerList);
            }
        });
    }

    private void setComboboxSpecToSearch() {
        List<Specialization> specializations = new ArrayList<>(specializationObservableList);

        Specialization allOption = new Specialization();
        allOption.setId(null);
        allOption.setName("Wszystkie");

        specializations.add(0, allOption);

        specializationToSearchObservableList.setAll(specializations);
    }

    private void listenerToComboboxSpecSearch() {
        comboboxSpec.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Specialization specialization = comboboxSpec.getValue();
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

    @FXML
    private void createOffer(){

        String name;
        String description;
        double price;
        OfferType offerType;

        Offer createOffer = new Offer();

        name = nameTextField.getText();
        if (name.isBlank()){
            errorLabel1.setText("Uzupelnij nazwe");
            return;
        }

        description = descriptionTextArea.getText();
        if (description.isBlank()){
            errorLabel1.setText("Uzupełnij opis");
            return;
        }

        try {
            price = Double.parseDouble(priceTextField.getText());
        }catch (NumberFormatException numberFormatException){
            errorLabel1.setText("Zły format liczbowy");
            return;
        }

        if (price <= 0) {
            errorLabel1.setText("Cena nie moze byc rowna 0 lub mniej");
            return;
        }

        offerType = comboboxOfferType.getValue();
        if (offerType == null){
            errorLabel1.setText("Wybierz typ oferty");
            return;
        }

        createOffer.setName(name);
        createOffer.setDescription(description);
        createOffer.setPrice(price);
        createOffer.setOfferType(offerType);

        offerService.createOffer(createOffer);


        updateLists();
        clearTextField();
        clearErrorLabels();
    }

    @FXML
    private void updateOffer(){

        String name;
        String description;
        double price = -1;

        if (curOffer == null){
            errorLabel1.setText("Wybierz oferte do aktualizacji");
            return;
        }
        Long offerId = curOffer.getId();
        Optional<Offer> optionalOffer = offerService.getOffer(offerId);

        if (optionalOffer.isEmpty()){
            errorLabel1.setText("Oferta nie istnieje w bazie danych");
            return;
        }
        Offer updateOffer = optionalOffer.get();


        name = nameTextField.getText();
        description = descriptionTextArea.getText();
        OfferType offerType = comboboxOfferType.getValue();
        try {
            price = Double.parseDouble(priceTextField.getText());
        }catch (NumberFormatException e){

        }

        if (!name.isBlank()){
            updateOffer.setName(name);
        }

        if (!description.isBlank()){
            updateOffer.setDescription(description);
        }

        if (price > 0){
            updateOffer.setPrice(price);
        }

        if (offerType != null){
            updateOffer.setOfferType(offerType);
        }

        offerService.updateOffer(offerId,updateOffer);
        updateLists();
        clearTextField();
        clearErrorLabels();
    }

    @FXML
    private void deleteOffer(){

        if (curOffer == null){
            errorLabel1.setText("Wybierz oferte do usuniecia");
            return;
        }
        Long offerID = curOffer.getId();
        offerService.deleteOffer(offerID);

        updateLists();
        clearErrorLabels();
    }

    @FXML
    private void addSpecToOffer(){

        if (curOffer == null){
            errorLabel1.setText("Wybierz oferte do ktorej chcesz dodac specjalizacje");
            return;
        }

        Long offerID = curOffer.getId();

        Specialization specialization = listViewSpecialization.getSelectionModel().getSelectedItem();

        if (specialization == null){
            errorLabel2.setText("Wybierz specjalizacje ktora chcesz dodac do oferty");
            return;
        }

        Long specID = specialization.getId();

        offerService.addSpecializationToOffer(offerID,specID);

        Optional<Offer> optionalOffer = offerService.getOffer(offerID);

        if (optionalOffer.isPresent()){

            curOffer = optionalOffer.get();
            Set<Specialization> setSpec = curOffer.getSpecializations();
            specializationOfOfferObservableList.setAll(setSpec);
        }
        updateLists();
        clearErrorLabels();
    }

    @FXML
    private void removeSpecFromOffer(){

        if (curOffer == null){
            errorLabel1.setText("Wybierz oferte");
            return;
        }

        Long offerID = curOffer.getId();

        Specialization specialization = listViewSpecOfOffer.getSelectionModel().getSelectedItem();

        if (specialization == null){
            errorLabel2.setText("Wybierz specjalizacje ktora chcesz usunac z oferty");
            return;
        }

        Long specID = specialization.getId();

        offerService.removeSpecializationFromOffer(offerID,specID);

        Optional<Offer> optionalOffer = offerService.getOffer(offerID);
        if (optionalOffer.isPresent()){

            curOffer = optionalOffer.get();
            Set<Specialization> setSpec = curOffer.getSpecializations();
            specializationOfOfferObservableList.setAll(setSpec);
        }
        updateLists();
        clearErrorLabels();
    }
}
