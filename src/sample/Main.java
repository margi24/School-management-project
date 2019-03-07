package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import repository.*;
import service.Service;
import validation.*;

public class Main extends Application {
    private double xOffset=0;
    private double yOffset=0;
    @Override
    public void start(Stage primaryStage) throws Exception{
        XMLRepositoryStudent studentFileRepository=new XMLRepositoryStudent("Studenti.xml");
        XMLRepositoryTema temaFileRepository=new XMLRepositoryTema("Teme.xml");
        XMLRepositoryNota notaFileRepository=new XMLRepositoryNota("Nota.xml");
        XMLRepositoryUser userFileRepository=new XMLRepositoryUser("User.xml");
        XMLRepositoryProf profFileRepository=new XMLRepositoryProf("Prof.xml");
        StudentValidator studentValidator=new StudentValidator();
        TemaValidator temaValidator=new TemaValidator();
        UserValidator userValidator =new UserValidator();
        NotaValidator notaValidator=new NotaValidator(studentFileRepository,temaFileRepository);
        ProfValidator profValidator=new ProfValidator();
        Service service=new Service(studentFileRepository,studentValidator,temaFileRepository,temaValidator,notaFileRepository,notaValidator,
                userFileRepository,userValidator,profFileRepository,profValidator);

        Parent root;
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(Main.class.getResource("sample.fxml"));
        root=loader.load();
        sample.Controller controller=loader.getController();
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
                primaryStage.setX(event.getScreenX()-xOffset);
                primaryStage.setY(event.getScreenY()-yOffset);
            }
        });

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene=new Scene(root, 345,442);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
