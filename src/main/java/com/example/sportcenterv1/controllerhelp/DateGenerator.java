package com.example.sportcenterv1.controllerhelp;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateGenerator {

    private DatePicker datePicker;

    private ComboBox<Integer> comboBoxHour;

    private ComboBox<Integer> comboBoxMinute;

    public DateGenerator(DatePicker datePicker, ComboBox<Integer> comboBoxHour, ComboBox<Integer> comboBoxMinute) {
        this.datePicker = datePicker;
        this.comboBoxHour = comboBoxHour;
        this.comboBoxMinute = comboBoxMinute;
    }


    public LocalDateTime getLocalDateTime(){
        try {


            LocalDate localDate = datePicker.getValue();
            int hour = comboBoxHour.getValue();
            int minute = comboBoxMinute.getValue();
            LocalDateTime localDateTime = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), hour, minute);

            return localDateTime;
        }catch (Exception e){

        }
        return null;
    }

}
