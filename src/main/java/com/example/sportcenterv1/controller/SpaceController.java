package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.controllerview.SpaceDetails;
import com.example.sportcenterv1.controllerview.SpaceView;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.space.Space;
import com.example.sportcenterv1.service.SpaceService;
import com.example.sportcenterv1.service.SpaceSpecializationService;
import com.example.sportcenterv1.service.SpecializationService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SpaceController {

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
    private SpaceView curSpaceView = new SpaceView("",spaceService);

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
    public void initialize(){
        settingChoice();
        settingSpaceView();
        listenerToComboBox();
        settingListViewSpace();
        spaceObservableList.setAll(spaceService.getAllSpaces());
        listenerToListViewItem();

        //Spec list test
        specializationObservableList.setAll(specializationService.getAllSpecializations());
        settingSpecList();

        //Spec of Space
        listViewSpecOfSpace.setItems(specOfSpaceObservableList);
    }

    private void getSpecOfSpace(Space space){
        specOfSpaceObservableList.setAll(space.getSpecializations());

    }

    private void settingSpaceView(){
        SpaceView newView = new SpaceView("", spaceService);
        vboxFieldsMod.getChildren().clear(); // Wyczyść zawartość HBox, jeśli chcesz podmienić widok
        vboxFieldsMod.getChildren().add(newView); // Dodaj nowy widok
    }

    private void settingChoice(){
        List<String> stringList = List.of("","Koszykówka","Sztuki walki","Piłka nożna","Basen","Medyczne");
        ObservableList<String> listOfTypeSpaces = FXCollections.observableArrayList();
        listOfTypeSpaces.setAll(stringList);
        comboCat.setItems(listOfTypeSpaces);
    }
    private void listenerToComboBox(){
        comboCat.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {

            SpaceView newView = new SpaceView(t1, spaceService);

            curSpaceView = newView;

            vboxFieldsMod.getChildren().clear();
            vboxFieldsMod.getChildren().add(newView);
            setCurSpaceList(t1);
        });
    }

    private void listenerToListViewItem(){

        listViewSpaces.getSelectionModel().selectedItemProperty().addListener((observableValue, space, t1) -> {


            List<Label> listLabel = details.getDetailsLabelsForSpace(t1);
            spaceDetails.getChildren().clear();
            spaceDetails.getChildren().addAll(listLabel);

            String type = spaceService.getSpaceType(t1.getId());

            SpaceView newView = new SpaceView(type, spaceService);

            curSpaceView = newView;

            vboxFieldsMod.getChildren().clear();
            vboxFieldsMod.getChildren().add(newView);
            setCurSpaceList(type);

            getSpecOfSpace(t1);
        });
    }

    private void setCurSpaceList(String choice){

        if (choice.equalsIgnoreCase("")){
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

    @FXML
    private void createNewSpace(){
        curSpaceView.createSpace();
        spaceObservableList.setAll(spaceService.getAllSpaces());
    }

    @FXML
    private void deleteSpace(){

        Space space = listViewSpaces.getSelectionModel().getSelectedItem();
        spaceService.deleteSpace(space.getId());
        spaceObservableList.setAll(spaceService.getAllSpaces());
    }

    @FXML
    private void updateSpace(){
        Long spaceID = listViewSpaces.getSelectionModel().getSelectedItem().getId();
        curSpaceView.updateSpace(spaceID);
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


    }

    @FXML
    private void removeSpecializationFromSpace(){
        Space space = listViewSpaces.getSelectionModel().getSelectedItem();
        Specialization specialization = listViewSpecOfSpace.getSelectionModel().getSelectedItem();

        spaceSpecializationService.deleteSpaceFromSpecialization(space.getId(), specialization.getId());
    }
}