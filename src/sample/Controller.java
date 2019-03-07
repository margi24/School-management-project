package sample;

import domain.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.Service;
import validation.ValidationException;

import java.io.IOException;

public class Controller {

    private double xOffset=0;
    private double yOffset=0;
    @FXML
    private ImageView exit;
    @FXML
    private ImageView mini;
    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField txtPass;

    @FXML
    private Button btnLog;

    private  Service service;
    public Controller() {
    }


    public void setService(Service service) {
        this.service = service;

    }

    public void clickBtn(ActionEvent event) throws IOException {
        try{
            User user=service.findUser(txtUser.getText());
            boolean verifparola=service.verifParola(txtUser.getText(),txtPass.getText());
            if(user.getTip().equals("admin")) {
                Parent root;
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/meniuProfAdmin.fxml"));
                root = loader.load();
                ControllerProfA controller = loader.getController();
                controller.setService(service);
                root.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    }
                });
                root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        stage.setX(event.getScreenX() - xOffset);
                        stage.setY(event.getScreenY() - yOffset);
                    }
                });
                Stage primaryStage = (Stage) exit.getScene().getWindow();
                stage.initStyle(StageStyle.TRANSPARENT);
                Scene scene = new Scene(root, 560, 370);
                scene.setFill(Color.TRANSPARENT);
                primaryStage.setScene(scene);
            }
            if(user.getTip().equals("student")){
                Parent root;
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/elev.fxml"));
                root = loader.load();
                ControllerElev controller = loader.getController();
                controller.setService(service,user);
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
                Stage primaryStage=(Stage) exit.getScene().getWindow();
                stage.initStyle(StageStyle.TRANSPARENT);
                Scene scene=new Scene(root, 604,477);
                scene.setFill(Color.TRANSPARENT);
                primaryStage.setScene(scene);
            }
            if(user.getTip().equals("profesor")){
                Parent root;
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/meniuAdmin.fxml"));
                root = loader.load();
                ControllerMeniuAdmin controller = loader.getController();
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
                Stage primaryStage=(Stage) exit.getScene().getWindow();
                stage.initStyle(StageStyle.TRANSPARENT);
                Scene scene=new Scene(root, 560,370);
                scene.setFill(Color.TRANSPARENT);
                primaryStage.setScene(scene);
            }
            }catch(ValidationException  | NullPointerException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eroare la logare");
                alert.setHeaderText(null);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("Username sau Password incorect!!");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
                dialogPane.getStyleClass().add("myDialog");
                dialogPane.setHeader(null);
                alert.show();
            }




        /*
        if(txtUser.getText().equals("a") && txtPass.getText().equals("a")){
            Parent root;
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/meniuAdmin.fxml"));
            root = loader.load();
            ControllerMeniuAdmin controller = loader.getController();
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
            Stage primaryStage=(Stage) exit.getScene().getWindow();
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene=new Scene(root, 560,370);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(scene);

        }
        else if(txtUser.getText().equals("b") && txtPass.getText().equals("b")){
            Parent root;
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/elev.fxml"));
            root = loader.load();
            ControllerElev controller = loader.getController();
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
            Stage primaryStage=(Stage) exit.getScene().getWindow();
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene=new Scene(root, 700,500);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(scene);

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare la logare");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Username sau Password incorect!!");
            alert.show();
        }*/
    }

    public void exitProgram(Event mouseEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    public void minimize(MouseEvent mouseEvent) {
        Stage stage = (Stage) mini.getScene().getWindow();
        stage.setIconified(true);
    }

    public void clickBtnSign(ActionEvent event) throws IOException {
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/signUp.fxml"));
        root = loader.load();
        ControllerSignUp controller = loader.getController();
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
        Stage primaryStage=(Stage) exit.getScene().getWindow();
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene=new Scene(root, 600,400);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
    }
}
