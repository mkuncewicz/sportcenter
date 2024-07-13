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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

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
    @Setter
    private String curChoice;

    private final Label errorLabel;


    public SpaceView(String choice, SpaceService spaceService, Label errorLabel) {
        this.spaceService = spaceService;
        curChoice = choice;
        // Ustawiamy preferowaną szerokość
        this.setPrefWidth(350);
        this.setPrefHeight(750);

        this.errorLabel = errorLabel;

        choiceSetting(choice);

    }

    private void  choiceSetting(String choice){
        settingForEnum();
        this.getChildren().clear();
        if (choice.equalsIgnoreCase("") || choice.equalsIgnoreCase("Sale/Pomieszczenia") ||choice.equalsIgnoreCase("ROOM")){
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

        if (curChoice.equalsIgnoreCase("") || curChoice.equalsIgnoreCase("Sale/Pomieszczenia") || curChoice.equalsIgnoreCase("Wszystkie")){
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
        String name;
        int capacity;
        double squareFootage;
        int hoopCount;
        CourtType courtType;

        // Pobieranie wartości z pól tekstowych
        if (tex1.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole nazwy");
            return;
        }
        name = tex1.getText();

        if (tex2.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole pojemności");
            return;
        }
        try {
            capacity = Integer.parseInt(tex2.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }
        if (capacity <= 0){
            errorLabel.setText("Liczba musi być większa od zera");
            return;
        }

        if (tex3.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole metrów kwadratowych pomieszczenia");
            return;
        }
        try {
            squareFootage = Integer.parseInt(tex3.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }
        if (squareFootage <= 0){
            errorLabel.setText("Liczba musi być większa od zera");
            return;
        }

        if (tex4.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole liczba zamontowanych koszy/obręczy");
            return;
        }
        try {
            hoopCount = Integer.parseInt(tex4.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }

        if (hoopCount < 0){
            errorLabel.setText("Liczba nie może być ujemna");
            return;
        }

        // Pobieranie wyboru z ComboBoxa
        CourtType choiceCourtType = courtTypeComboBox.getSelectionModel().getSelectedItem();
        if (choiceCourtType == null){
            errorLabel.setText("Wybierz typ nawierzchni");
            return;
        }
        courtType = choiceCourtType;

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
        errorLabel.setText("");
    }
    private void createMartialArtsRoom(){
        MartialArtsRoom saveMartialArts = new MartialArtsRoom();
        String name;
        int capacity;
        double squareFootage;
        int matCount;

        // Pobieranie wartości z pól tekstowych
        if (tex1.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole nazwy");
            return;
        }
        name = tex1.getText();

        if (tex2.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole pojemności");
            return;
        }
        try {
            capacity = Integer.parseInt(tex2.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }
        if (capacity <= 0){
            errorLabel.setText("Liczba musi być większa od zera");
            return;
        }

        if (tex3.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole metrów kwadratowych pomieszczenia");
            return;
        }
        try {
            squareFootage = Integer.parseInt(tex3.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }
        if (squareFootage <= 0){
            errorLabel.setText("Liczba musi być większa od zera");
            return;
        }

        if (tex4.getText().isBlank()){
            errorLabel.setText("Uzupelnij liczbe mat w sali");
            return;
        }
        try {
            matCount = Integer.parseInt(tex4.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }

        if (matCount < 0){
            errorLabel.setText("Liczba nie może być ujemna");
            return;
        }

        saveMartialArts.setName(name);
        saveMartialArts.setCapacity(capacity);
        saveMartialArts.setSquareFootage(squareFootage);
        saveMartialArts.setMatCount(matCount);

        // Próba zapisu do bazy danych
        try {
            spaceService.createSpace(saveMartialArts);
        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas zapisu do bazy danych: " + e.getMessage());
        }
        errorLabel.setText("");
    }
    private void createMedicalRoom(){
        MedicalRoom saveMedical = new MedicalRoom();

        String name;
        int capacity;
        double squareFootage;
        boolean choice;

        // Pobieranie wartości z pól tekstowych
        if (tex1.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole nazwy");
            return;
        }
        name = tex1.getText();

        if (tex2.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole pojemności");
            return;
        }
        try {
            capacity = Integer.parseInt(tex2.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }
        if (capacity <= 0){
            errorLabel.setText("Liczba musi być większa od zera");
            return;
        }

        if (tex3.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole metrów kwadratowych pomieszczenia");
            return;
        }
        try {
            squareFootage = Integer.parseInt(tex3.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }
        if (squareFootage <= 0){
            errorLabel.setText("Liczba musi być większa od zera");
            return;
        }

        String select = comboCat.getSelectionModel().getSelectedItem();
        if (select.equalsIgnoreCase("Tak")) choice = true;
        else choice = false;

        saveMedical.setName(name);
        saveMedical.setCapacity(capacity);
        saveMedical.setSquareFootage(squareFootage);
        saveMedical.setSterile(choice);

        // Próba zapisu do bazy danych
        try {
            spaceService.createSpace(saveMedical);
        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas zapisu do bazy danych: " + e.getMessage());
        }
        errorLabel.setText("");
    }
    private void createRoom(){
        Room saveRoom = new Room();
        String name;
        int capacity;
        double squareFootage;

        // Pobieranie wartości z pól tekstowych
        if (tex1.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole nazwy");
            return;
        }
        name = tex1.getText();

        if (tex2.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole pojemności");
            return;
        }
        try {
            capacity = Integer.parseInt(tex2.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }
        if (capacity <= 0){
            errorLabel.setText("Liczba musi być większa od zera");
            return;
        }

        if (tex3.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole metrów kwadratowych pomieszczenia");
            return;
        }
        try {
            squareFootage = Integer.parseInt(tex3.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }
        if (squareFootage <= 0){
            errorLabel.setText("Liczba musi być większa od zera");
            return;
        }

        saveRoom.setName(name);
        saveRoom.setCapacity(capacity);
        saveRoom.setSquareFootage(squareFootage);

        // Próba zapisu do bazy danych
        try {
            spaceService.createSpace(saveRoom);
        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas zapisu do bazy danych: " + e.getMessage());
        }
        errorLabel.setText("");
    }
    private void createSoccerField(){
        SoccerField saveSoccer = new SoccerField();

        String name;
        double squareFootage;
        int goalCount;
        TurfType turfType;


        if (tex1.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole nazwy");
            return;
        }
        name = tex1.getText();

        if (tex2.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole metrów kwadratowych boiska");
            return;
        }
        try {
            squareFootage = Double.parseDouble(tex2.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }

        if (squareFootage <= 0){
            errorLabel.setText("Pole metrow kwadratowych boiska musi byc wieksza od 0");
            return;
        }

        if (tex3.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole bramek wbudowanych na stałe");
            return;
        }
        try {
            goalCount = Integer.parseInt(tex3.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }
        if (goalCount < 0){
            errorLabel.setText("Liczba bramek nie może być mniejsza od 0");
        }


        TurfType choiceTurf = turfTypeComboBox.getSelectionModel().getSelectedItem();
        if (choiceTurf == null){
            errorLabel.setText("Wybierz typ nawierzchni");
            return;
        }
        turfType = choiceTurf;

        saveSoccer.setName(name);
        saveSoccer.setSquareFootage(squareFootage);
        saveSoccer.setGoalCount(goalCount);
        saveSoccer.setTurfType(turfType);

        // Próba zapisu do bazy danych
        try {
            spaceService.createSpace(saveSoccer);
        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas zapisu do bazy danych: " + e.getMessage());
        }
        errorLabel.setText("");
    }
    private void createSwimmingPool(){
        SwimmingPool saveSwimmingPool = new SwimmingPool();

        String name;
        double poolLength;
        double poolDepth;
        int laneCount = 10;

        if (tex1.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole nazwy");
            return;
        }
        name = tex1.getText();

        if (tex2.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole długość basenu");
            return;
        }
        try {
            poolLength = Double.parseDouble(tex2.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }

        if (poolLength <= 0){
            errorLabel.setText("Długośc basenu musi być większa od 0");
            return;
        }

        if (tex3.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole głębokośc basenu");
            return;
        }
        try {
            poolDepth = Double.parseDouble(tex3.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }

        if (poolDepth <= 0){
            errorLabel.setText("Głebokość basenu musi być większa od 0");
            return;
        }


        if (tex4.getText().isBlank()){
            errorLabel.setText("Uzupelnij pole ilość torów");
            return;
        }
        try {
            laneCount = Integer.parseInt(tex4.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Nie poprawna liczba");
            return;
        }

        if (laneCount < 0){
            errorLabel.setText("Liczba torów nie może być mniejsza od 0");
            return;
        }

        saveSwimmingPool.setName(name);
        saveSwimmingPool.setPoolLength(poolLength);
        saveSwimmingPool.setPoolDepth(poolDepth);
        saveSwimmingPool.setLaneCount(laneCount);

        // Próba zapisu do bazy danych
        try {
            spaceService.createSpace(saveSwimmingPool);
        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas zapisu do bazy danych: " + e.getMessage());
        }
        errorLabel.setText("");
    }

    public void updateSpace(Long spaceID){
        Optional<Space> optionalSpace = spaceService.getSpace(spaceID);
        if (optionalSpace.isEmpty()) return;

        Space updateSpace = optionalSpace.get();

       if (updateSpace instanceof BasketballRoom){
           updateBasketballRoom(updateSpace.getId());
       }else if (updateSpace instanceof MartialArtsRoom){
           updateMariatlArtsRoom(updateSpace.getId());
       } else if (updateSpace instanceof MedicalRoom) {
           updateMedicalRoom(updateSpace.getId());
       } else if (updateSpace instanceof Room) {
           updateRoom(updateSpace.getId());
       } else if (updateSpace instanceof SoccerField) {
           updateSoccerField(updateSpace.getId());
       } else if (updateSpace instanceof SwimmingPool) {
           updateSwimmingPool(updateSpace.getId());
       }
    }

    private void updateRoom(Long spaceID){
        Room updateRoom = new Room();

        int capacity;
        double squareFootage;

        if (!tex1.getText().isBlank()){
            updateRoom.setName(tex1.getText());
        }

        if (!tex2.getText().isBlank()){

            try {
                capacity = Integer.parseInt(tex2.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba w polu pojemnosci");
                return;
            }
            if (capacity <= 0){
                errorLabel.setText("Liczba pojemnosci musi byc wieksza od 0");
                return;
            }
            updateRoom.setCapacity(capacity);
        }

        if (!tex3.getText().isBlank()){

            try {
                squareFootage = Double.parseDouble(tex3.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba metrow kwadratowych");
                return;
            }
            if (squareFootage <= 0){
                errorLabel.setText("Liczba metrow kwadratowych pomieszczenia musi byc wieksza od 0");
                return;
            }
            updateRoom.setSquareFootage(squareFootage);
        }

        spaceService.updateSpace(spaceID,updateRoom);

        errorLabel.setText("");
    }
    public void updateBasketballRoom(Long spaceID) {

        BasketballRoom updateBasketball = new BasketballRoom();
        int capacity;
        double squareFootage;
        int hoopCount;


        if (!tex1.getText().isBlank()){
            updateBasketball.setName(tex1.getText());
        }

        if (!tex2.getText().isBlank()){

            try {
                capacity = Integer.parseInt(tex2.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba w polu pojemnosci");
                return;
            }
            if (capacity <= 0){
                errorLabel.setText("Liczba pojemnosci musi byc wieksza od 0");
                return;
            }
            updateBasketball.setCapacity(capacity);
        }

        if (!tex3.getText().isBlank()){

            try {
                squareFootage = Double.parseDouble(tex3.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba metrow kwadratowych");
                return;
            }
            if (squareFootage <= 0){
                errorLabel.setText("Liczba metrow kwadratowych pomieszczenia musi byc wieksza od 0");
                return;
            }
            updateBasketball.setSquareFootage(squareFootage);
        }


        if (!tex4.getText().isBlank()){

            try {
                hoopCount = Integer.parseInt(tex4.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba w liczbie koszy");
                return;
            }
            if (hoopCount < 0){
                errorLabel.setText("Liczba koszy nie może być ujemna");
                return;
            }
            updateBasketball.setHoopCount(hoopCount);
        }

        spaceService.updateSpace(spaceID,updateBasketball);

        errorLabel.setText("");
    }
    private void updateMariatlArtsRoom(Long spaceID){

        MartialArtsRoom updateMartialArts = new MartialArtsRoom();

        int capacity;
        double squareFootage;
        int matCount;


        if (!tex1.getText().isBlank()){
            updateMartialArts.setName(tex1.getText());
        }

        if (!tex2.getText().isBlank()){

            try {
                capacity = Integer.parseInt(tex2.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba w polu pojemnosci");
                return;
            }
            if (capacity <= 0){
                errorLabel.setText("Liczba pojemnosci musi byc wieksza od 0");
                return;
            }
            updateMartialArts.setCapacity(capacity);
        }

        if (!tex3.getText().isBlank()){

            try {
                squareFootage = Double.parseDouble(tex3.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba metrow kwadratowych");
                return;
            }
            if (squareFootage <= 0){
                errorLabel.setText("Liczba metrow kwadratowych pomieszczenia musi byc wieksza od 0");
                return;
            }
            updateMartialArts.setSquareFootage(squareFootage);
        }


        if (!tex4.getText().isBlank()){

            try {
                matCount = Integer.parseInt(tex4.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba w liczbie mat");
                return;
            }
            if (matCount < 0){
                errorLabel.setText("Liczba mat nie może być ujemna");
                return;
            }
            updateMartialArts.setMatCount(matCount);
        }

        spaceService.updateSpace(spaceID,updateMartialArts);

        errorLabel.setText("");
    }
    private void updateSoccerField(Long spaceID){

        SoccerField updateSoccerField = new SoccerField();

        double squareFootage;
        int goalCount;
        TurfType turfType;


        if (!tex1.getText().isBlank()){
            updateSoccerField.setName(tex1.getText());
        }

        if (!tex2.getText().isBlank()){

            try {
                squareFootage = Double.parseDouble(tex2.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba w metrach kwadratowych boiska");
                return;
            }
            if (squareFootage <= 0){
                errorLabel.setText("Liczba metrow kwadratowych musi byc wieksza od 0");
                return;
            }
            updateSoccerField.setSquareFootage(squareFootage);
        }

        if (!tex3.getText().isBlank()){

            try {
                goalCount = Integer.parseInt(tex3.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba w bramkach");
                return;
            }
            if (goalCount < 0){
                errorLabel.setText("Liczba bramek nie moze byc ujemna");
                return;
            }

            updateSoccerField.setGoalCount(goalCount);
        }

        if (!turfTypeComboBox.getSelectionModel().isEmpty()){
            turfType = turfTypeComboBox.getSelectionModel().getSelectedItem();
            updateSoccerField.setTurfType(turfType);
        }


        spaceService.updateSpace(spaceID,updateSoccerField);

        errorLabel.setText("");
    }
    private void updateSwimmingPool(Long spaceID){

        SwimmingPool updateSwimmingPool = new SwimmingPool();

        double poolLength;
        double poolDepth;
        int laneCount;

        if (!tex1.getText().isBlank()){
            updateSwimmingPool.setName(tex1.getText());
        }

        if (!tex2.getText().isBlank()){

            try {
                poolLength = Double.parseDouble(tex2.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba w długości basenu");
                return;
            }
            if (poolLength <= 0){
                errorLabel.setText("Długośc basenu musi być większa od 0");
                return;
            }
            updateSwimmingPool.setPoolLength(poolLength);
        }

        if (!tex3.getText().isBlank()){

            try {
                poolDepth = Double.parseDouble(tex3.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba w głębokości basenu");
                return;
            }
            if (poolDepth <= 0){
                errorLabel.setText("Głębokość basenu musi byc wieksza od 0");
                return;
            }
            updateSwimmingPool.setPoolDepth(poolDepth);
        }

        if (!tex4.getText().isBlank()){

            try {
                laneCount = Integer.parseInt(tex4.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba w torach basenu");
                return;
            }
            if (laneCount < 0){
                errorLabel.setText("Liczba torów basenu nie może być ujemna");
                return;
            }
            updateSwimmingPool.setLaneCount(laneCount);
        }


        spaceService.updateSpace(spaceID,updateSwimmingPool);

        errorLabel.setText("");
    }
    private void updateMedicalRoom(Long spaceID){

        MedicalRoom updateMedical = new MedicalRoom();

        int capacity;
        double squareFootage;

        if (!tex1.getText().isBlank()){
            updateMedical.setName(tex1.getText());
        }

        if (!tex2.getText().isBlank()){

            try {
                capacity = Integer.parseInt(tex2.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba w polu pojemnosci");
                return;
            }
            if (capacity <= 0){
                errorLabel.setText("Liczba pojemnosci musi byc wieksza od 0");
                return;
            }
            updateMedical.setCapacity(capacity);
        }

        if (!tex3.getText().isBlank()){

            try {
                squareFootage = Double.parseDouble(tex3.getText());
            }catch (NumberFormatException e){
                errorLabel.setText("Niepoprawna liczba metrow kwadratowych");
                return;
            }
            if (squareFootage <= 0){
                errorLabel.setText("Liczba metrow kwadratowych pomieszczenia musi byc wieksza od 0");
                return;
            }
            updateMedical.setSquareFootage(squareFootage);
        }


      if (!comboCat.getSelectionModel().isEmpty()){
          String isSterile = comboCat.getSelectionModel().getSelectedItem();

          if(isSterile.equalsIgnoreCase("Tak")){
              updateMedical.setSterile(true);
          }else {
              updateMedical.setSterile(false);
          }
      }

        spaceService.updateSpace(spaceID,updateMedical);

      errorLabel.setText("");
    }
}