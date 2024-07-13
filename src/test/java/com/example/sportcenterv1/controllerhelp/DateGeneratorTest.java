package com.example.sportcenterv1.controllerhelp;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DateGeneratorTest {

    private DatePicker datePicker;
    private ComboBox<Integer> comboBoxHour;
    private ComboBox<Integer> comboBoxMinute;
    private DateGenerator dateGenerator;

    @BeforeEach
    void setUp() {
        datePicker = mock(DatePicker.class);
        comboBoxHour = mock(ComboBox.class);
        comboBoxMinute = mock(ComboBox.class);

        dateGenerator = new DateGenerator(datePicker, comboBoxHour, comboBoxMinute);
    }

    @Test
    void testGetLocalDateTime() {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        int hour = 14;
        int minute = 30;

        when(datePicker.getValue()).thenReturn(localDate);
        when(comboBoxHour.getValue()).thenReturn(hour);
        when(comboBoxMinute.getValue()).thenReturn(minute);

        LocalDateTime result = dateGenerator.getLocalDateTime();
        assertNotNull(result);
        assertEquals(LocalDateTime.of(2024, 7, 1, 14, 30), result);
    }

    @Test
    void testGetLocalDateTime_NullValues() {
        when(datePicker.getValue()).thenReturn(null);
        when(comboBoxHour.getValue()).thenReturn(null);
        when(comboBoxMinute.getValue()).thenReturn(null);

        LocalDateTime result = dateGenerator.getLocalDateTime();
        assertNull(result);
    }

    @Test
    void testGetLocalDate() {
        LocalDate localDate = LocalDate.of(2024, 7, 1);

        when(datePicker.getValue()).thenReturn(localDate);

        LocalDate result = dateGenerator.getLocalDate();
        assertNotNull(result);
        assertEquals(LocalDate.of(2024, 7, 1), result);
    }

    @Test
    void testGetLocalTime() {
        int hour = 14;
        int minute = 30;

        when(comboBoxHour.getValue()).thenReturn(hour);
        when(comboBoxMinute.getValue()).thenReturn(minute);

        LocalTime result = dateGenerator.getLocalTime();
        assertNotNull(result);
        assertEquals(LocalTime.of(14, 30), result);
    }
}
