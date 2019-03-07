package sample;

import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import domain.Profesor;
import domain.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;
import service.Service;
import utils.ChangeEventType;
import utils.Observer;
import validation.ValidationException;
import view.ProfEvent;

import java.io.IOException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerProf implements Observer<ProfEvent> {
    @FXML
    private ImageView back;
    @FXML
    private Button view;
    @FXML
    private Button prof;
    @FXML
    private HBox hbox;
    @FXML
    private Label lbl;
    @FXML
    private Pane panePP;
    @FXML
    private TableView<Profesor> tblProf;
    @FXML
    private TableView<Student> tblStud;
    @FXML
    private TextField txtProf;
    @FXML
    private Pane panePr;
    @FXML
    private Label labelP;
    @FXML
    private TextField profT;
    @FXML
    private TextField profEmailt;
    @FXML
    private TableColumn<Profesor, String> colNumeProf;
    @FXML
    private TableColumn<Profesor, String> colEmailProf;
    private ObservableList<Profesor>profesorObservableList;
    private FilteredList<Profesor>filteredListPr;
    double xOffset=0;
    double yOffset=0;
    private Service service;
    public ControllerProf(){}
    public void setService(Service service){
        this.service=service;
        this.service.profEventObservable.addObserver(this);
        panePP.setVisible(true);
        panePr.setVisible(false);
        profesorObservableList = FXCollections.observableList(StreamSupport.stream(service.getAllProfi().spliterator(), false).collect(Collectors.toList()));
        TextFields.bindAutoCompletion(txtProf,service.getAllProfi());
        filteredListPr=new FilteredList<>(profesorObservableList, e->true);
        labelP.setVisible(false);

        loadTables();
        loadFiltrari();
    }

    private void loadFiltrari() {
        txtProf.setOnKeyReleased(e->{
            txtProf.textProperty().addListener((observableValue,oldValue,newValue)->{
                filteredListPr.setPredicate((Predicate<? super Profesor>) nota->{
                    if(newValue== null || newValue.isEmpty()){
                        return true;
                    }
                    if(nota.getID().contains(newValue)){
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Profesor> sortedList=new SortedList<>(filteredListPr);
            sortedList.comparatorProperty().bind(tblProf.comparatorProperty());
            tblProf.setItems(sortedList);
        });
    }

    private void loadTables() {
        tblProf.setItems(profesorObservableList);
        colNumeProf.setCellValueFactory(new PropertyValueFactory<Profesor,String>("ID"));
        colEmailProf.setCellValueFactory(new PropertyValueFactory<Profesor,String>("Email"));
        tblProf.getSelectionModel().selectedItemProperty().addListener((observer, oldData, newData)-> showDetails(newData));
        tblProf.setEditable(true);
        colEmailProf.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private void showDetails(Profesor newData) {
        if(newData!=null) {
            profEmailt.setText(newData.getID());
            profT.setText(newData.getEmail());
        }
    }



    public void changeEmailProf(TableColumn.CellEditEvent<Profesor, String> profStringCellEditEvent) {
        Profesor profesor=tblProf.getSelectionModel().getSelectedItem();
        profesor.setEmail(profStringCellEditEvent.getNewValue());
        service.updateProf(profesor);

    }

    public void clickBack(MouseEvent event) throws IOException {

        back.getScene().getWindow().hide();
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/meniuProfAdmin.fxml"));
        root = loader.load();
        ControllerProfA controller = loader.getController();
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
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene=new Scene(root, 560,370);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }
    public void clickDelProf(MouseEvent mouseEvent) {
        try{
            int idmaxi=0;
            Profesor st= new Profesor(profT.getText(), profEmailt.getText());
            String u=profEmailt.getText();
            service.deleteProf(st.getID());
            service.deleteUser(u);

        }catch (ValidationException | NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("EROARE STERGERE PROFESOR");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }
    }

    public void clickAddProf(MouseEvent mouseEvent) {
        try{
            int idmaxi=0;
            Profesor st= new Profesor(profT.getText(), profEmailt.getText());
            service.addProfesor(st);
            labelP.setVisible(true);
            labelP.setText("Profesor adaugat");

        }catch (ValidationException | NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("EROARE ADAUGARE PROFESOR");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }
    }

    @Override
    public void update(ProfEvent profEvent) {

        if(profEvent.getType() == ChangeEventType.ADD){
            profesorObservableList.add(profEvent.getData());
        }
        if(profEvent.getType() == ChangeEventType.DELETE){
            profesorObservableList.remove(profEvent.getOldData());
        }
        if(profEvent.getType()== ChangeEventType.UPDATE){
            profesorObservableList.remove(profEvent.getOldData());
            profesorObservableList.add(profEvent.getData());
        }
    }



    @FXML
    void click(ActionEvent event){
        if(event.getSource()==view){

            panePP.setVisible(true);
            new FadeIn(panePP).play();
            panePr.setVisible(false);
            new BounceIn(hbox).play();
            lbl.setText("View profesori");
        }
            if(event.getSource()==prof){
            profT.setText("");
            profEmailt.setText("");
            panePP.setVisible(false);
            panePr.setVisible(true);
            new FadeIn(panePr);
            new BounceIn(hbox).play();
            lbl.setText("Adauga profesor");
            labelP.setVisible(false);
        }


    }
}
