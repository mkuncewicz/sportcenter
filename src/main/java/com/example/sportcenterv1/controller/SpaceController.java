package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.entity.Space;
import com.example.sportcenterv1.service.SpaceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

    @FXML
    private ListView<Space> listViewSpaces;

    private ObservableList<Space> spaceObservableList = FXCollections.observableArrayList();

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldWidth;

    @FXML
    private TextField fieldLength;

    @FXML
    private TextField fieldDepth;


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
        spaceObservableList.setAll(spaceService.getAllSpaces());
        settingListSpaces();
    }

    private void settingListSpaces(){
        listViewSpaces.setCellFactory(employee -> new ListCell<Space>() {
            @Override
            protected void updateItem(Space item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + ", " + item.getType()); // Możesz tutaj wyświetlić dowolną właściwość obiektu Client
                }
            }
        });
        updateLists();
        listViewSpaces.setItems(spaceObservableList);
    }

    private void updateLists(){
        List<Space> listSpace = spaceService.getAllSpaces();
        spaceObservableList.setAll(listSpace);
    }

    @FXML
    private void addNewSpace(){
        Space saveSpace = new Space();

        String name = fieldName.getText();
        double width = Double.parseDouble(fieldWidth.getText());
        double length = Double.parseDouble(fieldLength.getText());
        double depth = Double.parseDouble(fieldDepth.getText());

        saveSpace.setName(name);
        saveSpace.setWidth(width);
        saveSpace.setLength(length);
        saveSpace.setDepth(depth);

        spaceService.createSpace(saveSpace);
        updateLists();
    }

    @FXML
    public void updateSpace(){

        Space selectedSpace = listViewSpaces.getSelectionModel().getSelectedItem();

        Space updateSpace = new Space();

        String name = fieldName.getText();
        String widthText = fieldWidth.getText();
        String lengthText = fieldLength.getText();
        String depthText = fieldDepth.getText();


        if (name != null && !name.isBlank()){
            updateSpace.setName(name);
        }

        if (widthText != null && !widthText.isBlank()){
            try {
                double width = Double.parseDouble(widthText);
                updateSpace.setWidth(width);
            }catch (NumberFormatException e){

            }
        }

        if (lengthText != null && !lengthText.isBlank()){
            try {
                double length = Double.parseDouble(lengthText);
                updateSpace.setLength(length);
            }catch (NumberFormatException e){

            }
        }

        if (depthText != null && !depthText.isBlank()){
            try {
                double depth = Double.parseDouble(depthText);
                updateSpace.setDepth(depth);
            }catch (NumberFormatException e){

            }
        }


        spaceService.updateSpace(selectedSpace.getId(), updateSpace);
        updateLists();
    }

    @FXML
    public void deleteSpace(){
        Space selectedSpace = listViewSpaces.getSelectionModel().getSelectedItem();

        if (selectedSpace != null){
            spaceService.deleteSpace(selectedSpace.getId());
        }
        updateLists();
    }

}
