package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.Service;

import java.io.IOException;


public class ControllerExperiment {
    @FXML
    Button btn1;
    @FXML
    Button btn2;
    @FXML
    BorderPane pane;
    private Service service;
    public void setService(Service service) {
        this.service=service;
    }

    public void clickbtn1(ActionEvent event) throws IOException {
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/studenti.fxml"));
        root = loader.load();
        ControllerStudent controller = loader.getController();
        controller.setService(service);

        Scene scene=new Scene(root, 560,370);
        stage.setScene(scene);
        pane.setCenter(root);
    }

    public void clickbtn2(ActionEvent event) throws IOException {
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/teme.fxml"));
        root = loader.load();
        ControllerTeme controller = loader.getController();
        controller.setService(service);

        Scene scene=new Scene(root, 560,370);
        stage.setScene(scene);
        pane.setCenter(root);
    }
}
