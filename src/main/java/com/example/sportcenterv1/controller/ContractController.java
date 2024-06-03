package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.dm.DarkModeSingleton;
import com.example.sportcenterv1.entity.Contract;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.ContractStatusType;
import com.example.sportcenterv1.entity.enums.ContractType;
import com.example.sportcenterv1.service.ContractService;
import com.example.sportcenterv1.service.EmployeeService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class ContractController {

    //DarkMode
    @FXML
    private AnchorPane mainPane;

    private boolean isDarkMode = false;

    private DarkModeSingleton darkModeSingleton = DarkModeSingleton.getInstance();

    @Autowired
    private ApplicationContext springContext;

    //Pracownicy
    @Autowired
    private EmployeeService employeeService;

    private ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();

    @FXML
    private ListView<Employee> listViewEmployees;

    //Kontrakty
    @Autowired
    private ContractService contractService;

    private ObservableList<Contract> contractObservableList = FXCollections.observableArrayList();

    @FXML
    private ListView<Contract> listViewContracts;

    //Create
    @FXML
    private TextField textFieldSalary;

    @FXML
    private DatePicker datePickerDateStart;

    @FXML
    private DatePicker datePickerDateEnd;

    @FXML
    private ComboBox<ContractType> comboBoxContractType;

    private ObservableList<ContractType> contractTypeObservableList = FXCollections.observableArrayList();

    @FXML
    private ComboBox<ContractStatusType> comboBoxUpdate;

    private ObservableList<ContractStatusType> contractStatusObservableList = FXCollections.observableArrayList();
    //Labels
    @FXML
    private Label labelSalary;

    @FXML
    private Label labelDateStart;

    @FXML
    private Label labelDateEnd;

    @FXML
    private Label labelContractType;

    @FXML
    private Label labelContractStatusType;

    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox<ContractStatusType> comboxstatussearch;

    private ObservableList<ContractStatusType> statusSearchObservableList = FXCollections.observableArrayList();

    @FXML
    private TextField textfieldsearch;

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
    private void setDarkMode(){
        String darkMode = getClass().getResource("/css/DMcontractManager.css").toExternalForm();

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

        String darkMode = getClass().getResource("/css/DMcontractManager.css").toExternalForm();

        if (isDarkMode){
            mainPane.getStylesheets().add(darkMode);
        }else {
            mainPane.getStylesheets().remove(darkMode);
        }
    }

    @FXML
    protected void initialize(){
        //DarkMode
        isDarkMode = darkModeSingleton.isDarkMode();
        checkActiveDarkMode();

        contractService.updateStatus();
        uploadContractList(-1L);
        setEmployeeObservableList();
        setContractView();
        setContractTypeList();
        setContractStatus();

        setStatusSearchObservableList();
        listenerToComboxStatusSearch();

        listenerToTextFieldSearch();


        comboxstatussearch.setItems(statusSearchObservableList);

        listViewEmployees.setItems(employeeObservableList);
        listViewContracts.setItems(contractObservableList);
        comboBoxContractType.setItems(contractTypeObservableList);
        comboBoxUpdate.setItems(contractStatusObservableList);

    }

    private void setEmployeeObservableList(){
        employeeObservableList.setAll(employeeService.getAllEmployees());

        listViewEmployees.setCellFactory(employeeListView -> new ListCell<>(){

            @Override
            protected void updateItem(Employee employee, boolean b) {
                super.updateItem(employee, b);
                if (b || employee == null){
                    setText(null);
                }else {
                    setText(employee.getFirstName() + " " + employee.getLastName());
                }
            }
        });
    }

    private void setContractView(){
        listenerToEmployee();
        listenerToContract();

        listViewContracts.setCellFactory(contract -> new ListCell<>(){

            @Override
            protected void updateItem(Contract contract, boolean b) {
                super.updateItem(contract, b);

                if(b || contract == null){
                    setText(null);
                }else {
                    setText(contract.getDateStart() + " - " + contract.getDateEnd() + ", " + contract.getContractStatusType().getDisplayName());
                }
            }
        });
    }

    private void setContractTypeList(){

        List<ContractType> contractTypeList = List.of(ContractType.EMPLOYMENT_CONTRACT,ContractType.MANDATE_CONTRACT,ContractType.SPECIFIC_TASK_CONTRACT,ContractType.INTERNSHIP,ContractType.B2B_CONTRACT);

        contractTypeObservableList.setAll(contractTypeList);
    }

    private void setContractStatus(){

        List<ContractStatusType> contractStatusTypeList = List.of(ContractStatusType.PENDING,ContractStatusType.REJECTED,ContractStatusType.CONFIRMED);

        contractStatusObservableList.setAll(contractStatusTypeList);
    }

    private void setStatusSearchObservableList(){

        List<ContractStatusType> list = new ArrayList<>(Arrays.asList(
                ContractStatusType.NEW,
                ContractStatusType.PENDING,
                ContractStatusType.REJECTED,
                ContractStatusType.CONFIRMED,
                ContractStatusType.IN_PROGRESS,
                ContractStatusType.COMPLETED,
                ContractStatusType.EXPIRING
        ));

        list.add(0,null);

        statusSearchObservableList.setAll(list);
    }
    private void listenerToEmployee(){

        listViewEmployees.getSelectionModel().selectedItemProperty().addListener((observableValue, employee, t1) -> {

            if (t1 != null){
                List<Contract> newList = contractService.getAllContractByEmployee(t1);
                newList.sort(Comparator.comparing(Contract::getDateStart).reversed());
                contractObservableList.setAll(newList);
                errorLabel.setText("");

                comboxstatussearch.setPromptText("Wszystkie");
                comboxstatussearch.setValue(null);
            }
        });
    }

    private void listenerToContract(){

        listViewContracts.getSelectionModel().selectedItemProperty().addListener((observableValue, contract, t1) -> {

            setLabels(t1);
            errorLabel.setText("");
        });
    }

    private void listenerToComboxStatusSearch(){


        comboxstatussearch.getSelectionModel().selectedItemProperty().addListener((observableValue, contractStatusType, t1) -> {

            List<Contract> list = contractService.getAllContractByEmployeeAndStatusType(listViewEmployees.getSelectionModel().getSelectedItem(),t1);
            list.sort(Comparator.comparing(Contract::getDateStart).reversed());

            contractObservableList.setAll(list);
        });
    }

    private void listenerToTextFieldSearch(){

        textfieldsearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String searchName = "";
                searchName = textfieldsearch.getText();

                List<Employee> employeeList = employeeService.getAllEmployeesByName(searchName);
                employeeObservableList.setAll(employeeList);
            }
        });
    }

    @FXML
    private void createContract(){

        Contract createContract = new Contract();

        double salary = -1;

        try {
             salary = Double.parseDouble(textFieldSalary.getText());
        }catch (NumberFormatException e){
            System.out.println("Zły format liczb");
            errorLabel.setText("Zły format liczb");
            return;
        }

        ContractType contractType = comboBoxContractType.getSelectionModel().getSelectedItem();

        LocalDate startLocalDate = datePickerDateStart.getValue();
        Date startDate = startLocalDate != null ? Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

        LocalDate endLocalDate = datePickerDateEnd.getValue();
        Date endDate = endLocalDate != null ? Date.from(endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

        if (salary == -1 || salary <= 0 || contractType == null || startDate == null || endDate == null){
            System.out.println("Nie ustawiono wszystkich pól lub złe dane");
            errorLabel.setText("Nie ustawiono wszystkich pól lub złe dane");
            return;
        }

        if (endDate.getTime() <= startDate.getTime()){
            System.out.println("Niepoprawnie podana data");
            errorLabel.setText("Niepoprawnie podana data");
            return;
        }

        createContract.setSalary(salary);
        createContract.setDateStart(startDate);
        createContract.setDateEnd(endDate);
        createContract.setContractType(contractType);
        createContract.setContractStatusType(ContractStatusType.NEW);

        System.out.println(createContract);

        Employee employee = listViewEmployees.getSelectionModel().getSelectedItem();

       if (employee == null){
           System.out.println("Nie wybrano pracownika");
           errorLabel.setText("Nie wybrano pracownika");
           return;
       }

       employeeService.addContractToEmployee(employee.getId(), createContract);

        //Pobranie obecnej listy kontaktow dla pracownika
        uploadContractList(employee.getId());

        //Resetowanie pol
        resetCreateFields();
    }

    @FXML
    private void updateContract(){

        Long contractID;

        try {
            contractID = listViewContracts.getSelectionModel().getSelectedItem().getId();
        }catch (Exception e){
            System.out.println("Nie wybrano kontrakt");
            errorLabel.setText("Nie wybrano kontraktu");
            return;
        }

        Optional<Contract> optionalContract = contractService.getContract(contractID);
        Contract dbContract = null;


        //Sprawdzenie czy kontakt istnieje
        if (optionalContract.isEmpty()){
            System.out.println("Kontrakt nie istnieje w bazie danych");
            errorLabel.setText("Kontrakt nie istnieje w bazie danych");
            return;
        }else {
            dbContract = optionalContract.get();
        }

        if (dbContract == null){
            System.out.println("Zle pobrano kontakt");
            errorLabel.setText("Źle pobrano kontrakt");
            return;
        }



        if (contractID == null) {
            System.out.println("Wybierz kontakt");
            errorLabel.setText("Wybierz kontrakt");
            return;
        }

        Contract updateContract = new Contract();

        double salary = 0;

        //Sprawdzenie poprawności danych liczbowych
        if (!textFieldSalary.getText().isBlank()) {
            try {
                salary = Double.parseDouble(textFieldSalary.getText());
            } catch (NumberFormatException e) {
                System.out.println("Zły format liczb");
                errorLabel.setText("Zły format liczb");
                return;
            }
        }

        ContractType contractType = comboBoxContractType.getSelectionModel().getSelectedItem();

        LocalDate startLocalDate = datePickerDateStart.getValue();
        Date startDate = startLocalDate != null ? Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

        LocalDate endLocalDate = datePickerDateEnd.getValue();
        Date endDate = endLocalDate != null ? Date.from(endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

        //Sprawdzenie poprawności dat w 3 przypadkach:
        //(kiedy chcemy zaktualizowac obie daty), (kiedy chcemy zaktualizowac start kontraktu), (kiedy chcemy zaktualizować koniec kontraktu)
        //Data startu kontraktu nie może być później niż jego koniec.
        if (startDate != null && endDate != null) {
            if (endDate.getTime() <= startDate.getTime()) {
                System.out.println("Niepoprawnie podana data");
                errorLabel.setText("Niepoprawnie podana data");
                return;
            }else {
                updateContract.setDateStart(startDate);
                updateContract.setDateEnd(endDate);
            }
        } else if (startDate != null && endDate == null){

            if (dbContract.getDateEnd().getTime() <= startDate.getTime()) {
                System.out.println("Niepoprawnie podana data");
                errorLabel.setText("Niepoprawnie podana data");
                return;
            }else {
                updateContract.setDateStart(startDate);
            }
        } else if (startDate == null && endDate != null){
            if (endDate.getTime() <= dbContract.getDateStart().getTime()) {
                System.out.println("Niepoprawnie podana data");
                errorLabel.setText("Niepoprawnie podana data");
                return;
            }else {
                updateContract.setDateEnd(endDate);
            }
        }

        if (salary >= 0 ) updateContract.setSalary(salary);

        if (contractType != null) updateContract.setContractType(contractType);

        contractService.updateContract(contractID, updateContract);


        //Pobranie obecnej listy kontaktow dla pracownika
        Long employeeID = listViewEmployees.getSelectionModel().getSelectedItem().getId();
        uploadContractList(employeeID);

        //Resetowanie pol
        resetCreateFields();
    }

    @FXML
    private void deleteContract(){
        if (listViewEmployees.getSelectionModel().isEmpty()){
            System.out.println("Wybierz jeszcze raz pracownika");
            errorLabel.setText("Wybierz pracownika");
            return;
        }

        Long employeeID;
        Long contractID;

        try {
            employeeID = listViewEmployees.getSelectionModel().getSelectedItem().getId();
        }catch (Exception e){
            System.out.println("Wybierz pracownika");
            errorLabel.setText("Wybierz pracownika");
            return;
        }

        try {
            contractID = listViewContracts.getSelectionModel().getSelectedItem().getId();
        }catch (Exception e){
            System.out.println("Wybierz kontrakt");
            errorLabel.setText("Wybierz kontrakt");
            return;
        }

        employeeService.removeContract(employeeID,contractID);

       //Pobranie obecnej listy kontaktow dla pracownika
       uploadContractList(employeeID);
    }

    @FXML
    private void updateStatus(){



        Long contraID;
        ContractStatusType contractStatusType = comboBoxUpdate.getSelectionModel().getSelectedItem();

        try {
            contraID = listViewContracts.getSelectionModel().getSelectedItem().getId();
        }catch (Exception e){
            System.out.println("Nie wybrano kontraktu");
            errorLabel.setText("Nie wybrano kontraktu");
            return;
        }

        if (contractStatusType == null){
            System.out.println("Nie wybrano statusu");
            errorLabel.setText("Nie wybrano statusu");
            return;
        }
        boolean result = contractService.updateStatus(contraID,contractStatusType);



        Long employeeID = null;
        if (listViewEmployees.getSelectionModel().isEmpty()){
            uploadContractList(null);
        }else {
            employeeID = listViewEmployees.getSelectionModel().getSelectedItem().getId();
            uploadContractList(employeeID);
        }
        System.out.println("Wykonało się");

        if (!result){
            System.out.println("Nie można zaktualizować statusu dla wybranego kontraktu");
            errorLabel.setText("Nie można zaktualizować statusu dla wybranego kontraktu");
        }
    }

    private void uploadContractList(Long employeeID){
        Optional<Employee> optionalEmployee = employeeService.getEmployee(employeeID);
        List<Contract> newList;

        //Sprawdzenie czy pracownik jest wybrany, jezeli nie zwraca pusta liste
        if (optionalEmployee.isPresent()) {
            Employee dbEmployee = optionalEmployee.get();
            newList = contractService.getAllContractByEmployee(dbEmployee);
        }else {
            newList = new ArrayList<>();
        }
        newList.sort(Comparator.comparing(Contract::getDateStart).reversed());
        contractObservableList.setAll(newList);
    }

    private void resetCreateFields(){

        textFieldSalary.setText(null);
        datePickerDateStart.setValue(null);
        datePickerDateEnd.setValue(null);
        comboBoxContractType.getSelectionModel().clearSelection();
    }

    private void setLabels(Contract contract){

        if (contract == null){
            labelSalary.setText("Wynagrodzenie: ");
            labelDateStart.setText("Rozpoczęcie: ");
            labelDateEnd.setText("Zakończenie: ");
            labelContractType.setText("Typ umowy: ");
            labelContractStatusType.setText("Status: ");
        }else {
            labelSalary.setText("Wynagrodzenie: " + contract.getSalary() + "zł (brutto)");
            labelDateStart.setText("Rozpoczęcie: " + contract.getDateStart());
            labelDateEnd.setText("Zakończenie: " + contract.getDateEnd());
            labelContractType.setText("Typ umowy: " + contract.getContractType());
            labelContractStatusType.setText("Status: " + contract.getContractStatusType());
        }
    }
}
