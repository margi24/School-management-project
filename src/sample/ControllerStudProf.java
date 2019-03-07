package sample;

import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import domain.Student;
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
import view.StudentEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerStudProf implements Observer<StudentEvent> {
    private Service service;
    private ObservableList<Student> observableList;

    @FXML
    private TextField nume;
    @FXML
    private TextField grupa;
    @FXML
    private TextField email;
    @FXML
    private TableView<Student> tableView;
    @FXML
    private TableColumn<Student, String> numecol;
    @FXML
    private TableColumn<Student, Integer> grupacol;
    @FXML
    private TableColumn<Student, String> emailcol;
    @FXML
    private Pagination pagination;
    @FXML
    private Button view;
    @FXML
    private Button add;
    @FXML
    private Pane panePP;
    @FXML
    private TextField txtCauta;
    @FXML
    private Pane paneAdd;
    @FXML
    private HBox hbox;
    @FXML
    private Label label;
    @FXML
    ImageView exit;
    @FXML
    ImageView mini;
    @FXML
    ImageView back;
    @FXML
    private Label lbl;
    private FilteredList<Student> filteredList;
    private List<Student> studentList;
    private  final  static  int nrRow=5;
    public int from=0;
    public int to=0;
    public ControllerStudProf() { }

    private double xOffset=0;
    private double yOffset=0;


    public void setService(Service service){

        this.service = service;
        this.service.studentEventObservable.addObserver(this);
        studentList=new ArrayList<>(service.getStudentiLista().size());
        observableList = FXCollections.observableList(StreamSupport.stream(service.getAllStudenti().spliterator(), false).collect(Collectors.toList()));
        int pagSize=this.service.sizeSt()/this.nrRow+1;
        pagination.setPageCount(pagSize);
        pagination.setPageFactory(this::createPageStudent);
        panePP.setVisible(true);
        paneAdd.setVisible(false);
        TextFields.bindAutoCompletion(txtCauta,service.getNumeStudenti());
        filteredList=new FilteredList<>(observableList,e->true);

        lbl.setVisible(false);
        loadTextField();
        loadTableSt();
    }

    private void loadTextField() {
        txtCauta.setOnKeyReleased(e->{
            txtCauta.textProperty().addListener((observableValue,oldValue,newValue)->{
                filteredList.setPredicate((Predicate<? super Student>) nota->{
                    if(newValue== null || newValue.isEmpty()){
                        return true;
                    }

                    if(nota.getNume().contains(newValue)){
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Student> sortedList=new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedList);
        });
    }

    public List<Student>getDataStudents(){
        //int size=service.sizeSt();
        //if(to<size)to=size;
        return this.service.getPieceDataSt(from,to-1);

    }

    private Node createPageStudent(int pageIndex) {
        this.from = Math.min(Math.max(nrRow * pageIndex , 0), service.sizeSt());
        this.to =Math.min(from+nrRow,service.getStudentiLista().size());
        this.tableView.setItems(FXCollections.observableList(this.getDataStudents()));
        return this.tableView;
    }
    private Node createPage(int page) {
        int fromIndex=Math.min(Math.max(nrRow * page , 0), service.getStudentiLista().size());//page*nrRow;
        int toIndex=Math.min(fromIndex+nrRow,service.getStudentiLista().size());// Math.min(Math.max(nrRow * page, fromIndex), service.getStudentiLista().size());
        tableView.setItems(FXCollections.observableArrayList(service.getStudentiLista().subList(fromIndex,toIndex)));
        int numP=studentList.size();
        if(studentList.size()%nrRow!=0){
            numP++;
        }
        pagination.setPageCount(numP);
        return tableView;
    }

    private void loadTableSt() {
        tableView.setItems(observableList);
        numecol.setCellValueFactory(new PropertyValueFactory<Student,String>("Nume"));
        grupacol.setCellValueFactory(new PropertyValueFactory<Student,Integer>("Grupa"));
        emailcol.setCellValueFactory(new PropertyValueFactory<Student,String>("Email"));
        tableView.getSelectionModel().selectedItemProperty().addListener((observer, oldData, newData)-> showDetails(newData));
        tableView.setEditable(true);
        numecol.setCellFactory(TextFieldTableCell.forTableColumn());
        grupacol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        emailcol.setCellFactory(TextFieldTableCell.forTableColumn());


    }

    private void showDetails(Student st) {
        if(st!=null){
            nume.setText(st.getNume());
            email.setText(st.getEmail());
            grupa.setText(String.valueOf(st.getGrupa()));
        }
    }

    @Override
    public void update(StudentEvent studentEvent) {
        if(studentEvent.getType() == ChangeEventType.ADD){
            observableList.add(studentEvent.getData());
            pagination.setPageFactory(this::createPageStudent);
            loadTableSt();

        }
        if(studentEvent.getType() == ChangeEventType.DELETE){
            observableList.remove(studentEvent.getOldData());
            pagination.setPageFactory(this::createPageStudent);
            loadTableSt();
        }
        if(studentEvent.getType()== ChangeEventType.UPDATE){
            observableList.remove(studentEvent.getOldData());
            observableList.add(studentEvent.getData());
            pagination.setPageFactory(this::createPageStudent);
            loadTableSt();

        }
    }

    public ObservableList getList(){
        return observableList;
    }


    @FXML
    void clickAdd(MouseEvent event) {
        try{
            int idmaxi=0;
            for (Student st:service.getStudentiLista()) {
                if (Integer.parseInt(st.getID())>idmaxi)
                    idmaxi=Integer.parseInt(st.getID());
            }
            idmaxi++;
            Student st= new Student(String.valueOf(idmaxi), nume.getText(), Integer.parseInt(grupa.getText()),email.getText());
            service.addStudent(st);
            lbl.setVisible(true);
            lbl.setText("Student adaugat");

        }catch (ValidationException | NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("EROARE ADAUGARE STUDENT");
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
            Student st =service.findStudentDupaNume(nume.getText());
            service.deleteNotaSt(st);
            service.deleteStudent(st.getID());}
        catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("EROARE STERGERE STUDENT");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }

    }
    @FXML
    void click(ActionEvent event){
        if(event.getSource()==view){

            panePP.setVisible(true);
            new FadeIn(panePP).play();
            paneAdd.setVisible(false);
            new BounceIn(hbox).play();
            label.setText("View studenti");
        }
        else{
            nume.setText("");
            grupa.setText("");
            email.setText("");
            lbl.setVisible(false);
            panePP.setVisible(false);
            paneAdd.setVisible(true);
            new FadeIn(paneAdd);
            new BounceIn(hbox).play();
            label.setText("Adauga student");
        }
    }
/*
    @FXML
    void clickUpd(ActionEvent event) {
        Student st =service.findStudentDupaNume(nume.getText());
        Student student = new Student(st.getID(), nume.getText(),Integer.parseInt(grupa.getText()), email.getText());
        try{
            service.updateStudent(student);}
        catch (ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("EROARE UPDATE STUDENT");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());
            alert.show();
        }
    }*/

    public void onNumeChange(TableColumn.CellEditEvent<Student, String> studentStringCellEditEvent) {
        Student student=tableView.getSelectionModel().getSelectedItem();
        student.setNume(studentStringCellEditEvent.getNewValue());
        service.updateStudent(student);

    }

    public void onGrupaChange(TableColumn.CellEditEvent<Student, Integer> studentIntegerCellEditEvent) {
        Student student=tableView.getSelectionModel().getSelectedItem();
        student.setGrupa(studentIntegerCellEditEvent.getNewValue());
        service.updateStudent(student);

    }

    public void onEmailChange(TableColumn.CellEditEvent<Student, String> studentStringCellEditEvent) {
        Student student=tableView.getSelectionModel().getSelectedItem();
        student.setEmail(studentStringCellEditEvent.getNewValue());
        service.updateStudent(student);

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
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/meniuProfAdmin.fxml"));
        root = loader.load();
        ControllerProfA controller = loader.getController();
        controller.setService(service);
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
