package sample;

import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;
import org.controlsfx.control.textfield.TextFields;
import service.Service;
import utils.ChangeEventType;
import utils.Observer;
import validation.ValidationException;
import view.TemaEvent;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerTeme implements Observer<TemaEvent> {
    @FXML
    private TableView<Tema> tableView;


    @FXML
    private TableColumn<Tema, String> descriere;

    @FXML
    private TableColumn<Tema, Integer> deadline;

    @FXML
    private TableColumn<Tema, Integer> primire;
    @FXML
    private TextField descriereText;

    @FXML
    private TextField deadlineText;

    @FXML
    private TextField primireText;
    @FXML
    private ImageView back;

    @FXML
    private Label label;
    @FXML
    private Button add;
    @FXML
    private ImageView mini;

    @FXML
    private ImageView exit;
    @FXML
    private TextField txtCauta;
    @FXML
    private HBox hbox;
    @FXML
    private Pagination pagination;
    @FXML
    private Pane panePP;
    @FXML
    private Pane paneA;
    @FXML
    private Button view;
    @FXML
    private Label lblT;

    private Service service;
    private static final int nrRow=5;
    ObservableList<Tema>observableList;
    private double  xOffset=0;
    private double  yOffset=0;
    private int from=0;
    private int to=0;
    private FilteredList<Tema> filteredList;
    public ControllerTeme() {}

    public void setService(Service service){
        this.service = service;
        this.service.temaEventObservable.addObserver(this);
        observableList = FXCollections.observableList(StreamSupport.stream(service.getAllTeme().spliterator(), false).collect(Collectors.toList()));
        TextFields.bindAutoCompletion(txtCauta,service.getNumeTeme());
        label.setText("View teme");
        filteredList=new FilteredList<>(observableList,e->true);
        int pagSize=this.service.sizeT()/this.nrRow+1;
        pagination.setPageCount(pagSize);
        pagination.setPageFactory(this::createDateTeme);
        panePP.setVisible(true);
        paneA.setVisible(false);
        loadTableSt();
        loadFiltru();
        lblT.setVisible(false);
    }

    private void loadFiltru() {
        txtCauta.setOnKeyReleased(e->{
            txtCauta.textProperty().addListener((observableValue,oldValue,newValue)->{
                filteredList.setPredicate((Predicate<? super Tema>) nota->{
                    if(newValue== null || newValue.isEmpty()){
                        return true;
                    }

                    if(nota.getDescriere().contains(newValue)){
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Tema> sortedList=new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedList);
        });

        descriereText.setOnKeyReleased(e->{
            descriereText.textProperty().addListener((observableValue,oldValue,newValue)->{
                filteredList.setPredicate((Predicate<? super Tema>) nota->{
                    if(newValue== null || newValue.isEmpty()){
                        return true;
                    }

                    if(nota.getDescriere().contains(newValue)){
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Tema> sortedList=new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedList);
        });


    }

    public List<Tema> getDataTeme(){
        return this.service.getPieceDataT(from,to-1);

    }
    private Node createDateTeme(Integer page) {
        from=Math.min(Math.max(nrRow * (page ), 0), service.getTemeLista().size());//page*nrRow;
        to=Math.min(from+nrRow,service.getTemeLista().size());//        int toIndex=Math.min(Math.max(nrRow * page, fromIndex), service.getStudentiLista().size());//
        tableView.setItems(FXCollections.observableList(this.getDataTeme()));
        return tableView;
    }
    private Node createDate(Integer page) {
        int fromIndex=Math.min(Math.max(nrRow * (page ), 0), service.getTemeLista().size());//page*nrRow;
        int toIndex=Math.min(fromIndex+nrRow,service.getTemeLista().size());//        int toIndex=Math.min(Math.max(nrRow * page, fromIndex), service.getStudentiLista().size());//
        tableView.setItems(FXCollections.observableArrayList(service.getTemeLista().subList(fromIndex,toIndex)));
        return tableView;
    }

    private void loadTableSt() {
        tableView.setItems(observableList);
        descriere.setCellValueFactory(new PropertyValueFactory<Tema,String>("Descriere"));
        deadline.setCellValueFactory(new PropertyValueFactory<Tema,Integer>("Deadline"));
        primire.setCellValueFactory(new PropertyValueFactory<Tema,Integer>("Primire"));
        tableView.getSelectionModel().selectedItemProperty().addListener((observer, oldData, newData)-> showDetails(newData));
        tableView.setEditable(true);
        descriere.setCellFactory(TextFieldTableCell.forTableColumn());
        deadline.setCellFactory(TextFieldTableCell.<Tema, Integer>forTableColumn(new IntegerStringConverter()));
        primire.setCellFactory(TextFieldTableCell.<Tema, Integer>forTableColumn(new IntegerStringConverter()));



    }

    private void showDetails(Tema t) {
        if(t!=null){
            descriereText.setText(t.getDescriere());
            deadlineText.setText(String.valueOf(t.getDeadline()));
            primireText.setText(String.valueOf(t.getPrimire()));
        }
    }
    @FXML
    public void clickAdd(MouseEvent e){
        try{
            int idmaxi=0;
            for (Tema st:service.getTemeLista()) {
                if (Integer.parseInt(st.getID())>idmaxi)
                    idmaxi=Integer.parseInt(st.getID());
            }
            idmaxi++;
            Tema t= new Tema(String.valueOf(idmaxi), descriereText.getText(), Integer.parseInt(deadlineText.getText()),Integer.parseInt(primireText.getText()));
            service.addTema(t);
            lblT.setVisible(true);
        }catch (ValidationException  | NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("EROARE ADAUGARE TEMA");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }
    }

    @FXML
    void clickDel(MouseEvent event) {
        try{
            Tema t=service.findTemaDupaDescriere(descriereText.getText());
            try{
            service.deleteNotaT(t);}
            catch (Exception ex){}
            service.deleteTema(t.getID());}
        catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("EROARE STERGERE TEMA");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }

    }
    /*
    @FXML
    void clickUpd(MouseEvent event) {
        Tema tema=service.findTemaDupaDescriere(descriereText.getText());
        Tema t = new Tema(tema.getID(), descriereText.getText(),Integer.parseInt(deadlineText.getText()), Integer.parseInt(primireText.getText()));
        try{
            service.updateTema(t);}
        catch (ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("EROARE UPDATE TEMA");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());
            alert.show();
        }
    }*/

    @Override
    public void update(TemaEvent tema) {
        if(tema.getType() == ChangeEventType.ADD){
            observableList.add(tema.getData());
            loadTableSt();
            pagination.setPageFactory(this::createDateTeme);


        }
        if(tema.getType() == ChangeEventType.DELETE){
            observableList.remove(tema.getOldData());
            loadTableSt();
            pagination.setPageFactory(this::createDateTeme);



        }
        if(tema.getType()== ChangeEventType.UPDATE){
            observableList.remove(tema.getOldData());
            observableList.add(tema.getData());
            loadTableSt();
            pagination.setPageFactory(this::createDateTeme);
        }
    }


    public void onDescriereChange(TableColumn.CellEditEvent<Tema, String> temaStringCellEditEvent) {
        int k=0;
        Tema tema=tableView.getSelectionModel().getSelectedItem();
        String descriere=tema.getDescriere();
        try{
        tema.setDescriere(temaStringCellEditEvent.getNewValue());
        service.updateTema(tema);}
        catch (ValidationException e){
            k++;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare ");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }
        if(k>0){
            tema.setDescriere(descriere);
        }

    }

    public void onDeadlineChange(TableColumn.CellEditEvent<Tema, Integer> temaIntegerCellEditEvent) {
        try{
        Tema tema=tableView.getSelectionModel().getSelectedItem();
        tema.setDeadline(temaIntegerCellEditEvent.getNewValue());
        service.updateTema(tema);}
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("EROARE");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }
    }

    public void onPrimireChange(TableColumn.CellEditEvent<Tema, Integer> temaIntegerCellEditEvent) {
        try{
        Tema tema=tableView.getSelectionModel().getSelectedItem();
        tema.setPrimire(temaIntegerCellEditEvent.getNewValue());
        service.updateTema(tema);}
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
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
    public void clickBack(MouseEvent event) throws IOException {

        back.getScene().getWindow().hide();
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
        int k=0;
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene=new Scene(root, 560,370);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void click(ActionEvent event){
        if(event.getSource()==view){

            panePP.setVisible(true);
            new FadeIn(panePP).play();
            paneA.setVisible(false);
            new BounceIn(hbox).play();
            label.setText("View teme");
            lblT.setVisible(false);
        }
        if(event.getSource()==add){
            descriereText.setText("");
            deadlineText.setText("");
            primireText.setText("");
            panePP.setVisible(false);
            paneA.setVisible(true);
            new FadeIn(paneA);
            new BounceIn(hbox).play();
            label.setText("Adauga tema");
        }
    }
}

