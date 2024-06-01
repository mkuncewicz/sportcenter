package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.entity.Address;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.service.EmployeeService;
import com.example.sportcenterv1.service.EmployeeSpecializationService;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class EmployeeController {

    @FXML
    private AnchorPane mainPane;
    private boolean isDarkMode = false;
    @Autowired
    private ApplicationContext springContext;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SpecializationService specializationService;

    @Autowired
    private EmployeeSpecializationService employeeSpecService;

    //Pracownicy obsługa
    @FXML
    private ListView<Employee> listViewEmployees;

    private ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();

    @FXML
    private TextField fieldFirstName;

    @FXML
    private TextField fieldLastName;

    @FXML
    private DatePicker datePickerBirth;

    @FXML
    private TextField fieldPhone;

    @FXML
    private TextField fieldCity;

    @FXML
    private TextField fieldStreet;

    @FXML
    private TextField textFieldBuildingNumber;

    @FXML
    private TextField textFieldApartmentNumber;


    @FXML
    private Label labelEmFirstName;

    @FXML
    private Label labelEmLastName;

    @FXML
    private Label labelEmBirth;

    @FXML
    private Label labelEmPhone;

    @FXML
    private Label labelEmCity;

    @FXML
    private Label labelEmStreet;

    @FXML
    private Label labelEmBuildingNumber;

    @FXML
    private Label labelEmApartmentNumber;

    @FXML
    private Label labelEmCurContracts;

    @FXML
    private Label labelEmDateOfEndContract1;

    @FXML
    private Label labelEmDateOfEndContract2;


    @FXML
    private ComboBox<Specialization> selectEmployees;

    //Specjalizacje obsługa
    @FXML
    private ListView<Specialization> listViewSpecialization;

    private ObservableList<Specialization> specializationObservableList = FXCollections.observableArrayList();

    private ObservableList<Specialization> selectedSpecializationObservableList = FXCollections.observableArrayList();

    @FXML
    private TextField fieldSpecName;

    //Specjalizacje pracowników
    @FXML
    private ListView<Specialization> listViewSpecOfEmployee;

    private ObservableList<Specialization> specOfEmployeeObservableList = FXCollections.observableArrayList();

    @FXML
    private VBox vboxSpecOfEmployee;


    //Errors
    @FXML
    private Label errorLabel1;

    @FXML
    private Label errorLabel2;

    //
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
        String darkMode = getClass().getResource("/css/DMemployeeManagerStyle.css").toExternalForm();

        if (isDarkMode){
            mainPane.getStylesheets().remove(darkMode);
            isDarkMode = false;
        }else {
            mainPane.getStylesheets().add(darkMode);
            isDarkMode = true;
        }
    }

    @FXML
    public void initialize() {
       settingListEmployees();
       settingListSpec();
       settingListSpecOfEmployee();
       settingSelectionEmployees();
       vboxSpecOfEmployee.setVisible(false);

       listenerToTextFieldSearch();
    }

    private void settingSelectionEmployees(){
        Specialization allOption = new Specialization();
        allOption.setId(null);
        allOption.setName("Wszyscy");

        selectedSpecializationObservableList.setAll(specializationObservableList);
        selectedSpecializationObservableList.add(0,null);

        selectEmployees.setItems(selectedSpecializationObservableList);

        filterEmployees();
    }

    private void filterEmployees(){

        selectEmployees.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

                if (newValue == null) {
                    // Jeśli ID jest null, pobierz wszystkich pracowników
                    List<Employee> allEmployees = employeeService.getAllEmployees();
                    employeeObservableList.setAll(allEmployees);
                } else {
                    // W przeciwnym razie, pobierz pracowników dla wybranej specjalizacji
                    Long specID = newValue.getId();
                    List<Employee> filterEmployeesList = employeeService.getEmployeesWithSpecialization(specID);
                    employeeObservableList.setAll(filterEmployeesList);
                }
        });
    }
    private void settingListEmployees(){
        listViewEmployees.setCellFactory(employee -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFirstName() + " " + item.getLastName()); // Możesz tutaj wyświetlić dowolną właściwość obiektu Client
                }
            }
        });
        updateList();
        listViewEmployees.setItems(employeeObservableList);
        listenOnChoiceEmployee();
    }

    private void settingListSpec(){
        listViewSpecialization.setCellFactory(specializationListView -> new ListCell<Specialization>(){
            @Override
            protected void updateItem(Specialization item, boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        listViewSpecialization.setItems(specializationObservableList);
        updateList();
    }

    private void settingListSpecOfEmployee(){
        listViewSpecOfEmployee.setCellFactory(specializationListView -> new ListCell<Specialization>(){

            @Override
            protected void updateItem(Specialization item, boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        updateList();
        listViewSpecOfEmployee.setItems(specOfEmployeeObservableList);
    }


    private void listenOnChoiceEmployee(){
        listViewEmployees.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Employee>() {
            @Override
            public void changed(ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) {
                if (newValue != null) {
                    // Wykonaj akcję, gdy wybrano nowego klienta
                    setLabelsForEmployee(newValue);

                    List<Specialization> curSpecOfEmployee = newValue.getSpecializations().stream().toList();
                    specOfEmployeeObservableList.setAll(curSpecOfEmployee);

                    vboxSpecOfEmployee.setVisible(true);
                }
            }
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

    private void updateList() {
        List<Employee> curEmployeeList = employeeService.getAllEmployees();
        employeeObservableList.setAll(curEmployeeList);

        List<Specialization> curSpecList = specializationService.getAllSpecializations();

        // Dodawanie na początku listy
        specializationObservableList.setAll(curSpecList);
        selectedSpecializationObservableList.setAll(specializationObservableList);
        selectedSpecializationObservableList.add(0,null);
        selectEmployees.getSelectionModel().clearSelection(); //bk
    }

    private void cleanFieldTextEmployee(){
        fieldFirstName.clear();
        fieldLastName.clear();
        fieldPhone.clear();
    }

    @FXML
    protected void addNewEmployee() {
        Employee createEmployee = new Employee();
        Address newAdress = new Address();

        String firstName = fieldFirstName.getText();
        String lastName = fieldLastName.getText();

        LocalDate localDate = datePickerBirth.getValue();
        Date birth = localDate != null ? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

        String phone = fieldPhone.getText();
        String city = fieldCity.getText();
        String street = fieldStreet.getText();
        String buildingNumber = textFieldBuildingNumber.getText();
        String apartmentNumber = textFieldApartmentNumber.getText();

        if (firstName.isBlank()){
            errorLabel1.setText("Podaj imie");
            return;
        }
        if(lastName.isBlank()){
            errorLabel1.setText("Podaj nazwisko");
            return;
        }
        if (phone.isBlank() || phone.length() < 9){
            errorLabel1.setText("Podaj numer/Niepoprawny numer");
            return;
        }
        if (birth == null){
            errorLabel1.setText("Zła data urodzenia");
            return;
        }

        createEmployee.setFirstName(firstName);
        createEmployee.setLastName(lastName);
        createEmployee.setPhoneNumber(phone);
        createEmployee.setDateOfBirth(birth);

        if (city.isBlank()){
            errorLabel1.setText("Nie podano miasta");
            return;
        }

        if (street.isBlank()){
            errorLabel1.setText("Nie podano ulicy");
            return;
        }

        if (buildingNumber.isBlank()){
            errorLabel1.setText("Nie podano nr domu");
            return;
        }

        newAdress.setCity(city);
        newAdress.setStreet(street);
        newAdress.setBuildingNumber(buildingNumber);

        if (apartmentNumber.isBlank()){
            newAdress.setApartmentNumber("Brak");
        }else newAdress.setApartmentNumber(apartmentNumber);

        createEmployee.setAddress(newAdress);

        employeeService.createEmployee(createEmployee);

        updateList();
        cleanFieldTextEmployee();

        errorLabel1.setText(null);
    }

    @FXML
    protected void updateEmployee(){
        Employee selectedItem = listViewEmployees.getSelectionModel().getSelectedItem();

        Employee changeEmployee = new Employee();

        String firstName = fieldFirstName.getText();
        String lastName = fieldLastName.getText();
        String phoneNumber = fieldPhone.getText();

        LocalDate localDate = datePickerBirth.getValue();
        Date birth = localDate != null ? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

        String city = fieldCity.getText();
        String street = fieldStreet.getText();
        String buildingNumber = textFieldBuildingNumber.getText();
        String apartmentNumber = textFieldApartmentNumber.getText();

        if (!firstName.isBlank()) changeEmployee.setFirstName(firstName);
        if (!lastName.isBlank()) changeEmployee.setLastName(lastName);
        if (!phoneNumber.isBlank() && phoneNumber.length() >= 9) changeEmployee.setPhoneNumber(phoneNumber);
        if (birth != null) changeEmployee.setDateOfBirth(birth);

        Address updateAddress = new Address();

        if (!city.isBlank()) updateAddress.setCity(city);
        if (!street.isBlank()) updateAddress.setStreet(street);
        if (!buildingNumber.isBlank()) updateAddress.setBuildingNumber(buildingNumber);
        if (!apartmentNumber.isBlank()) updateAddress.setApartmentNumber(apartmentNumber);

        changeEmployee.setAddress(updateAddress);

        employeeService.updateEmployee(selectedItem.getId(), changeEmployee);

        updateList();
        cleanFieldTextEmployee();
    }

    @FXML
    protected void deleteEmployee(){
        Employee selectedItem = listViewEmployees.getSelectionModel().getSelectedItem();

        employeeService.deleteEmployee(selectedItem.getId());
        updateList();
    }

    @FXML
    protected void addSpecToEmployee(){
        Employee optionEmployee = listViewEmployees.getSelectionModel().getSelectedItem();
        Specialization optionSpec = listViewSpecialization.getSelectionModel().getSelectedItem();

        if (optionEmployee == null){
            errorLabel2.setText("Nie wybrano pracownika");
            return;
        }
        if (optionSpec == null){
            errorLabel2.setText("Nie wybrano specializacji");
            return;
        }


        employeeSpecService.addSpecializationToEmployee(optionEmployee.getId(), optionSpec.getId());

        Optional<Employee> optionalEmployee = employeeService.getEmployee(optionEmployee.getId());
        Employee curEmployee = optionalEmployee.get();

        specOfEmployeeObservableList.setAll(curEmployee.getSpecializations().stream().toList());


        updateList();
        errorLabel2.setText(null);
    }

    @FXML
    protected void addNewSpecialization(){
        Specialization saveSpecialization = new Specialization();
        String name = fieldSpecName.getText();

        if(name == null){
           if (name.isBlank()){
            errorLabel2.setText("Podaj nazwe dla specializacji");
            return;
        }
        }

        saveSpecialization.setName(name);

        specializationService.createSpecialization(saveSpecialization);
        updateList();
        errorLabel2.setText(null);
        fieldSpecName.setText(null);
    }

    @FXML
    protected void updateSpecialization(){

        Specialization specialization = listViewSpecialization.getSelectionModel().getSelectedItem();
        Specialization updateSpec = new Specialization();

        if (specialization == null){
            errorLabel2.setText("Wybierz specializacje");
            return;
        }

        String name = fieldSpecName.getText();

        if (name.isBlank() || name == null){
            System.out.println("Wpisz nazwe do zmiany");
            return;
        }
        updateSpec.setName(name);

        specializationService.updateSpecialization(specialization.getId(),updateSpec);
        updateList();
        errorLabel2.setText(null);
        fieldSpecName.setText(null);
    }

    @FXML
    protected void deleteSpecialization(){
        Specialization specialization = listViewSpecialization.getSelectionModel().getSelectedItem();

        specializationService.deleteSpecialization(specialization.getId());
        updateList();
    }

    @FXML
    protected void deleteSpecializationOfEmployee(){
        Employee employeeItem = listViewEmployees.getSelectionModel().getSelectedItem();
        Specialization specItem = listViewSpecOfEmployee.getSelectionModel().getSelectedItem();

        if (employeeItem == null){
            errorLabel1.setText("Wybierz pracownika");
            return;
        }

        if(specItem == null){
            errorLabel1.setText("Wybierz specjalizacje");
            return;
        }

        employeeSpecService.deleteSpecializationFromEmployee(employeeItem.getId(), specItem.getId());

        Employee newEmployee = employeeService.getEmployee(employeeItem.getId()).get();

        specOfEmployeeObservableList.setAll(newEmployee.getSpecializations().stream().toList());

        updateList();
        errorLabel1.setText(null);
    }

    private void setLabelsForEmployee(Employee employee){
        int curContracts = employeeService.getCountOfContracts(employee);
        Date dateOfEndActiveContract = employeeService.getDateToLastActiveContract(employee);

        labelEmFirstName.setText("Imie: " + employee.getFirstName());
        labelEmLastName.setText("Nazwisko: " + employee.getLastName());
        labelEmBirth.setText("Data urodzenia: " + employee.getDateOfBirth());
        labelEmPhone.setText("NR TEL: " + employee.getPhoneNumber());
        labelEmCity.setText("Miasto: " + employee.getAddress().getCity());
        labelEmStreet.setText("Ulica: " + employee.getAddress().getStreet());
        labelEmBuildingNumber.setText("Numer budynku: " + employee.getAddress().getBuildingNumber());
        labelEmApartmentNumber.setText("Numer mieszkania: " + employee.getAddress().getApartmentNumber());

        labelEmCurContracts.setText("Aktualne kontrakty: " + curContracts);
        if (dateOfEndActiveContract != null) labelEmDateOfEndContract2.setText(dateOfEndActiveContract+"");
        else labelEmDateOfEndContract2.setText("Brak");
    }

    @FXML
    private void clearDatePicker(){
        datePickerBirth.setValue(null);
    }
}
