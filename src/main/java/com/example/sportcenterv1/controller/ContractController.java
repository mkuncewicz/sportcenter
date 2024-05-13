package com.example.sportcenterv1.controller;

import com.example.sportcenterv1.entity.Contract;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.ContractStatusType;
import com.example.sportcenterv1.entity.enums.ContractType;
import com.example.sportcenterv1.service.ContractService;
import com.example.sportcenterv1.service.EmployeeService;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class ContractController {

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
    protected void initialize(){
        //Pracownicy
        setListViewEmployees();
        employeeObservableList.setAll(employeeService.getAllEmployees());

        //Typy kontraktow
        setContractStatusList();
        comboBoxContractType.setItems(contractTypeObservableList);


        //Kontrakty
        setListViewContracts();
        listViewContracts.setItems(contractObservableList);

        setListenToEmployeeListView();
        setListenToContractListView();
    }

    private void setListViewEmployees(){
        listViewEmployees.setItems(employeeObservableList);

        listViewEmployees.setCellFactory(employee -> new ListCell<Employee>(){

            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null){
                    setText(null);
                }else {
                    setText(item.getFirstName() + " " + item.getLastName());
                }
            }
        });
    }

    private void setListViewContracts(){

        listViewContracts.setCellFactory(contract -> new ListCell<Contract>(){

            @Override
            protected void updateItem(Contract item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null){
                    setText(null);
                }else {
                    setText(item.getDateEnd()+", "+item.getContractStatusType().getDisplayName());
                }
            }
        });
    }

    private void setContractStatusList(){
        List<ContractType> list = List.of(ContractType.EMPLOYMENT_CONTRACT,ContractType.MANDATE_CONTRACT,ContractType.SPECIFIC_TASK_CONTRACT,ContractType.INTERNSHIP,ContractType.B2B_CONTRACT);

        contractTypeObservableList.setAll(list);
    }

    private void setListenToEmployeeListView() {

        listViewEmployees.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Employee>() {
            @Override
            public void changed(ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) {
                if (newValue != null) {
                    List<Contract> contractList = newValue.getContracts().stream().toList();
                    contractObservableList.setAll(contractList);
                } else {
                    contractObservableList.setAll(new ArrayList<>());
                }
            }
        });
    }

    private void setListenToContractListView(){

        listViewContracts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contract>() {

            @Override
            public void changed(ObservableValue<? extends Contract> observableValue, Contract contract, Contract newValue) {
                if (newValue != null){
                    setLabels(newValue);
                }else {
                    clearLabels();
                }
            }
        });

    }

    private void setLabels(Contract contract){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDateStart = formatter.format(contract.getDateStart());
        String formattedDateEnd = formatter.format(contract.getDateEnd());

        labelSalary.setText("Wynagrodzenie: " + contract.getSalary() + "zl (brutto)");
        labelDateStart.setText("Rozpoczęcie: " + formattedDateStart);
        labelDateEnd.setText("Zakończenie: " + formattedDateEnd);
        labelContractType.setText("Typ umowy: " + contract.getContractType().getDisplayName());
        labelContractStatusType.setText("Status: " + contract.getContractStatusType().getDisplayName());

    }

    private void clearLabels(){

        labelSalary.setText("Wynagrodzenie: ");
        labelDateStart.setText("Rozpoczęcie: ");
        labelDateEnd.setText("Zakończenie: ");
        labelContractType.setText("Typ umowy");
        labelContractStatusType.setText("Status: ");
    }
    @FXML
    private void createContract(){
        Employee employee = listViewEmployees.getSelectionModel().getSelectedItem();

        if (employee != null){

            Optional<Employee> optionalEmployee = employeeService.getEmployee(employee.getId());

            if (optionalEmployee.isPresent()){

                Employee curEmployee = optionalEmployee.get();
                Contract contract = new Contract();


            double salary = Double.parseDouble(textFieldSalary.getText());

            LocalDate localDateStart = datePickerDateStart.getValue();
            Date start = localDateStart != null ? Date.from(localDateStart.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

            LocalDate localDateEnd = datePickerDateEnd.getValue();
            Date end = localDateEnd != null ? Date.from(localDateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

            ContractType contractType = comboBoxContractType.getSelectionModel().getSelectedItem();

            if (salary < 0 || start == null || end == null || contractType == null){
                System.out.println("Zle dane");
            }else {
                contract.setSalary(salary);
                contract.setDateStart(start);
                contract.setDateEnd(end);
                contract.setContractType(contractType);

                employeeService.addContractToEmployee(curEmployee.getId(), contract);
                updateListContracts(curEmployee.getId());
            }
            }
        }
    }

    @FXML
    private void deleteContract(){
        Contract contract = listViewContracts.getSelectionModel().getSelectedItem();

        if (contract != null){
            Long employeeID = contract.getEmployee().getId();
            employeeService.removeContract(employeeID,contract.getId());

            updateListContracts(employeeID);
        }
    }

    private void updateListContracts(Long employeeID){

        Optional<Employee> optionalEmployee = employeeService.getEmployee(employeeID);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            contractObservableList.setAll(employee.getContracts());
        }
    }
}
