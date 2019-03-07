package sample;


import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;
import service.Service;
import validation.ValidationException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class ControllerFiltrari {
    @FXML
    private RadioButton btnFiltrareTeme;
    @FXML
    private RadioButton btnNoteStudent;
    @FXML
    private RadioButton btnFiltrareGrupa;
    @FXML
    private RadioButton btnData;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ComboBox<String> comboBoxTeme;
    @FXML
    private TextField studenti;
    @FXML
    private ComboBox<String> comboBoxGrupa;
    @FXML
    private DatePicker dataInitiala;
    @FXML
    private DatePicker dataFinala;
    @FXML
    private PieChart pieChart;
    @FXML
    private ImageView back;
    private double xOffset=0;
    private double yOffset=0;
    private Service service;
    private ToggleGroup group=new ToggleGroup();
    private String fileName="D:\\an2\\map\\labMare\\src\\generatePdf\\filtrare";
    public ControllerFiltrari() {
    }

    public void setService(Service service) {
        this.service=service;
        loadComboBoxes();
        btnNoteStudent.setToggleGroup(group);
        btnFiltrareTeme.setToggleGroup(group);
        btnFiltrareGrupa.setToggleGroup(group);
        btnData.setToggleGroup(group);
        ObservableList<PieChart.Data>pieChartData=FXCollections.observableArrayList(new PieChart.Data("filtrari",100));
        pieChart.setData(pieChartData);
        TextFields.bindAutoCompletion(studenti,service.getNumeStudenti());
    }


    private void loadComboBoxes() {
        List<String> listaNume=service.getNumeTeme();
        ObservableList<String> observable= FXCollections.observableList(listaNume);
        comboBoxTeme.setItems(observable);
        List<String> listaNumeS=service.getNumeStudenti();
        List<String> listaNumeG=service.getNrGrupe();
        ObservableList<String> observableG= FXCollections.observableList(listaNumeG);
        comboBoxGrupa.setItems(observableG);

    }

    @FXML
    void clickBtnFiltrareData(ActionEvent event) {
        try {
            comboBoxGrupa.getSelectionModel().selectFirst();
            comboBoxTeme.getSelectionModel().selectFirst();
            studenti.setPromptText("Student");
            List<Nota> note = service.filtrareData(dataInitiala.getValue(), dataFinala.getValue());
            int sub5 = 0;
            int n57 = 0;
            int n79 = 0;
            int n10 = 0;
            for (Nota n : note) {
                if (n.getNota() < 5) {
                    sub5++;
                }
                if (n.getNota() >= 5 && n.getNota() <= 7) {
                    n57++;
                }
                if (n.getNota() > 7 && n.getNota() <= 9) {
                    n79++;
                }
                if (n.getNota() > 9) {
                    n10++;
                }
            }
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("sunt " + String.valueOf(sub5) + " note < 5", sub5),
                    new PieChart.Data("sunt " + String.valueOf(n57) + " note intre 5-7", n57),
                    new PieChart.Data("sunt " + String.valueOf(n79) + " note intre 7-9", n79),
                    new PieChart.Data("sunt " + String.valueOf(n10) + " note > 9", n10));
            pieChart.setData(pieChartData);

        }
        catch (ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORRR");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Introduceti data");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }

    }

    @FXML
    void clickBtnFiltrareGrupa(ActionEvent event) {
        try {
            dataFinala.setPromptText("Data end");
            dataInitiala.setPromptText("Data start");
            studenti.setPromptText("Student");
            String numeS = comboBoxTeme.getValue();
            Tema s = service.findTemaDupaDescriere(numeS);
            String nrG = comboBoxGrupa.getValue();
            List<Nota> note = service.filtrareNoteGrupa(s, Integer.parseInt(nrG));
            int sub5 = 0;
            int n57 = 0;
            int n79 = 0;
            int n10 = 0;
            for (Nota n : note) {
                if (n.getNota() < 5) {
                    sub5++;
                }
                if (n.getNota() >= 5 && n.getNota() <= 7) {
                    n57++;
                }
                if (n.getNota() > 7 && n.getNota() <= 9) {
                    n79++;
                }
                if (n.getNota() > 9) {
                    n10++;
                }
            }
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("sunt " + String.valueOf(sub5) + " note < 5", sub5),
                    new PieChart.Data("sunt " + String.valueOf(n57) + " note intre 5-7", n57),
                    new PieChart.Data("sunt " + String.valueOf(n79) + " note intre 7-9", n79),
                    new PieChart.Data("sunt " + String.valueOf(n10) + " note > 9", n10));
            pieChart.setData(pieChartData);
        }catch (ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORRR");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Introduceti tema si grupa");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }

    }

    @FXML
    void clickBtnFiltrareNoteStudent(ActionEvent event) {
        try{
        comboBoxGrupa.getSelectionModel().selectFirst();
        comboBoxTeme.getSelectionModel().selectFirst();
        dataFinala.setPromptText("Data end");
        dataInitiala.setPromptText("Data start");
        String numeS=studenti.getText();
        Student s=service.findStudentDupaNume(numeS);
        List<Nota> note=service.filtrareNoteStudent(s);
        int sub5=0;
        int n57=0;
        int n79=0;
        int n10=0;
        for(Nota n: note){
            if(n.getNota()<5){
                sub5++;
            }if(n.getNota()>=5 && n.getNota()<=7){
                n57++;
            }if(n.getNota()>7 && n.getNota()<=9){
                n79++;
            }if(n.getNota()>9){
                n10++;
            }
        }
        ObservableList<PieChart.Data>pieChartData=FXCollections.observableArrayList(
                new PieChart.Data("sunt "+String.valueOf(sub5)+" note < 5",sub5),
                new PieChart.Data("sunt "+String.valueOf(n57)+" note intre 5-7",n57),
                new PieChart.Data("sunt "+String.valueOf(n79)+" note intre 7-9",n79),
                new PieChart.Data("sunt "+String.valueOf(n10)+" note > 9",n10));
        pieChart.setData(pieChartData);
        }
        catch (ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORRR");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Introduceti student");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }


    }

    @FXML
    void clickBtnFiltrareTema(ActionEvent event) {
        try {
            comboBoxGrupa.getSelectionModel().selectFirst();
            dataFinala.setPromptText("Data end");
            dataInitiala.setPromptText("Data start");
            studenti.setPromptText("Student");
            int sub5 = 0;
            int n57 = 0;
            int n79 = 0;
            int n10 = 0;

            String numeS = comboBoxTeme.getValue();
            Tema s = service.findTemaDupaDescriere(numeS);
            List<Nota> note = service.filtrareNoteTema(s);
            for (Nota n : note) {
                if (n.getNota() < 5) {
                    sub5++;
                }
                if (n.getNota() >= 5 && n.getNota() <= 7) {
                    n57++;
                }
                if (n.getNota() > 7 && n.getNota() <= 9) {
                    n79++;
                }
                if (n.getNota() > 9) {
                    n10++;
                }
            }
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("sunt " + String.valueOf(sub5) + " note < 5", sub5),
                    new PieChart.Data("sunt " + String.valueOf(n57) + " note intre 5-7", n57),
                    new PieChart.Data("sunt " + String.valueOf(n79) + " note intre 7-9", n79),
                    new PieChart.Data("sunt " + String.valueOf(n10) + " note > 9", n10));
            pieChart.setData(pieChartData);
        }catch (ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORRR");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Introduceti tema");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("../cssDesign/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialogPane.setHeader(null);
            alert.show();
        }


    }

    public void click(ActionEvent event){
           try {
               JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
               int returnValue = jfc.showOpenDialog(null);

               Document document = new Document(PageSize.A4);
               if (returnValue == JFileChooser.APPROVE_OPTION) {
                   File selectedFile = jfc.getSelectedFile();
                   PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(selectedFile.getAbsolutePath()+ ".pdf"));

               document.open();
               WritableImage image = pieChart.snapshot(new SnapshotParameters(), null);
               ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
               ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteOutput);
               Image img = Image.getInstance(byteOutput.toByteArray());
               document.add(img);
               document.close();
               writer.close();
           }

           }catch (Exception ex){
               ex.printStackTrace();
           }


    }

    public void clickBack(MouseEvent mouseEvent) throws IOException {
        back.getScene().getWindow().hide();
        Parent root;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../sample/note.fxml"));
        root = loader.load();
        ControllerNota controller = loader.getController();
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
        Scene scene=new Scene(root, 751,567);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }
    }


