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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.Service;

import java.io.IOException;

public class ControllerMeniuAdmin {

    private double xOffset=0;
    private double yOffset=0;
    @FXML
    Button btnStud;
    @FXML
    Button btnTeme;
    @FXML
    Button btnNote;
    @FXML
    private AnchorPane pane;
    @FXML
    ImageView exit;
    @FXML
    ImageView mini;

    private Service service;

    public void setService(Service service) {

        this.service=service;

    }

    @FXML
    public void clickBtnStud(MouseEvent event)throws IOException {
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/studenti.fxml"));
        root = loader.load();
        ControllerStudent controllerStudent = loader.getController();
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
    void clickBtnNote(MouseEvent event) throws IOException {
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/note.fxml"));
        root = loader.load();
        ControllerNota controllerNota = loader.getController();
        controllerNota.setService(service);
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
        Scene scene=new Scene(root, 751,566);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);



    }

    @FXML
    void clickBtnTeme(MouseEvent event) throws IOException{
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/teme.fxml"));
        root = loader.load();
        ControllerTeme controllerTeme = loader.getController();
        controllerTeme.setService(service);
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
        Scene scene=new Scene(root, 640,488);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
    }

    public void exitProgram(MouseEvent mouseEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    public void minimize(MouseEvent event) {

        Stage stage = (Stage) mini.getScene().getWindow();
        stage.setIconified(true);
    }

    public void clickLogOut(ActionEvent event) throws IOException {


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
    }
