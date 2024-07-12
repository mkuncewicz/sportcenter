package com.example.sportcenterv1.controllerhelp;

import com.example.sportcenterv1.entity.Client;
import com.example.sportcenterv1.entity.Offer;
import com.example.sportcenterv1.entity.Reservation;
import com.example.sportcenterv1.entity.employee.Employee;
import com.example.sportcenterv1.entity.enums.OfferType;
import com.example.sportcenterv1.entity.enums.ReservationStatus;
import com.example.sportcenterv1.entity.space.Space;
import com.example.sportcenterv1.service.ClientService;
import com.example.sportcenterv1.service.OfferService;
import com.example.sportcenterv1.service.ReservationService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class ReservationComponent {


    private final ReservationService reservationService;
    private  Label errorLabel;

    private final ClientService clientService;

    private final OfferService offerService;

    private final DateGenerator dateGenerator;

    private final ComboBox<ReservationStatus> comboboxStatusReservation;

    private final ComboBox<Employee> comboboxEmployee;

    private final ComboBox<Space> comboboxSpace;

    public ReservationComponent(ReservationService reservationService, Label errorLabel, ClientService clientService, OfferService offerService, DateGenerator dateGenerator, ComboBox<ReservationStatus> comboboxStatusReservation, ComboBox<Employee> comboboxEmployee, ComboBox<Space> comboboxSpace) {
        this.reservationService = reservationService;
        this.errorLabel = errorLabel;
        this.clientService = clientService;
        this.offerService = offerService;
        this.dateGenerator = dateGenerator;
        this.comboboxStatusReservation = comboboxStatusReservation;
        this.comboboxEmployee = comboboxEmployee;
        this.comboboxSpace = comboboxSpace;
    }

    public boolean createReservation(OfferType offerType, Client client, Offer offer){

        boolean isCreated = false;

        if (offerType == OfferType.ONE_TIME){
            isCreated = createReservationForOneTime(client, offer);
        }else {
            isCreated = createReservationForSubscription(client, offer);
        }

        return  isCreated;
    }

    public boolean updateReservation(Reservation selectedReservation, Client client, Offer offer){

        boolean isUpdate = false;

        OfferType offerTypeFromSelectedReservaion = selectedReservation.getOffer().getOfferType();

        if (selectedReservation == null) {
            errorLabel.setText("Wybierz rezerwację do aktualizacji");
            return false;
        }

        // Pobierz wartości, które użytkownik wprowadził (jeśli w ogóle)
        LocalDateTime localDateTime = dateGenerator.getLocalDateTime();
        ReservationStatus reservationStatus = comboboxStatusReservation.getValue();
        Client selectedClient = client;
        Offer selectedOffer = offer;

        Employee selectedEmployee = comboboxEmployee.getSelectionModel().getSelectedItem();
        Space selectedSpace = comboboxSpace.getSelectionModel().getSelectedItem();


        // Aktualizuj tylko te pola, które zostały zmienione przez użytkownika
        if (localDateTime != null) {
            selectedReservation.setDate(localDateTime);
        }

        if (reservationStatus != null) {
            selectedReservation.setReservationStatus(reservationStatus);
        }

        if (selectedClient != null) {
            selectedReservation.setClient(selectedClient);
        }

        if (selectedOffer != null) {
            selectedReservation.setOffer(selectedOffer);
        }

        if (selectedEmployee != null && offerTypeFromSelectedReservaion == OfferType.ONE_TIME) {
            selectedReservation.setEmployee(selectedEmployee);
        }

        if (selectedSpace != null && offerTypeFromSelectedReservaion == OfferType.ONE_TIME) {
            selectedReservation.setSpace(selectedSpace);
        }

        reservationService.updateReservation(selectedReservation.getId(), selectedReservation);

        return true;
    }

    private boolean createReservationForOneTime(Client selectClient, Offer selectOffer){
        Reservation createReservation = new Reservation();

        LocalDateTime localDateTime = dateGenerator.getLocalDateTime();
        if (localDateTime == null){
            errorLabel.setText("Wybierz poprawnie date");
            return false;
        }

        ReservationStatus reservationStatus = comboboxStatusReservation.getValue();

        if (reservationStatus == null){
            errorLabel.setText("Wybierz status rezerwacji");
            return false;
        }

        Long clientId = selectClient.getId();
        Optional<Client> optionalClient = clientService.getClient(clientId);
        Client client;
        if (optionalClient.isEmpty()){
            errorLabel.setText("Wybierz klienta");
            return false;
        }else {
            client = optionalClient.get();
        }

        Long offerId = selectOffer.getId();
        Optional<Offer> optionalOffer = offerService.getOffer(offerId);
        Offer offer;
        if (optionalOffer.isEmpty()){
            errorLabel.setText("Wybierz oferte/usługe");
            return false;
        }else {
            offer = optionalOffer.get();
        }

        Employee employee = comboboxEmployee.getSelectionModel().getSelectedItem();
        if (employee == null){
            errorLabel.setText("Wybierz pracownika");
            return false;
        }

        Space space = comboboxSpace.getSelectionModel().getSelectedItem();
        if (space == null){
            errorLabel.setText("Wybierz sale/hale");
            return false;
        }

        createReservation.setReservationStatus(reservationStatus);
        createReservation.setDate(localDateTime);
        createReservation.setClient(client);
        createReservation.setOffer(offer);

        createReservation.setEmployee(employee);
        createReservation.setSpace(space);

        reservationService.createReservation(createReservation);

        return true;
    }

    private boolean createReservationForSubscription(Client selectClient, Offer selectOffer){
        Reservation createReservation = new Reservation();

        LocalDate localDate = dateGenerator.getLocalDate();

        if (localDate == null){
            errorLabel.setText("Wybierz poprawnie date");
            return false;
        }

        ReservationStatus reservationStatus = comboboxStatusReservation.getValue();

        if (reservationStatus == null){
            errorLabel.setText("Wybierz status rezerwacji");
            return false;
        }

        Long clientId = selectClient.getId();
        Optional<Client> optionalClient = clientService.getClient(clientId);
        Client client;
        if (optionalClient.isEmpty()){
            errorLabel.setText("Wybierz klienta");
            return false;
        }else {
            client = optionalClient.get();
        }

        Long offerId = selectOffer.getId();
        Optional<Offer> optionalOffer = offerService.getOffer(offerId);
        Offer offer;
        if (optionalOffer.isEmpty()){
            errorLabel.setText("Wybierz oferte/usługe");
            return false;
        }else {
            offer = optionalOffer.get();
        }


        createReservation.setReservationStatus(reservationStatus);

        LocalDateTime dateTime = localDate.atStartOfDay(); // Ustaw czas na północ (00:00) lub inny odpowiedni czas
        createReservation.setDate(dateTime);

        createReservation.setClient(client);
        createReservation.setOffer(offer);

        reservationService.createReservation(createReservation);

        return true;
    }
}
