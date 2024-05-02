package com.example.sportcenterv1.controllerview;

import com.example.sportcenterv1.entity.enums.CourtType;
import com.example.sportcenterv1.entity.enums.TurfType;
import com.example.sportcenterv1.entity.space.*;
import com.example.sportcenterv1.service.SpaceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpaceView extends VBox {

    @Autowired
    private SpaceService spaceService;

    private TextField tex1 = new TextField();
    private TextField tex2 = new TextField();
    private TextField tex3 = new TextField();
    private TextField tex4 = new TextField();

    private ComboBox<CourtType> courtTypeComboBox = new ComboBox<>();

    private ComboBox<TurfType> turfTypeComboBox = new ComboBox<>();

    private ComboBox<String> comboBoxTest = new ComboBox<>();

    @Getter
    private String curChoice;
    public SpaceView(String choice) {
        curChoice = choice;
        // Ustawiamy styl
        this.setStyle("-fx-background-color: whitesmoke");
        // Ustawiamy preferowaną szerokość
        this.setPrefWidth(350);
        this.setPrefHeight(750);

        choiceSetting(choice);

    }

    private void  choiceSetting(String choice){
        settingForEnum();
        this.getChildren().clear();
        if (choice.equalsIgnoreCase("")){
            textFieldForBasicRoom();
        } else if (choice.equalsIgnoreCase("Koszykówka")) {
            textFieldForBasketball();
        } else if (choice.equalsIgnoreCase("Sztuki walki")) {
            textFieldForMartialArts();
        } else if (choice.equalsIgnoreCase("Piłka nożna")) {
            textFieldForSoccer();
        } else if (choice.equalsIgnoreCase("Basen")) {
            textFieldForSwimmingPool();
        } else if (choice.equalsIgnoreCase("Medyczne")) {
            textFieldForMedical();
        }
    }
    
    private void textFieldForBasicRoom(){
        tex1.setPromptText("Nazwa");
        tex2.setPromptText("Pojemność");
        tex3.setPromptText("Przestrzeń (metry kwadratowe)");

        this.getChildren().add(tex1);
        this.getChildren().add(tex2);
        this.getChildren().add(tex3);
    }

    private void textFieldForBasketball(){

        tex1.setPromptText("Nazwa");
        tex2.setPromptText("Pojemność");
        tex3.setPromptText("Przestrzeń (metry kwadratowe)");

      tex4.setPromptText("Liczba koszy");
      courtTypeComboBox.setPromptText("Typ nawierzchni");

      this.getChildren().add(tex1);
      this.getChildren().add(tex2);
      this.getChildren().add(tex3);
      this.getChildren().add(tex4);
      this.getChildren().add(courtTypeComboBox);
    }

    private void textFieldForSoccer(){
        tex1.setPromptText("Nazwa");
        tex2.setPromptText("Przestrzeń (metry kwadratowe)");
        tex3.setPromptText("Ilość bramek)");
        turfTypeComboBox.setPromptText("Typ nawierzchni");

        this.getChildren().add(tex1);
        this.getChildren().add(tex2);
        this.getChildren().add(tex3);
        this.getChildren().add(turfTypeComboBox);
    }

    private void textFieldForMartialArts(){
        tex1.setPromptText("Nazwa");
        tex2.setPromptText("Pojemność");
        tex3.setPromptText("Przestrzeń (metry kwadratowe)");
        tex4.setPromptText("Ilość mat");

        this.getChildren().add(tex1);
        this.getChildren().add(tex2);
        this.getChildren().add(tex3);
        this.getChildren().add(tex4);
    }

    private void textFieldForMedical(){
        tex1.setPromptText("Nazwa");
        tex2.setPromptText("Pojemność");
        tex3.setPromptText("Przestrzeń (metry kwadratowe)");
        comboBoxTest.setPromptText("Czy jest sterylna");


        this.getChildren().add(tex1);
        this.getChildren().add(tex2);
        this.getChildren().add(tex3);
        this.getChildren().add(comboBoxTest);
    }


    private void textFieldForSwimmingPool(){
        tex1.setPromptText("Nazwa");
        tex2.setPromptText("Długość basenu");
        tex3.setPromptText("Głębokość basenu");
        tex4.setPromptText("Ilość torów");

        this.getChildren().add(tex1);
        this.getChildren().add(tex2);
        this.getChildren().add(tex3);
        this.getChildren().add(tex4);
    }

    private void settingForEnum(){
        //Court
        List<CourtType> courtTypesList = List.of(CourtType.WOOD,CourtType.TAR,CourtType.SYNTHETIC,CourtType.CONCRETE);
        ObservableList<CourtType> courtTypeObservableList = FXCollections.observableArrayList(courtTypesList);
        courtTypeComboBox.setItems(courtTypeObservableList);

        //Turf
        List<TurfType> turfTypeList = List.of(TurfType.ARTIFICIAL, TurfType.HYBRID, TurfType.NATURAL);
        ObservableList<TurfType> turfTypeObservableList = FXCollections.observableArrayList(turfTypeList);
        turfTypeComboBox.setItems(turfTypeObservableList);

        List<String> stringList = new ArrayList<>();
        stringList.add("Tak");
        stringList.add("Nie");
        ObservableList<String> stringObservableList = FXCollections.observableList(stringList);
        comboBoxTest.setItems(stringObservableList);

    }

    public void createSpace(){
        if (curChoice.equalsIgnoreCase("")){
            createRoom();
        } else if (curChoice.equalsIgnoreCase("Koszykówka")) {
            createBasketballRoom();
        } else if (curChoice.equalsIgnoreCase("Sztuki walki")) {
            createMartialArtsRoom();
        } else if (curChoice.equalsIgnoreCase("Piłka nożna")) {
            createSoccerField();
        } else if (curChoice.equalsIgnoreCase("Basen")) {
            createSwimmingPool();
        } else if (curChoice.equalsIgnoreCase("Medyczne")) {
            createMedicalRoom();
        }
    }

    private void createBasketballRoom(){
        BasketballRoom saveBasket = new BasketballRoom();

        String name = "Brak nazwy";
        int capacity = 10;
        double squareFootage = 10;
        int hoopCount = 2;
        CourtType courtType = null;

        CourtType choiceCourtType = courtTypeComboBox.getSelectionModel().getSelectedItem();

        if (tex1.getText().isBlank()) name = tex1.getText();
        if (tex2.getText().isBlank()) capacity = Integer.parseInt(tex2.getText());
        if (tex3.getText().isBlank()) squareFootage = Double.parseDouble(tex3.getText());
        if (tex4.getText().isBlank()) hoopCount = Integer.parseInt(tex4.getText());
        if (choiceCourtType != null) courtType = choiceCourtType;

        saveBasket.setName(name);
        saveBasket.setCapacity(capacity);
        saveBasket.setSquareFootage(squareFootage);
        saveBasket.setHoopCount(hoopCount);
        saveBasket.setCourtType(courtType);

        spaceService.createSpace(saveBasket);

    }
    private void createMartialArtsRoom(){
        MartialArtsRoom saveMartialArts = new MartialArtsRoom();
        String name = "Brak nazwy";
        int capacity = 10;
        double squareFootage = 10;
        int matCount = 1;

        if (tex1.getText().isBlank()) name = tex1.getText();
        if (tex2.getText().isBlank()) capacity = Integer.parseInt(tex2.getText());
        if (tex3.getText().isBlank()) squareFootage = Double.parseDouble(tex3.getText());
        if (tex4.getText().isBlank()) matCount = Integer.parseInt(tex4.getText());

        saveMartialArts.setName(name);
        saveMartialArts.setCapacity(capacity);
        saveMartialArts.setSquareFootage(squareFootage);
        saveMartialArts.setMatCount(matCount);

        spaceService.createSpace(saveMartialArts);
    }
    private void createMedicalRoom(){
        MedicalRoom saveMedical = new MedicalRoom();

        String name = "Brak nazwy";
        int capacity = 10;
        double squareFootage = 10;
        boolean choice = false;

        String select = comboBoxTest.getSelectionModel().getSelectedItem();

        if (tex1.getText().isBlank()) name = tex1.getText();
        if (tex2.getText().isBlank()) capacity = Integer.parseInt(tex2.getText());
        if (tex3.getText().isBlank()) squareFootage = Double.parseDouble(tex3.getText());
        if (select.equalsIgnoreCase("Tak")) choice = true;

        saveMedical.setName(name);
        saveMedical.setCapacity(capacity);
        saveMedical.setSquareFootage(squareFootage);
        saveMedical.setSterile(choice);

        spaceService.createSpace(saveMedical);
    }
    private void createRoom(){
        Room saveRoom = new Room();
        String name = "Brak nazwy";
        int capacity = 10;
        double squareFootage = 10;

        if (tex1.getText().isBlank()) name = tex1.getText();
        if (tex2.getText().isBlank()) capacity = Integer.parseInt(tex2.getText());
        if (tex3.getText().isBlank()) squareFootage = Double.parseDouble(tex3.getText());

        saveRoom.setName(name);
        saveRoom.setCapacity(capacity);
        saveRoom.setSquareFootage(squareFootage);

        spaceService.createSpace(saveRoom);
    }
    private void createSoccerField(){
        SoccerField saveSoccer = new SoccerField();

        String name = "Brak nazwy";
        double squareFootage = 7140;
        int goalCount = 2;
        TurfType turfType = TurfType.ARTIFICIAL;

        TurfType choiceTurf = turfTypeComboBox.getSelectionModel().getSelectedItem();

        if (tex1.getText().isBlank()) name = tex1.getText();
        if (tex2.getText().isBlank()) squareFootage = Double.parseDouble(tex2.getText());
        if (tex3.getText().isBlank()) goalCount = Integer.parseInt(tex3.getText());
        if (choiceTurf != null) turfType = choiceTurf;

        saveSoccer.setName(name);
        saveSoccer.setSquareFootage(squareFootage);
        saveSoccer.setGoalCount(goalCount);
        saveSoccer.setTurfType(turfType);

        spaceService.createSpace(saveSoccer);
    }
    private void createSwimmingPool(){
        SwimmingPool swimmingPool = new SwimmingPool();

        String name = "Brak nazwy";
        double poolLength = 50;
        double poolDepth = 2;
        int laneCount = 10;

        if (tex1.getText().isBlank()) name = tex1.getText();
        if (tex2.getText().isBlank()) poolLength = Double.parseDouble(tex2.getText());
        if (tex3.getText().isBlank()) poolDepth = Double.parseDouble(tex3.getText());
        if (tex4.getText().isBlank()) laneCount = Integer.parseInt(tex4.getText());

        swimmingPool.setName(name);
        swimmingPool.setPoolLength(poolLength);
        swimmingPool.setPoolDepth(poolDepth);
        swimmingPool.setLaneCount(laneCount);

        spaceService.createSpace(swimmingPool);
    }
}