package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.controllerview.SpaceDetails;
import com.example.sportcenterv1.controllerview.SpaceView;
import com.example.sportcenterv1.dm.DarkModeSingleton;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.space.Space;
import com.example.sportcenterv1.service.SpaceService;
import com.example.sportcenterv1.service.SpaceSpecializationService;
import com.example.sportcenterv1.service.SpecializationService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SpaceController {

    private DarkModeSingleton darkModeSingleton = DarkModeSingleton.getInstance();

    @FXML
    private AnchorPane mainPane;

    private boolean isDarkMode = false;

    @Autowired
    private ApplicationContext springContext;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private SpecializationService specializationService;

    @Autowired
    private SpaceSpecializationService spaceSpecializationService;

    @FXML
    private ListView<Space> listViewSpaces;

    private ObservableList<Space> spaceObservableList = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> comboCat;

    @FXML
    private VBox vboxFieldsMod;
    private SpaceView curSpaceView;

    @FXML
    private VBox spaceDetails;

    private SpaceDetails details = new SpaceDetails();

    //Specialization
    @FXML
    private ListView<Specialization> listOfSpec;

    private ObservableList<Specialization> specializationObservableList = FXCollections.observableArrayList();

    //SpecOfSpace
    @FXML
    private ListView<Specialization> listViewSpecOfSpace;
    private ObservableList<Specialization> specOfSpaceObservableList = FXCollections.observableArrayList();


    private ObjectProperty<Space> curSpace = new SimpleObjectProperty<>(null);

    //ErrorLabels
    @FXML
    private Label errorLabel1;

    @FXML
    private Label errorLabel2;

    @FXML
    private ComboBox<Specialization> comboboxspec;

    private ObservableList<Specialization> specializationForSearchObservableList = FXCollections.observableArrayList();

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
    private void setDarkMode(){
        String darkMode = getClass().getResource("/css/DMspaceManagerStyle2.css").toExternalForm();

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

        String darkMode = getClass().getResource("/css/DMspaceManagerStyle2.css").toExternalForm();

        if (isDarkMode){
            mainPane.getStylesheets().add(darkMode);
        }else {
            mainPane.getStylesheets().remove(darkMode);
        }
    }

    @FXML
    public void initialize(){
        //DarkMode
        isDarkMode = darkModeSingleton.isDarkMode();
        checkActiveDarkMode();

        settingChoice();
        listenerToComboBox();
        settingListViewSpace();
        spaceObservableList.setAll(spaceService.getAllSpaces());
        listenerToListViewSpace();
        listenerToCurSpace();
        settingComboboxspec();

        //Spec list
        specializationObservableList.setAll(specializationService.getAllSpecializations());
        settingSpecList();

        //Spec of Space
        listViewSpecOfSpace.setItems(specOfSpaceObservableList);
    }

    private void getSpecOfSpace(Space space){
        specOfSpaceObservableList.setAll(space.getSpecializations());

    }

    private void settingChoice(){
        List<String> stringList = List.of("Wszystkie","Sale/Pomieszczenia","Koszykówka","Sztuki walki","Piłka nożna","Basen","Medyczne");
        ObservableList<String> listOfTypeSpaces = FXCollections.observableArrayList();
        listOfTypeSpaces.setAll(stringList);
        comboCat.setItems(listOfTypeSpaces);
    }

    private void settingComboboxspec(){
        List<Specialization> specializationList = specializationService.getAllSpecializations();
        specializationList.add(0, null);
        specializationForSearchObservableList.setAll(specializationList);

        comboboxspec.setItems(specializationForSearchObservableList);
        comboboxspec.setCellFactory(param -> new ListCell<Specialization>() {
            @Override
            protected void updateItem(Specialization specialization, boolean empty) {
                super.updateItem(specialization, empty);
                if (empty || specialization == null) {
                    setText("Wszystkie");
                } else {
                    setText(specialization.getName());
                }
            }
        });


        comboboxspec.setButtonCell(new ListCell<Specialization>(){

            @Override
            protected void updateItem(Specialization specialization, boolean b) {
                super.updateItem(specialization, b);
                if (b || specialization == null){
                    setText("Wszystkie");
                }else{
                    setText(specialization.getName());
                }
            }
        });

        listenerToComboboxSpec();
    }

    private void listenerToComboboxSpec(){

        comboboxspec.getSelectionModel().selectedItemProperty().addListener((observableValue, specialization, t1) -> {
            comboCat.getSelectionModel().select(0);

            List<Space> spaceList = spaceService.getAllSpaces(t1);
            spaceObservableList.setAll(spaceList);


        });
    }

    private void listenerToComboBox(){
        comboCat.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {

            SpaceView newView = new SpaceView(t1, spaceService,errorLabel1);

            curSpaceView = newView;

            vboxFieldsMod.getChildren().clear();
            vboxFieldsMod.getChildren().add(newView);
            setCurSpaceList(t1);
        });
    }

    private void listenerToListViewSpace(){

        listViewSpaces.getSelectionModel().selectedItemProperty().addListener((observableValue, space, t1) -> {

            curSpace.set(t1);

            List<Label> listLabel = details.getDetailsLabelsForSpace(t1);
            spaceDetails.getChildren().clear();
            spaceDetails.getChildren().addAll(listLabel);

            String type = spaceService.getSpaceType(t1.getId());

            SpaceView newView = new SpaceView(type, spaceService,errorLabel1);

            curSpaceView = newView;

            vboxFieldsMod.getChildren().clear();
            vboxFieldsMod.getChildren().add(newView);
            setCurSpaceList(type);

            getSpecOfSpace(t1);
        });
    }

    private void listenerToCurSpace(){

        curSpace.addListener(new ChangeListener<Space>() {
            @Override
            public void changed(ObservableValue<? extends Space> observableValue, Space space, Space t1) {

                if (t1 == null){
                    //Jezeli obiekt nie jest wybrany, czyscimy widok specjalizacji/kategorii ostatniego wybranego obiektu
                    List<Specialization> emptyList = new ArrayList<>();
                    specOfSpaceObservableList.setAll(emptyList);
                }else {
                    List<Specialization> specializationList = new ArrayList<>(t1.getSpecializations());
                    specOfSpaceObservableList.setAll(specializationList);
                }
            }
        });
    }

    private void setCurSpaceList(String choice) {


        if (choice == null) {

        }else if (choice.equalsIgnoreCase("Wszystkie")){
            List<Space> spaceList = spaceService.getAllSpaces();
            spaceObservableList.setAll(spaceList);

        } else if (choice.equalsIgnoreCase("Koszykówka")) {
            List<Space> spaceList = spaceService.getAllSpaces("BASKETBALL_ROOM");
            spaceObservableList.setAll(spaceList);

        } else if (choice.equalsIgnoreCase("Sztuki walki")) {
            List<Space> spaceList = spaceService.getAllSpaces("MARTIAL_ARTS_ROOM");
            spaceObservableList.setAll(spaceList);

        }else if(choice.equalsIgnoreCase("Piłka nożna")){
            List<Space> spaceList = spaceService.getAllSpaces("SOCCER_FIELD");
            spaceObservableList.setAll(spaceList);

        }else if (choice.equalsIgnoreCase("Sale/Pomieszczenia")){
            List<Space> spaceList = spaceService.getAllSpaces("ROOM");
            spaceObservableList.setAll(spaceList);

        }else if(choice.equalsIgnoreCase("Basen")){
            List<Space> spaceList = spaceService.getAllSpaces("SWIMMING_POOL");
            spaceObservableList.setAll(spaceList);

        } else if (choice.equalsIgnoreCase("Medyczne")) {
            List<Space> spaceList = spaceService.getAllSpaces("MEDICAL_ROOM");
            spaceObservableList.setAll(spaceList);
        }
    }

    private void settingListViewSpace(){
        listViewSpaces.setCellFactory(space -> new ListCell<Space>(){

            @Override
            protected void updateItem(Space item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null){
                    setText(null);
                }else {
                    setText(item.getName());
                }
            }
        });
        listViewSpaces.setItems(spaceObservableList);
    }

    private void reloadSpaceList(){
        String curChoice;

        if (comboCat.getSelectionModel().getSelectedItem() != null) curChoice = comboCat.getSelectionModel().getSelectedItem();
        else  curChoice = "Wszystkie";
        setCurSpaceList(curChoice);
    }

    private void reloadSpecializationsList(){

        Space space = curSpace.get();

        if (space == null){
            //Jezeli obiekt nie jest wybrany, czyscimy widok specjalizacji/kategorii ostatniego wybranego obiektu
            List<Specialization> emptyList = new ArrayList<>();
            specOfSpaceObservableList.setAll(emptyList);
        }else {

            Optional<Space> updateSpace = spaceService.getSpace(space.getId());
            if (updateSpace.isPresent()){

            space = updateSpace.get();
            List<Specialization> specializationList = new ArrayList<>(space.getSpecializations());
            specOfSpaceObservableList.setAll(specializationList);

        }}
    }



    @FXML
    private void createNewSpace(){

        String selectedItem = comboCat.getSelectionModel().getSelectedItem();
        if (selectedItem == null) selectedItem = "Wszystkie";
        curSpaceView.setCurChoice(selectedItem);

        curSpaceView.createSpace();
        spaceObservableList.setAll(spaceService.getAllSpaces());

        reloadSpaceList();
    }

    @FXML
    private void deleteSpace(){

        Space space = listViewSpaces.getSelectionModel().getSelectedItem();
        spaceService.deleteSpace(space.getId());
        spaceObservableList.setAll(spaceService.getAllSpaces());

        reloadSpaceList();
    }

    @FXML
    private void updateSpace(){
        Long spaceID = listViewSpaces.getSelectionModel().getSelectedItem().getId();
        curSpaceView.updateSpace(spaceID);

        reloadSpaceList();
    }

    private void settingSpecList(){
        listOfSpec.setCellFactory(spec -> new ListCell<Specialization>(){

            @Override
            protected void updateItem(Specialization item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null){
                    setText(null);
                }else {
                    setText(item.getName());
                }
            }
        });
        listOfSpec.setItems(specializationObservableList);
    }

    @FXML
    private void addSpecializationToSpace(){
        Space space = listViewSpaces.getSelectionModel().getSelectedItem();
        Specialization specialization = listOfSpec.getSelectionModel().getSelectedItem();

        spaceSpecializationService.addSpaceToSpecialization(space.getId(), specialization.getId());

        specOfSpaceObservableList.setAll(space.getSpecializations());
        listViewSpecOfSpace.setItems(specOfSpaceObservableList);

        reloadSpecializationsList();
    }

    @FXML
    private void removeSpecializationFromSpace(){
        Space space = listViewSpaces.getSelectionModel().getSelectedItem();
        Specialization specialization = listViewSpecOfSpace.getSelectionModel().getSelectedItem();

        spaceSpecializationService.deleteSpaceFromSpecialization(space.getId(), specialization.getId());
        reloadSpecializationsList();
    }
}

