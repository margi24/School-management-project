package sample;

import domain.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import service.Service;

public class ControllerShowSt {
    @FXML
    private Label l3;

    @FXML
    private Label l1;

    @FXML
    private Label l2;

    @FXML
    private Label l4;
    @FXML
    private javafx.scene.control.Button btnIesire;


    private Service service;

    public void setService(Service service, String numeStudent){
        this.service=service;
        Student st=service.findStudentDupaNume(numeStudent);
        l1.setText(st.getID());
        l2.setText(st.getNume());
        l3.setText(String.valueOf(st.getGrupa()));
        l4.setText(st.getEmail());

    }
    @FXML
    public void clickIesire(javafx.event.ActionEvent event) {
        Stage stage=(Stage)btnIesire.getScene().getWindow();
        stage.close();
    }
}
