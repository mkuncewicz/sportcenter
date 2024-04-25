package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.entity.Employee;
import com.example.sportcenterv1.entity.Specialization;
import com.example.sportcenterv1.service.EmployeeService;
import com.example.sportcenterv1.service.EmployeeSpecializationService;
import com.example.sportcenterv1.service.SpecializationService;
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
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class EmployeeController {

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
    private TextField fieldPhone;

    @FXML
    private Label labelEmFirstName;

    @FXML
    private Label labelEmLastName;

    @FXML
    private Label labelEmPhone;

    @FXML
    private ComboBox<Specialization> selectEmployees;

    //Specjalizacje obsługa
    @FXML
    private ListView<Specialization> listViewSpecialization;

    private ObservableList<Specialization> specializationObservableList = FXCollections.observableArrayList();

    @FXML
    private TextField fieldSpecName;

    //Specjalizacje pracowników
    @FXML
    private ListView<Specialization> listViewSpecOfEmployee;

    private ObservableList<Specialization> specOfEmployeeObservableList = FXCollections.observableArrayList();


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
    public void initialize() {
       settingListEmployees();
       settingListSpec();
       settingListSpecOfEmployee();
       settingSelectionEmployees();
    }

    private void settingSelectionEmployees(){
        Specialization allOption = new Specialization();
        allOption.setId(null);
        allOption.setName("Wszyscy");

        specializationObservableList.add(0, allOption);
        selectEmployees.setItems(specializationObservableList);
        filterEmployees();
    }

    private void filterEmployees(){

        selectEmployees.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.getId() == null) {
                    // Jeśli ID jest null, pobierz wszystkich pracowników
                    List<Employee> allEmployees = employeeService.getAllEmployees();
                    employeeObservableList.setAll(allEmployees);
                } else {
                    // W przeciwnym razie, pobierz pracowników dla wybranej specjalizacji
                    Long specID = newValue.getId();
                    List<Employee> filterEmployeesList = employeeService.getEmployeesWithSpecialization(specID);
                    employeeObservableList.setAll(filterEmployeesList);
                }
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
                    labelEmFirstName.setText("IMIE: " + newValue.getFirstName());
                    labelEmLastName.setText("NAZWISKO: " + newValue.getLastName());
                    labelEmPhone.setText("NR TEL: " + newValue.getPhoneNumber());

                    List<Specialization> curSpecOfEmployee = newValue.getSpecializations().stream().toList();
                    specOfEmployeeObservableList.setAll(curSpecOfEmployee);
                }
            }
        });
    }

    private void updateList() {
        List<Employee> curEmployeeList = employeeService.getAllEmployees();
        employeeObservableList.setAll(curEmployeeList);

        List<Specialization> curSpecList = specializationService.getAllSpecializations();
        Specialization allOption = new Specialization();
        allOption.setId(null);  // null lub inna unikalna wartość
        allOption.setName("Wybierz wszystkich");  // Nazwa wyświetlana użytkownikowi

        // Dodawanie na początku listy
        specializationObservableList.add(0, allOption);
        specializationObservableList.setAll(curSpecList);
    }

    private void cleanFieldTextEmployee(){
        fieldFirstName.clear();
        fieldLastName.clear();
        fieldPhone.clear();
    }

    @FXML
    protected void addNewEmployee() {
        Employee createEmployee = new Employee();

        String firstName = fieldFirstName.getText();
        String lastName = fieldLastName.getText();
        String phone = fieldPhone.getText();

        if (!firstName.isBlank()) createEmployee.setFirstName(firstName);
        if (!lastName.isBlank()) createEmployee.setLastName(lastName);
        if (!phone.isBlank()) createEmployee.setPhoneNumber(phone);

        employeeService.createEmployee(createEmployee);
        updateList();
        cleanFieldTextEmployee();
    }

    @FXML
    protected void updateEmployee(){
        Employee selectedItem = listViewEmployees.getSelectionModel().getSelectedItem();

        Employee changeEmployee = new Employee();

        String firstName = fieldFirstName.getText();
        String lastName = fieldLastName.getText();
        String phoneNumber = fieldPhone.getText();

        if (!firstName.isBlank()) changeEmployee.setFirstName(firstName);
        if (!lastName.isBlank()) changeEmployee.setLastName(lastName);
        if (!phoneNumber.isBlank()) changeEmployee.setPhoneNumber(phoneNumber);

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

        employeeSpecService.addSpecializationToEmployee(optionEmployee.getId(), optionSpec.getId());

        Optional<Employee> optionalEmployee = employeeService.getEmployee(optionEmployee.getId());
        Employee curEmployee = optionalEmployee.get();

        specOfEmployeeObservableList.setAll(curEmployee.getSpecializations().stream().toList());


        updateList();
    }

    @FXML
    protected void addNewSpecialization(){
        Specialization saveSpecialization = new Specialization();
        String name = fieldSpecName.getText();

        saveSpecialization.setName(name);

        specializationService.createSpecialization(saveSpecialization);
        updateList();
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

        employeeSpecService.deleteSpecializationFromEmployee(employeeItem.getId(), specItem.getId());

        Employee newEmployee = employeeService.getEmployee(employeeItem.getId()).get();

        specOfEmployeeObservableList.setAll(newEmployee.getSpecializations().stream().toList());

        updateList();

    }
}
