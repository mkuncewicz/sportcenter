package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.entity.Client;
import com.example.sportcenterv1.service.ClientService;
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
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class ClientController {

    @Autowired
    private ApplicationContext springContext;

    @Autowired
    private ClientService clientService;

    private ObservableList<Client> clientObservableList = FXCollections.observableArrayList();

    @FXML
    private ListView<Client> listViewClient;

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

        List<Client> allClients = clientService.getAllClients();
        clientObservableList.setAll(allClients);
        settingListViewClients();

    }


    private void settingListViewClients(){
        listViewClient.setCellFactory(client -> new ListCell<Client>(){

            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null){
                    setText(null);
                }else {
                    setText(item.getFirstName() + " " + item.getFirstName());
                }
            }
        });

    }
}
