package sample;

import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import domain.Nota;
import domain.Tema;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.Service;
import validation.ValidationException;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerElev {
    @FXML
    private HBox hbox;
    @FXML
    Label lbl;
    @FXML
    private Pane panePP;
    @FXML
    private Pane paneN;
    @FXML
    private Pane paneT;
    @FXML
    private Button view;

    @FXML
    private Button tema;

    @FXML
    private Button nota;
    @FXML
    private Label lblN;

    @FXML
    private Label lblG;

    @FXML
    private Label lblE;
    @FXML
    private TableView<Tema> tableTeme;
    @FXML
    private TableView<Nota> tabelNota;

    @FXML
    private TableColumn<Tema, String> colDescriere;

    @FXML
    private TableColumn<Tema, Integer> colDeadline;

    @FXML
    private TableColumn<Tema, Integer> colPrimire;

    @FXML
    private TableColumn<Nota, String> colTema;

    @FXML
    private TableColumn<Nota, String> colFeedback;

    @FXML
    private TableColumn<Nota, Double> colNota;

    @FXML
    private ImageView exit;

    @FXML
    private ImageView mini;
    private double xOffset=0;
    private double yOffset=0;
    ObservableList<Tema> observableListT;
    ObservableList<Nota> observableListN;

    public ControllerElev(){}
    private  Service service;
    public void setService(Service service, User user) {
        this.service=service;
        try{
            service.findStudentDupaEmail(service.findUser(user.getID()).getID());
        observableListT=FXCollections.observableList(StreamSupport.stream(service.getTemaSt(user.getID()).spliterator(), false).collect(Collectors.toList()));
        observableListN= FXCollections.observableList(StreamSupport.stream(service.getNote(service.getNoteLista().stream().filter(n->service.findStudentDupaNume(n.getNumeStudent()).getEmail().equals(user.getID())).collect(Collectors.toList())).spliterator(), false).collect(Collectors.toList()));
        loadTableN();
        loadTableT();
        lblN.setText(service.findStudentDupaEmail(user.getID()).getNume());
        lblE.setText(String.valueOf(service.findStudentDupaEmail(user.getID()).getGrupa()));
        lblG.setText(user.getID());
        }
        catch (ValidationException ex){

        }
        panePP.setVisible(true);
        paneT.setVisible(false);
        paneN.setVisible(false);
        lbl.setText("View");

    }

    private void loadTableT() {
        tableTeme.setItems(observableListT);
        colDescriere.setCellValueFactory(new PropertyValueFactory<Tema,String>("Descriere"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<Tema,Integer>("Deadline"));
        colPrimire.setCellValueFactory(new PropertyValueFactory<Tema,Integer>("Primire"));

    }

    private void loadTableN() {
        tabelNota.setItems(observableListN);
        colTema.setCellValueFactory(new PropertyValueFactory<Nota,String>("descriereTema"));
        colFeedback.setCellValueFactory(new PropertyValueFactory<Nota,String>("Data"));
        colNota.setCellValueFactory(new PropertyValueFactory<Nota,Double>("Nota"));
    }

    @FXML
    public void click(ActionEvent event) {
        if (event.getSource() == view) {

            paneN.setVisible(false);
            paneT.setVisible(false);
            panePP.setVisible(true);
            new FadeIn(hbox).play();
            new BounceIn(panePP).play();
            lbl.setText("View");
        }
        if (event.getSource() == tema) {
            panePP.setVisible(false);
            paneN.setVisible(false);
            paneT.setVisible(true);
            lbl.setText("Teme");
            new FadeIn(hbox).play();
            new BounceIn(paneT).play();
        }
        if (event.getSource() == nota) {
            lbl.setText("Note");
            panePP.setVisible(false);
            paneT.setVisible(false);
            paneN.setVisible(true);
            new FadeIn(hbox).play();
            new BounceIn(paneN).play();

        }
    }


    public void minimize(MouseEvent mouseEvent) {
        Stage stage = (Stage) mini.getScene().getWindow();
        stage.setIconified(true);
    }

    public void clickLogOut(ActionEvent event) throws IOException {
        mini.getScene().getWindow().hide();
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
