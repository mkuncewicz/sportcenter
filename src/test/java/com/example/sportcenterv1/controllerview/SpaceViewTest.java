package com.example.sportcenterv1.controllerview;

import com.example.sportcenterv1.entity.enums.CourtType;
import com.example.sportcenterv1.entity.enums.TurfType;
import com.example.sportcenterv1.entity.space.BasketballRoom;
import com.example.sportcenterv1.entity.space.MartialArtsRoom;
import com.example.sportcenterv1.entity.space.MedicalRoom;
import com.example.sportcenterv1.entity.space.Room;
import com.example.sportcenterv1.entity.space.SoccerField;
import com.example.sportcenterv1.entity.space.Space;
import com.example.sportcenterv1.entity.space.SwimmingPool;
import com.example.sportcenterv1.service.SpaceService;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpaceViewTest {

    @Mock
    private SpaceService spaceService;

    @InjectMocks
    private SpaceView spaceView;

    @BeforeEach
    void setUp() {
        spaceView = new SpaceView("Room", spaceService);
    }

    @Test
    void testTextFieldForBasicRoom() {
        spaceView.choiceSetting("Room");

        TextField tex1 = (TextField) spaceView.lookup("#tex1");
        TextField tex2 = (TextField) spaceView.lookup("#tex2");
        TextField tex3 = (TextField) spaceView.lookup("#tex3");

        assertEquals("Nazwa", tex1.getPromptText());
        assertEquals("Pojemność", tex2.getPromptText());
        assertEquals("Przestrzeń (metry kwadratowe)", tex3.getPromptText());
    }

    @Test
    void testTextFieldForBasketball() {
        spaceView.choiceSetting("Koszykówka");

        TextField tex1 = (TextField) spaceView.lookup("#tex1");
        TextField tex2 = (TextField) spaceView.lookup("#tex2");
        TextField tex3 = (TextField) spaceView.lookup("#tex3");
        TextField tex4 = (TextField) spaceView.lookup("#tex4");
        ComboBox<CourtType> courtTypeComboBox = (ComboBox<CourtType>) spaceView.lookup("#courtTypeComboBox");

        assertEquals("Nazwa", tex1.getPromptText());
        assertEquals("Pojemność", tex2.getPromptText());
        assertEquals("Przestrzeń (metry kwadratowe)", tex3.getPromptText());
        assertEquals("Liczba koszy", tex4.getPromptText());
        assertNotNull(courtTypeComboBox);
    }

    @Test
    void testCreateBasketballRoom() {
        spaceView.choiceSetting("Koszykówka");

        TextField tex1 = (TextField) spaceView.lookup("#tex1");
        TextField tex2 = (TextField) spaceView.lookup("#tex2");
        TextField tex3 = (TextField) spaceView.lookup("#tex3");
        TextField tex4 = (TextField) spaceView.lookup("#tex4");
        ComboBox<CourtType> courtTypeComboBox = (ComboBox<CourtType>) spaceView.lookup("#courtTypeComboBox");

        tex1.setText("Basketball Room A");
        tex2.setText("100");
        tex3.setText("500");
        tex4.setText("4");
        courtTypeComboBox.setItems(FXCollections.observableArrayList(CourtType.values()));
        courtTypeComboBox.getSelectionModel().select(CourtType.WOOD);

        spaceView.createSpace();

        verify(spaceService, times(1)).createSpace(any(BasketballRoom.class));
    }

    @Test
    void testCreateRoom() {
        spaceView.choiceSetting("Room");

        TextField tex1 = (TextField) spaceView.lookup("#tex1");
        TextField tex2 = (TextField) spaceView.lookup("#tex2");
        TextField tex3 = (TextField) spaceView.lookup("#tex3");

        tex1.setText("Room A");
        tex2.setText("50");
        tex3.setText("200");

        spaceView.createSpace();

        verify(spaceService, times(1)).createSpace(any(Room.class));
    }

    @Test
    void testUpdateRoom() {
        spaceView.choiceSetting("Room");

        TextField tex1 = (TextField) spaceView.lookup("#tex1");
        TextField tex2 = (TextField) spaceView.lookup("#tex2");
        TextField tex3 = (TextField) spaceView.lookup("#tex3");

        tex1.setText("Updated Room A");
        tex2.setText("75");
        tex3.setText("250");

        when(spaceService.getSpace(1L)).thenReturn(Optional.of(new Room()));

        spaceView.updateSpace(1L);

        verify(spaceService, times(1)).updateSpace(eq(1L), any(Room.class));
    }

    @Test
    void testUpdateBasketballRoom() {
        spaceView.choiceSetting("Koszykówka");

        TextField tex1 = (TextField) spaceView.lookup("#tex1");
        TextField tex2 = (TextField) spaceView.lookup("#tex2");
        TextField tex3 = (TextField) spaceView.lookup("#tex3");
        TextField tex4 = (TextField) spaceView.lookup("#tex4");
        ComboBox<CourtType> courtTypeComboBox = (ComboBox<CourtType>) spaceView.lookup("#courtTypeComboBox");

        tex1.setText("Updated Basketball Room A");
        tex2.setText("150");
        tex3.setText("600");
        tex4.setText("6");
        courtTypeComboBox.setItems(FXCollections.observableArrayList(CourtType.values()));
        courtTypeComboBox.getSelectionModel().select(CourtType.SYNTHETIC);

        when(spaceService.getSpace(1L)).thenReturn(Optional.of(new BasketballRoom()));

        spaceView.updateSpace(1L);

        verify(spaceService, times(1)).updateSpace(eq(1L), any(BasketballRoom.class));
    }

    // Add similar tests for other room types...
}
