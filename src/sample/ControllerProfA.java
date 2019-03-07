package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.Service;

import java.io.IOException;

public class ControllerProfA {
    @FXML
    private ImageView mini;

    @FXML
    private ImageView exit;

    @FXML
    private Button btnStud;

    @FXML
    private Button btnNote;
    private double xOffset=0;
    private double yOffset=0;
    private Service service;
    public ControllerProfA(){}
    public void setService(Service service) {
        this.service = service;
    }



    @FXML
    void clickBtnProf(MouseEvent event) throws IOException {
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/prof.fxml"));
        root = loader.load();
        ControllerProf controllerStudent = loader.getController();
        controllerStudent.setService(service);
        root.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset=event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX()-xOffset);
                stage.setY(event.getScreenY()-yOffset);
            }
        });
        Stage primaryStage=(Stage)exit.getScene().getWindow();
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene=new Scene(root, 748,531);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
    }

    @FXML
    void clickBtnStud(MouseEvent event) throws IOException {
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/studProf.fxml"));
        root = loader.load();
        ControllerStudProf controllerStudent = loader.getController();
        controllerStudent.setService(service);
        root.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset=event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX()-xOffset);
                stage.setY(event.getScreenY()-yOffset);
            }
        });
        Stage primaryStage=(Stage)exit.getScene().getWindow();
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene=new Scene(root, 714,567);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
    }

    @FXML
    void clickLogOut(ActionEvent event) throws IOException {
        exit.getScene().getWindow().hide();
        int i=0;
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/sample.fxml"));
        root = loader.load();
        Controller controller = loader.getController();
        controller.setService(service);
        root.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset=event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX()-xOffset);
                stage.setY(event.getScreenY()-yOffset);
            }
        });
        // Stage primaryStage=(Stage) exit.getScene().getWindow();
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene=new Scene(root, 345,442);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }


    public void exitProgram(MouseEvent mouseEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    public void minimize(MouseEvent event) {

        Stage stage = (Stage) mini.getScene().getWindow();
        stage.setIconified(true);
    }


}
