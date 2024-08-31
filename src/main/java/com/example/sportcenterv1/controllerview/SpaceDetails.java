package com.example.sportcenterv1.controllerview;

import com.example.sportcenterv1.entity.space.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


public class SpaceDetails extends VBox {

    private Label label1 = new Label();

    private Label label2 = new Label();

    private Label label3 = new Label();

    private Label label4 = new Label();

    private Label label5 = new Label();

    private Label label6 = new Label();

    private Label label7 = new Label();

    public SpaceDetails() {


    }


    public List<Label> getDetailsLabelsForSpace(Space space){

        if (space instanceof BasketballRoom){
            BasketballRoom basketballRoom = (BasketballRoom) space;
            return getLabelsForBasketBasketballRooms(basketballRoom);

        } else if (space instanceof MartialArtsRoom) {
            MartialArtsRoom martialArtsRoom = (MartialArtsRoom) space;
            return getLabelsForMarialArsRoom(martialArtsRoom);

        } else if (space instanceof MedicalRoom) {
            MedicalRoom medicalRoom = (MedicalRoom) space;
            return getLabelsForMedicalRooms(medicalRoom);

        } else if (space instanceof  Room) {
            Room room = (Room) space;
            return getLabelsForRoom(room);

        } else if (space instanceof SoccerField){
            SoccerField soccerField = (SoccerField) space;
            return getLabelsForSocerField(soccerField);

        } else if (space instanceof SwimmingPool) {
            SwimmingPool swimmingPool = (SwimmingPool) space;
            return getLabelsForSwimmingPool(swimmingPool);

        }

        return null;
    }

    public List<Label> getLabelsForRoom(Room room){
        List<Label> result = new ArrayList<>();
        label1.setText("Nazwa: " + room.getName());
        label2.setText("Ilość: " + room.getCapacity());
        label3.setText("Metry kwadratowe: " + room.getSquareFootage());

        result.add(label1);
        result.add(label2);
        result.add(label3);

        return result;
    }

    private List<Label> getLabelsForBasketBasketballRooms(BasketballRoom basketballRoom){
        List<Label> result = new ArrayList<>();
        label1.setText("Nazwa: " + basketballRoom.getName());
        label2.setText("Max ilość użytkowników: " + basketballRoom.getCapacity());
        label3.setText("Metry kwadratowe: " + basketballRoom.getCapacity());
        label4.setText("Ilość koszy: " + basketballRoom.getHoopCount());
        label5.setText("Typ nawierzchni " + basketballRoom.getCourtType().getDisplayName());

        result.add(label1);
        result.add(label2);
        result.add(label3);
        result.add(label4);
        result.add(label5);

        return result;
    }

    private List<Label> getLabelsForMarialArsRoom(MartialArtsRoom martialArtsRoom){
        List<Label> result = new ArrayList<>();
        label1.setText("Nazwa: " + martialArtsRoom.getName());
        label2.setText("Max ilośc użytkowników: " + martialArtsRoom.getCapacity());
        label3.setText("Metry kwadratowe: " + martialArtsRoom.getSquareFootage());
        label4.setText("Ilość mat: " + martialArtsRoom.getMatCount());

        result.add(label1);
        result.add(label2);
        result.add(label3);
        result.add(label4);

        return result;
    }

    private List<Label> getLabelsForMedicalRooms(MedicalRoom medicalRoom){
        List<Label> result = new ArrayList<>();
        label1.setText("Nazwa: " + medicalRoom.getName());
        label2.setText("Max ilość użytkowników: " + medicalRoom.getName());
        label3.setText("Metry kwadratowe: " + medicalRoom.getSquareFootage());
        if (medicalRoom.isSterile()) label4.setText("Sterylność: Tak" );
        else label4.setText("Sterylność: Nie");

        result.add(label1);
        result.add(label2);
        result.add(label3);
        result.add(label4);

        return result;
    }

    private List<Label> getLabelsForSocerField(SoccerField soccerField){
        List<Label> result = new ArrayList<>();
        label1.setText("Nazwa: " + soccerField.getName());
        label2.setText("Metry kwadratowe: " + soccerField.getSquareFootage());
        label3.setText("Ilość bramek: " + soccerField.getGoalCount());
        label4.setText("Typ nawierzchni: " + soccerField.getTurfType());

        result.add(label1);
        result.add(label2);
        result.add(label3);
        result.add(label4);

        return result;
    }

    private List<Label> getLabelsForSwimmingPool(SwimmingPool swimmingPool){
        List<Label> result = new ArrayList<>();
        label1.setText("Nazwa: " + swimmingPool.getName());
        label2.setText("Długośc basenu: " + swimmingPool.getPoolLength());
        label3.setText("Głębokość basenu: " + swimmingPool.getPoolDepth());
        label4.setText("Ilość torów: " + swimmingPool.getLaneCount());

        result.add(label1);
        result.add(label2);
        result.add(label3);
        result.add(label4);

        return result;
    }

}