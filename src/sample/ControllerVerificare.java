package sample;

import domain.Nota;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import service.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControllerVerificare  {
    @FXML
    private Label labelStudent;
    @FXML
    private Label labelTema;
    @FXML
    private Label labelNota;
    @FXML
    private Label labelPenalizare;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancel;
    public int verif=0;
    public LocalDate localDate;
    private Service service;
    public ControllerVerificare()  {}

    public void setService(Service service, String numeStudent, String descriereNota, String nota, String data, String feetbackk, int verifCheckBox, double penalitati){
        this.service=service;
        labelStudent.setText(numeStudent);
        labelTema.setText(descriereNota);
        if(penalitati<=5){

            if(verifCheckBox==1){
                labelNota.setText(nota);
                labelPenalizare.setText("Student motivat");
            }
            else{
                Double notaFinala=Double.parseDouble(nota)-penalitati;
                labelNota.setText(String.valueOf(notaFinala));
                labelPenalizare.setText(feetbackk);
            }
        }
        else{
            if(verifCheckBox==1){
                labelNota.setText(nota);
                labelPenalizare.setText("Student motivat");
            }
            else {
                labelNota.setText("1");
                labelPenalizare.setText("Studentul a luat nota 1 din cauza penalizarilor");
            }
        }
        verif=verifCheckBox;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        localDate = LocalDate.parse(data, formatter);
    }
    public void clickBtnOK(ActionEvent event) {
        String idNota=service.findStudentDupaNume(labelStudent.getText()).getID()+"#"+service.findTemaDupaDescriere(labelTema.getText()).getID();
        Nota n=new Nota(idNota,service.findStudentDupaNume(labelStudent.getText()).getID(),service.findTemaDupaDescriere(labelTema.getText()).getID(),Double.parseDouble(labelNota.getText()),
                localDate,labelStudent.getText(),labelTema.getText());

            service.addNota(n,labelPenalizare.getText(),verif);
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();


    }

    public void clickBtnCancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
        //Platform.exit();-->>iese din toate ferestrele
    }

}
