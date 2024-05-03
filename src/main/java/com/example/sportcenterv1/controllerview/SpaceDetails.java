package com.example.sportcenterv1.controllerview;

import com.example.sportcenterv1.entity.space.Room;
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


    public void getDetailsForSpace(){

    }

    public List<Label> getLabels(Room room){
        List<Label> result = new ArrayList<>();
        label1.setText("Nazwa: " + room.getName());
        label2.setText("Ilość: " + room.getCapacity());
        label3.setText("Metry kwadratowe: " + room.getCapacity());

        result.add(label1);
        result.add(label2);
        result.add(label3);

        return result;
    }

    public VBox setLabels(Room room){
        this.getChildren().clear();

        label1.setText("Nazwa: " + room.getName());
        label2.setText("Ilość: " + room.getCapacity());
        label3.setText("Metry kwadratowe: " + room.getCapacity());

        this.getChildren().add(label1);
        this.getChildren().add(label2);
        this.getChildren().add(label3);

        return this;
    }
}
