package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.controllerview.SpaceDetails;
import com.example.sportcenterv1.controllerview.SpaceView;
import com.example.sportcenterv1.entity.space.*;
import com.example.sportcenterv1.service.SpaceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SpaceController {

    @Autowired
    private ApplicationContext springContext;

    @Autowired
    private SpaceService spaceService;

    @FXML
    private ListView<Space> listViewSpaces;

    private ObservableList<Space> spaceObservableList = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> comboTest;

    @FXML
    private VBox vboxTest;
    private SpaceView curSpaceView = new SpaceView("",spaceService);

    @FXML
    private VBox spaceDetails;

    private SpaceDetails details = new SpaceDetails();

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

        //Test
        listenerToListViewItem();
    }

    private void settingSpaceView(){
        SpaceView newView = new SpaceView("", spaceService);
        vboxTest.getChildren().clear(); // Wyczyść zawartość HBox, jeśli chcesz podmienić widok
        vboxTest.getChildren().add(newView); // Dodaj nowy widok
    }

    private void settingChoice(){
        List<String> stringList = List.of("","Koszykówka","Sztuki walki","Piłka nożna","Basen","Medyczne");
        ObservableList<String> listOfTypeSpaces = FXCollections.observableArrayList();
        listOfTypeSpaces.setAll(stringList);
        comboTest.setItems(listOfTypeSpaces);
    }
    private void listenerToComboBox(){
        comboTest.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {

            SpaceView newView = new SpaceView(t1, spaceService);

            curSpaceView = newView;

            vboxTest.getChildren().clear();
            vboxTest.getChildren().add(newView);
            setCurSpaceList(t1);

                });
    }

    private void listenerToListViewItem(){

        listViewSpaces.getSelectionModel().selectedItemProperty().addListener((observableValue, space, t1) -> {
            System.out.println("Uzyłeś");
            System.out.print("Obiekt: " + t1.getName());

            List<Label> listLabel = details.getLabels((Room)t1);
            spaceDetails.getChildren().clear();
            spaceDetails.getChildren().addAll(listLabel);
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

    }

    @FXML
    private void deleteSpace(){

        Space space = listViewSpaces.getSelectionModel().getSelectedItem();
        spaceService.deleteSpace(space.getId());

    }

    @FXML
    private void updateSpace(){
        Long spaceID = listViewSpaces.getSelectionModel().getSelectedItem().getId();
        curSpaceView.updateSpace(spaceID);
    }
}
