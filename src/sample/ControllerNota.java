package sample;

import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;
import service.Service;
import utils.ChangeEventType;
import utils.Observer;
import validation.ValidationException;
import view.NotaEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerNota implements Observer<NotaEvent> {

    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private TableView<Nota> tableView;
    @FXML
    private TableColumn<Nota,String> colStudent;
    @FXML
    private TableColumn<Nota,String> colTema;
    @FXML
    private TableColumn<Nota,Double> colNota;
    @FXML
    private Button btnAddNota;
    @FXML
    private VBox vBox;
    @FXML
    private TextArea textAreaFeedback;
    @FXML
    private TextField textFieldStudent;
    @FXML
    private TextField textFieldNota;
    @FXML
    private TextField textFieldCauta;
    @FXML
    private TextField textFieldData;
    @FXML
    private TextField txtTema;
    @FXML
    private  CheckBox checkBox;
    @FXML
    private Button btnAddSt;
    @FXML
    private Button btnFiltrari;
    @FXML
    private Pane addPane;
    @FXML
    private Pane viewPane;
    @FXML
    private Button btnView;
    @FXML
    private Pagination pagination;
    @FXML
    private Label label;
    @FXML
    private HBox hbox;
    @FXML
    private ImageView back1;
    @FXML
    private javafx.scene.image.ImageView exit;
    @FXML
     private ImageView mini;
    private int from=0;
    private int to=0;

    private Service service;
    private ObservableList<Nota> notaObservableList;
    public  int verificareCorect=0;
    double xOffset=0;
    double yOffset=0;
    private  final  static  int nrRow=5;
    FilteredList<Nota> filteredList;



    public void setService(Service service){

        this.service=service;
        textAreaFeedback.setText("");
        this.service.notaEventObservable.addObserver(this);
        notaObservableList= FXCollections.observableList(StreamSupport.stream(service.getNote(service.getNoteLista()).spliterator(), false).collect(Collectors.toList()));
        filteredList=new FilteredList<>(notaObservableList,e->true);
        viewPane.setVisible(true);
        addPane.setVisible(false);
        int pagSize = service.sizeN()/nrRow+1;
        pagination.setPageCount(pagSize);
        pagination.setPageFactory(this::createDateNote);
        label.setText("Views");
        TextFields.bindAutoCompletion(textFieldStudent,service.getNumeStudenti());
        TextFields.bindAutoCompletion(textFieldCauta,service.getNumeStudenti());
        TextFields.bindAutoCompletion(txtTema,service.getNumeTeme());
        loadFiltru();
        loadComboBox();
        loadTableValues();

    }

    public List<Nota> getDataNote(){
        return this.service.getPieceDataN(from,to-1);

    }
    private Node createDateNote(Integer page) {
        from=Math.min(Math.max(nrRow * (page ), 0), service.getTemeLista().size());//page*nrRow;
        to=Math.min(from+nrRow,service.getTemeLista().size());//        int toIndex=Math.min(Math.max(nrRow * page, fromIndex), service.getStudentiLista().size());//
        tableView.setItems(FXCollections.observableList(this.getDataNote()));
        return tableView;
    }
    private void loadFiltru() {
        textFieldCauta.setOnKeyReleased(e->{
            textFieldCauta.textProperty().addListener((observableValue,oldValue,newValue)->{
                    filteredList.setPredicate((Predicate<? super Nota>) nota->{
                        if(newValue== null || newValue.isEmpty()){
                            return true;
                        }

                        if(nota.getNumeStudent().contains(newValue)){
                            return true;
                        }
                        return false;
                });
            });
            SortedList<Nota>sortedList=new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedList);
        });
        txtTema.setOnKeyReleased(e->{
            txtTema.textProperty().addListener((observableValue,oldValue,newValue)->{
                filteredList.setPredicate((Predicate<? super Nota>) nota->{
                    if(newValue== null || newValue.isEmpty()){
                        return true;
                    }

                    if(nota.getDescriereTema().contains(newValue)){
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Nota>sortedList=new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedList);
        });
    }

    private Node createPage(Integer page) {
        int fromIndex=Math.min(Math.max(nrRow * (page), 0), service.getNoteLista().size());//page*nrRow;
        int toIndex= Math.min(fromIndex+nrRow,service.getNoteLista().size());;//Math.min(fromIndex+nrRow,service.getStudentiLista().size());
        //int toIndex=nrRow;
        tableView.setItems(FXCollections.observableArrayList(service.getNoteLista().subList(fromIndex,toIndex)));
        return tableView;
    }


    public ControllerNota() {    }

    public ObservableList getList(){
        return notaObservableList;
    }

    public void loadComboBox(){
        List<String> listaNume=service.getNumeTeme();
        ObservableList<String>observable=FXCollections.observableList(listaNume);
        comboBox.setItems(observable);
        try {
            Tema tema = service.temaCurenta();
            if (tema != null)
                comboBox.getSelectionModel().select(tema.getDescriere());
        }catch(Exception ex){

        }

    }

    private void loadTableValues() {
        tableView.setItems(notaObservableList);
        colStudent.setCellValueFactory(new PropertyValueFactory<Nota,String>("numeStudent"));
        colTema.setCellValueFactory(new PropertyValueFactory<Nota,String>("descriereTema"));
        colNota.setCellValueFactory(new PropertyValueFactory<Nota,Double>("Nota"));
    }


    @FXML
    public void btnAdaugaAction(MouseEvent event) throws IOException {
        try{
        int verifCheckBox=0;
        if(checkBox.isSelected()){
            verifCheckBox=1;
        }
        String numeStudent=textFieldStudent.getText();
        String descriereTema=comboBox.getValue();
        Student student=service.findStudentDupaNume(numeStudent);
        Tema tema=service.findTemaDupaDescriere(descriereTema);
        String idNota=student.getID()+"#"+tema.getID();
        service.findNota(idNota);
        String data=textFieldData.getText();
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
        localDate = LocalDate.parse(data, formatter);}
        catch (RuntimeException ex){
            verificareCorect+=1;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORRR");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Data incorecta");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }
        int predare = service.calculeazaSPredare(localDate);
        double penalitati=0.0;
        if(!checkBox.isSelected()&& predare != tema.getDeadline()){
            penalitati = 2.5 * (predare - tema.getDeadline());
            textAreaFeedback.setText("Nota a fost diminuata cu "+ penalitati+" puncte din cauza intarzierilor");
        }
            String nota=textFieldNota.getText();
            Double nota2;
            try{
                nota2=Double.parseDouble(nota);
            }catch (RuntimeException ex){
                verificareCorect+=1;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERRORRRR");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setHeaderText(null);
                alert.setContentText("Nota nu e ok");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
                dialogPane.getStyleClass().add("myDialog");
                dialogPane.setHeader(null);
                alert.show();
            }
        if(verificareCorect==0) {
            Parent root;
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/confirmare.fxml"));
            root = loader.load();
            ControllerVerificare controllerVerif = loader.getController();
            controllerVerif.setService(service, textFieldStudent.getText(), comboBox.getValue(), textFieldNota.getText(),
                    textFieldData.getText(), textAreaFeedback.getText(), verifCheckBox,penalitati);
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
            Scene scene=new Scene(root, 600,400);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

        }
        }
        catch (  ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORRR");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(ex.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }
    }

    @FXML
    public void showStudent(ActionEvent event) throws IOException {
        try {
            Parent root;
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/showSt.fxml"));
            root = loader.load();
            ControllerShowSt controllerShow = loader.getController();
            controllerShow.setService(service, textFieldCauta.getText());
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

            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root, 343, 441);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

        }catch(ValidationException ex){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORRR");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Student inexistent");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();

        }



    }

    @FXML
    private void clickButonFiltrari(ActionEvent event) throws IOException{
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/filtrari.fxml"));
        root = loader.load();
        ControllerFiltrari controllerShow = loader.getController();
        controllerShow.setService(service);
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
        Scene scene=new Scene(root, 727,456);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);

    }
    @Override
    public void update(NotaEvent studentEvent) {
        if (studentEvent.getType() == ChangeEventType.ADD) {
            notaObservableList.add(studentEvent.getData());
            pagination.setPageFactory(this::createDateNote);
            loadTableValues();
        }
    }
    public void click(ActionEvent event) {
        if(event.getSource()==btnAddNota){
            label.setText("Adauga nota");
            addPane.setVisible(true);
            new FadeIn(addPane).play();
            viewPane.setVisible(false);
            new BounceIn(hbox).play();
            textFieldData.setText(String.valueOf(LocalDate.now()));
        }
        if(event.getSource()==btnView){
            label.setText("View note");
            viewPane.setVisible(true);
            new FadeIn(viewPane).play();
            addPane.setVisible(false);
            new BounceIn(hbox).play();
            textAreaFeedback.setText("");
            textFieldStudent.setText("");
            textFieldData.setText("");
            textFieldNota.setText("");


        }
    }
    public void exitProgram(MouseEvent mouseEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    public void minimize(MouseEvent event) {

        Stage stage = (Stage) mini.getScene().getWindow();
        stage.setIconified(true);
    }
    public void clickback(MouseEvent event) throws IOException {

        back1.getScene().getWindow().hide();
        int j=0;
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
        // Stage primaryStage=(Stage) exit.getScene().getWindow();
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene=new Scene(root, 560,370);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }
}
