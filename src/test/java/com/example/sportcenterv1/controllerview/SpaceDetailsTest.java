package com.example.sportcenterv1.controllerview;

import com.example.sportcenterv1.entity.enums.CourtType;
import com.example.sportcenterv1.entity.enums.TurfType;
import com.example.sportcenterv1.entity.space.*;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SpaceDetailsTest {

    private SpaceDetails spaceDetails;

    @BeforeEach
    void setUp() {
        spaceDetails = new SpaceDetails();
    }

    @Test
    void testGetLabelsForRoom() {
        Room room = new Room();
        room.setName("Room A");
        room.setCapacity(100);
        room.setSquareFootage(500);

        List<Label> labels = spaceDetails.getDetailsLabelsForSpace(room);

        assertEquals(3, labels.size());
        assertEquals("Nazwa: Room A", labels.get(0).getText());
        assertEquals("Ilość: 100", labels.get(1).getText());
        assertEquals("Metry kwadratowe: 500", labels.get(2).getText());
    }

    @Test
    void testGetLabelsForBasketballRoom() {
        BasketballRoom basketballRoom = new BasketballRoom();
        basketballRoom.setName("Basketball Room A");
        basketballRoom.setCapacity(200);
        basketballRoom.setSquareFootage(1000);
        basketballRoom.setHoopCount(4);
        basketballRoom.setCourtType(CourtType.WOOD);

        List<Label> labels = spaceDetails.getDetailsLabelsForSpace(basketballRoom);

        assertEquals(5, labels.size());
        assertEquals("Nazwa: Basketball Room A", labels.get(0).getText());
        assertEquals("Max ilość użytkowników: 200", labels.get(1).getText());
        assertEquals("Metry kwadratowe: 200", labels.get(2).getText());
        assertEquals("Ilość koszy: 4", labels.get(3).getText());
        assertEquals("Typ nawierzchni Drewno", labels.get(4).getText());
    }

    @Test
    void testGetLabelsForMartialArtsRoom() {
        MartialArtsRoom martialArtsRoom = new MartialArtsRoom();
        martialArtsRoom.setName("Martial Arts Room A");
        martialArtsRoom.setCapacity(50);
        martialArtsRoom.setSquareFootage(300);
        martialArtsRoom.setMatCount(10);

        List<Label> labels = spaceDetails.getDetailsLabelsForSpace(martialArtsRoom);

        assertEquals(4, labels.size());
        assertEquals("Nazwa: Martial Arts Room A", labels.get(0).getText());
        assertEquals("Max ilośc użytkowników: 50", labels.get(1).getText());
        assertEquals("Metry kwadratowe: 300", labels.get(2).getText());
        assertEquals("Ilość mat: 10", labels.get(3).getText());
    }

    @Test
    void testGetLabelsForMedicalRoom() {
        MedicalRoom medicalRoom = new MedicalRoom();
        medicalRoom.setName("Medical Room A");
        medicalRoom.setCapacity(20);
        medicalRoom.setSquareFootage(200);
        medicalRoom.setSterile(true);

        List<Label> labels = spaceDetails.getDetailsLabelsForSpace(medicalRoom);

        assertEquals(4, labels.size());
        assertEquals("Nazwa: Medical Room A", labels.get(0).getText());
        assertEquals("Max ilość użytkowników: Medical Room A", labels.get(1).getText());
        assertEquals("Metry kwadratowe: 200", labels.get(2).getText());
        assertEquals("Sterylność: Tak", labels.get(3).getText());
    }

    @Test
    void testGetLabelsForSoccerField() {
        SoccerField soccerField = new SoccerField();
        soccerField.setName("Soccer Field A");
        soccerField.setSquareFootage(5000);
        soccerField.setGoalCount(2);
        soccerField.setTurfType(TurfType.NATURAL);

        List<Label> labels = spaceDetails.getDetailsLabelsForSpace(soccerField);

        assertEquals(4, labels.size());
        assertEquals("Nazwa: Soccer Field A", labels.get(0).getText());
        assertEquals("Metry kwadratowe: 5000", labels.get(1).getText());
        assertEquals("Ilość bramek: 2", labels.get(2).getText());
        assertEquals("Typ nawierzchni: NATURAL", labels.get(3).getText());
    }

    @Test
    void testGetLabelsForSwimmingPool() {
        SwimmingPool swimmingPool = new SwimmingPool();
        swimmingPool.setName("Swimming Pool A");
        swimmingPool.setPoolLength(50);
        swimmingPool.setPoolDepth(2);
        swimmingPool.setLaneCount(8);

        List<Label> labels = spaceDetails.getDetailsLabelsForSpace(swimmingPool);

        assertEquals(4, labels.size());
        assertEquals("Nazwa: Swimming Pool A", labels.get(0).getText());
        assertEquals("Długośc basenu: 50", labels.get(1).getText());
        assertEquals("Głębokość basenu: 2", labels.get(2).getText());
        assertEquals("Ilość torów: 8", labels.get(3).getText());
    }
}
