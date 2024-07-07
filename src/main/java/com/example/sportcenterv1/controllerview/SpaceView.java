package com.example.sportcenterv1.controllerview;

import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.entity.enums.CourtType;
import com.example.sportcenterv1.entity.enums.TurfType;
import com.example.sportcenterv1.entity.space.*;
import com.example.sportcenterv1.service.SpaceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpaceView extends VBox {

    private final SpaceService spaceService;

    private TextField tex1 = new TextField();
    private TextField tex2 = new TextField();
    private TextField tex3 = new TextField();
    private TextField tex4 = new TextField();

    private ComboBox<CourtType> courtTypeComboBox = new ComboBox<>();

    private ComboBox<TurfType> turfTypeComboBox = new ComboBox<>();

    private ComboBox<String> comboCat = new ComboBox<>();

    @FXML
    private ListView<Specialization> listOfSpec;

    @Getter
    private String curChoice;

    public SpaceView(String choice, SpaceService spaceService) {
        this.spaceService = spaceService;
        curChoice = choice;
        // Ustawiamy preferowaną szerokość
        this.setPrefWidth(350);
        this.setPrefHeight(750);

        choiceSetting(choice);

    }

    protected void  choiceSetting(String choice){
        settingForEnum();
        this.getChildren().clear();
        if (choice.equalsIgnoreCase("") || choice.equalsIgnoreCase("Room")){
            textFieldForBasicRoom();
        } else if (choice.equalsIgnoreCase("Koszykówka") || choice.equalsIgnoreCase("BASKETBALL_ROOM")) {
            textFieldForBasketball();
        } else if (choice.equalsIgnoreCase("Sztuki walki") || choice.equalsIgnoreCase("MARTIAL_ARTS_ROOM")) {
            textFieldForMartialArts();
        } else if (choice.equalsIgnoreCase("Piłka nożna") || choice.equalsIgnoreCase("SOCCER_FIELD")) {
            textFieldForSoccer();
        } else if (choice.equalsIgnoreCase("Basen") || choice.equalsIgnoreCase("SWIMMING_POOL")) {
            textFieldForSwimmingPool();
        } else if (choice.equalsIgnoreCase("Medyczne") || choice.equalsIgnoreCase("MEDICAL_ROOM")) {
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
        comboCat.setPromptText("Czy jest sterylna");


        this.getChildren().add(tex1);
        this.getChildren().add(tex2);
        this.getChildren().add(tex3);
        this.getChildren().add(comboCat);
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
        comboCat.setItems(stringObservableList);

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

    private void createBasketballRoom() {
        // Inicjalizacja domyślnych wartości
        BasketballRoom saveBasket = new BasketballRoom();
        String name = "Brak nazwy";
        int capacity = 10;
        double squareFootage = 10;
        int hoopCount = 2;
        CourtType courtType = null;

        // Pobieranie wartości z pól tekstowych
        if (!tex1.getText().isBlank()) name = tex1.getText();
        if (!tex2.getText().isBlank()) capacity = Integer.parseInt(tex2.getText());
        if (!tex3.getText().isBlank()) squareFootage = Double.parseDouble(tex3.getText());
        if (!tex4.getText().isBlank()) hoopCount = Integer.parseInt(tex4.getText());

        // Pobieranie wyboru z ComboBoxa
        CourtType choiceCourtType = courtTypeComboBox.getSelectionModel().getSelectedItem();
        if (choiceCourtType != null) courtType = choiceCourtType;

        // Ustawianie wartości w obiekcie
        saveBasket.setName(name);
        saveBasket.setCapacity(capacity);
        saveBasket.setSquareFootage(squareFootage);
        saveBasket.setHoopCount(hoopCount);
        saveBasket.setCourtType(courtType);

        // Próba zapisu do bazy danych
        try {
            spaceService.createSpace(saveBasket);
        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas zapisu do bazy danych: " + e.getMessage());
        }
    }
    private void createMartialArtsRoom(){
        MartialArtsRoom saveMartialArts = new MartialArtsRoom();
        String name = "Brak nazwy";
        int capacity = 10;
        double squareFootage = 10;
        int matCount = 1;

        if (!tex1.getText().isBlank()) name = tex1.getText();
        if (!tex2.getText().isBlank()) capacity = Integer.parseInt(tex2.getText());
        if (!tex3.getText().isBlank()) squareFootage = Double.parseDouble(tex3.getText());
        if (!tex4.getText().isBlank()) matCount = Integer.parseInt(tex4.getText());

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

        String select = comboCat.getSelectionModel().getSelectedItem();

        if (!tex1.getText().isBlank()) name = tex1.getText();
        if (!tex2.getText().isBlank()) capacity = Integer.parseInt(tex2.getText());
        if (!tex3.getText().isBlank()) squareFootage = Double.parseDouble(tex3.getText());
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

        if (!tex1.getText().isBlank()) name = tex1.getText();
        if (!tex2.getText().isBlank()) capacity = Integer.parseInt(tex2.getText());
        if (!tex3.getText().isBlank()) squareFootage = Double.parseDouble(tex3.getText());

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

        if (!tex1.getText().isBlank()) name = tex1.getText();
        if (!tex2.getText().isBlank()) squareFootage = Double.parseDouble(tex2.getText());
        if (!tex3.getText().isBlank()) goalCount = Integer.parseInt(tex3.getText());
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

        if (!tex1.getText().isBlank()) name = tex1.getText();
        if (!tex2.getText().isBlank()) poolLength = Double.parseDouble(tex2.getText());
        if (!tex3.getText().isBlank()) poolDepth = Double.parseDouble(tex3.getText());
        if (!tex4.getText().isBlank()) laneCount = Integer.parseInt(tex4.getText());

        swimmingPool.setName(name);
        swimmingPool.setPoolLength(poolLength);
        swimmingPool.setPoolDepth(poolDepth);
        swimmingPool.setLaneCount(laneCount);

        spaceService.createSpace(swimmingPool);
    }

    public void updateSpace(Long spaceID){
        if (curChoice.equalsIgnoreCase("") || curChoice.equalsIgnoreCase("Room")){
            updateRoom(spaceID);
        } else if (curChoice.equalsIgnoreCase("Koszykówka") || curChoice.equalsIgnoreCase("BASKETBALL_ROOM")) {
            updateBasketballRoom(spaceID);
        } else if (curChoice.equalsIgnoreCase("Sztuki walki") || curChoice.equalsIgnoreCase("MARTIAL_ARTS_ROOM")) {
            updateMariatlArtsRoom(spaceID);
        } else if (curChoice.equalsIgnoreCase("Piłka nożna") || curChoice.equalsIgnoreCase("SOCCER_FIELD")) {
            updateSoccerField(spaceID);
        } else if (curChoice.equalsIgnoreCase("Basen") || curChoice.equalsIgnoreCase("SWIMMING_POOL")) {
            updateSwimmingPool(spaceID);
        } else if (curChoice.equalsIgnoreCase("Medyczne") || curChoice.equalsIgnoreCase("MEDICAL_ROOM")) {
            updateMedicalRoom(spaceID);
        }
    }

    private void updateRoom(Long spaceID){
        Room updateRoom = new Room();

        if (!tex1.getText().isBlank())updateRoom.setName(tex1.getText());

        try {
            updateRoom.setCapacity(Integer.parseInt(tex2.getText()));
        } catch (NumberFormatException e) {
            System.err.println("Niepoprawny format liczby w polu pojemność: " + tex2.getText());
        }

        try {
            updateRoom.setSquareFootage(Double.parseDouble(tex3.getText()));
        } catch (NumberFormatException e) {
            System.err.println("Niepoprawny format liczby w polu przestrzeń: " + tex3.getText());
        }

        spaceService.updateSpace(spaceID,updateRoom);
    }
    public void updateBasketballRoom(Long spaceID) {

        BasketballRoom updateBasketball = new BasketballRoom();

        if (!tex1.getText().isBlank())updateBasketball.setName(tex1.getText());

        try {
            updateBasketball.setCapacity(Integer.parseInt(tex2.getText()));
        } catch (NumberFormatException e) {
            System.err.println("Niepoprawny format liczby w polu pojemność: " + tex2.getText());
        }

        try {
            updateBasketball.setSquareFootage(Double.parseDouble(tex3.getText()));
        } catch (NumberFormatException e) {
            System.err.println("Niepoprawny format liczby w polu przestrzeń: " + tex3.getText());
        }

        try {
            updateBasketball.setHoopCount(Integer.parseInt(tex4.getText()));
        } catch (NumberFormatException e) {
            System.err.println("Niepoprawny format liczby w polu liczba koszy: " + tex4.getText());
        }

        spaceService.updateSpace(spaceID,updateBasketball);
    }
    private void updateMariatlArtsRoom(Long spaceID){

        MartialArtsRoom updateMartialArts = new MartialArtsRoom();

        if (!tex1.getText().isBlank())updateMartialArts.setName(tex1.getText());

        try {
            updateMartialArts.setCapacity(Integer.parseInt(tex2.getText()));
        }catch (NumberFormatException e){
            System.err.println("Niepoprawny format liczby w polu pojemność: " + tex2.getText());
        }

        try {
            updateMartialArts.setSquareFootage(Double.parseDouble(tex3.getText()));
        }catch (NumberFormatException e){
            System.err.println("Niepoprawny format liczby w polu przestrzeń: " + tex3.getText());
        }

        try {
            updateMartialArts.setMatCount(Integer.parseInt(tex4.getText()));
        }catch (NumberFormatException e){
            System.err.println("Niepoprawny format liczby w polu przestrzeń: " + tex4.getText());
        }

        spaceService.updateSpace(spaceID,updateMartialArts);
    }
    private void updateSoccerField(Long spaceID){

        SoccerField updateSoccerField = new SoccerField();

        if (!tex1.getText().isBlank()) updateSoccerField.setName(tex1.getText());

        try {
            updateSoccerField.setSquareFootage(Double.parseDouble(tex2.getText()));
        }catch (NumberFormatException e){
            System.err.println("Niepoprawny format liczby w polu przestrzeń: " + tex2.getText());
        }

        try {
            updateSoccerField.setGoalCount(Integer.parseInt(tex3.getText()));
        }catch (NumberFormatException e){
            System.err.println("Niepoprawny format liczby w polu przestrzeń: " + tex3.getText());
        }


        updateSoccerField.setTurfType(turfTypeComboBox.getSelectionModel().getSelectedItem());

        spaceService.updateSpace(spaceID,updateSoccerField);
    }
    private void updateSwimmingPool(Long spaceID){

        SwimmingPool updateSwimmingPool = new SwimmingPool();

        if (!tex1.getText().isBlank()) updateSwimmingPool.setName(tex1.getText());

        try {
            updateSwimmingPool.setPoolLength(Double.parseDouble(tex2.getText()));
        }catch (NumberFormatException e){
            System.err.println("Niepoprawny format liczby w polu przestrzeń: " + tex2.getText());
        }

        try {
            updateSwimmingPool.setPoolDepth(Double.parseDouble(tex3.getText()));
        }catch (NumberFormatException e){
            System.err.println("Niepoprawny format liczby w polu przestrzeń: " + tex3.getText());
        }

        try {
            updateSwimmingPool.setLaneCount(Integer.parseInt(tex4.getText()));
        }catch (NumberFormatException e){
            System.err.println("Niepoprawny format liczby w polu przestrzeń: " + tex4.getText());
        }


        spaceService.updateSpace(spaceID,updateSwimmingPool);
    }
    private void updateMedicalRoom(Long spaceID){

        Optional<Space> optionalSpace = spaceService.getSpace(spaceID);
        MedicalRoom space = (MedicalRoom) optionalSpace.get();


        MedicalRoom updateMedical = new MedicalRoom();

        if (tex1.getText().isBlank()) updateMedical.setName(tex1.getText());

        try {
            updateMedical.setCapacity(Integer.parseInt(tex2.getText()));
        }catch (NumberFormatException e){
            System.err.println("Niepoprawny format liczby w polu przestrzeń: " + tex2.getText());
        }

        try {
            updateMedical.setSquareFootage(Double.parseDouble(tex3.getText()));
        }catch (NumberFormatException e){
            updateMedical.setCapacity(Integer.parseInt(tex3.getText()));
        }

        if (comboCat.getSelectionModel().getSelectedItem().equalsIgnoreCase( "Tak")){
            updateMedical.setSterile(true);
        } else if (comboCat.getSelectionModel().getSelectedItem().equalsIgnoreCase("Nie")) {
            updateMedical.setSterile(false);
        }else {
            updateMedical.setSterile(space.isSterile());
        }

        spaceService.updateSpace(spaceID,updateMedical);
    }
}