/*
    @FXML
    public void clickBtnFiltrareTema(ActionEvent event) {
        String numeS=comboBoxTeme.getValue();
        Tema s=service.findTemaDupaDescriere(numeS);
        List<Nota> note=service.filtrareNoteTema(s);
        ObservableList<Nota> notaObservableList= FXCollections.observableList(StreamSupport.stream(note.spliterator(), false).collect(Collectors.toList()));
        tabelFiltrari.setItems(notaObservableList);
        colSt.setCellValueFactory(new PropertyValueFactory<Nota,String>("numeStudent"));
        colT.setCellValueFactory(new PropertyValueFactory<Nota,String>("descriereTema"));
        colN.setCellValueFactory(new PropertyValueFactory<Nota,Double>("Nota"));

    }
    @FXML
    public void clickBtnFiltrareNoteStudent(ActionEvent event) {
        String numeS=comboBoxStudenti.getValue();
        Student s=service.findStudentDupaNume(numeS);
        List<Nota> note=service.filtrareNoteStudent(s);
        ObservableList<Nota> notaObservableList= FXCollections.observableList(StreamSupport.stream(note.spliterator(), false).collect(Collectors.toList()));
        tabelFiltrari.setItems(notaObservableList);
        colSt.setCellValueFactory(new PropertyValueFactory<Nota,String>("numeStudent"));
        colT.setCellValueFactory(new PropertyValueFactory<Nota,String>("descriereTema"));
        colN.setCellValueFactory(new PropertyValueFactory<Nota,Double>("Nota"));

    }
    @FXML
    public void clickBtnFiltrareGrupa(ActionEvent event){
        String numeS=comboBoxTeme.getValue();
        Tema s=service.findTemaDupaDescriere(numeS);
        String nrG=comboBoxGrupa.getValue();
        List<Nota> note=service.filtrareNoteGrupa(s, Integer.parseInt(nrG));
        ObservableList<Nota> notaObservableList= FXCollections.observableList(StreamSupport.stream(note.spliterator(), false).collect(Collectors.toList()));
        tabelFiltrari.setItems(notaObservableList);
        colSt.setCellValueFactory(new PropertyValueFactory<Nota,String>("numeStudent"));
        colT.setCellValueFactory(new PropertyValueFactory<Nota,String>("descriereTema"));
        colN.setCellValueFactory(new PropertyValueFactory<Nota,Double>("Nota"));
    }

    @FXML
    public void clickBtnFiltrareData(ActionEvent event){
        List<Nota> note=service.filtrareData(dataInitiala.getValue(), dataFinala.getValue());
        ObservableList<Nota> notaObservableList= FXCollections.observableList(StreamSupport.stream(note.spliterator(), false).collect(Collectors.toList()));
        tabelFiltrari.setItems(notaObservableList);
        colSt.setCellValueFactory(new PropertyValueFactory<Nota,String>("numeStudent"));
        colT.setCellValueFactory(new PropertyValueFactory<Nota,String>("descriereTema"));
        colN.setCellValueFactory(new PropertyValueFactory<Nota,Double>("Nota"));
    }*/
