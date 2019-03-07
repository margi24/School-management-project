package sample;

import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import org.mindrot.jbcrypt.BCrypt;
import service.Service;
import validation.ValidationException;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class ControllerSignUp {
    @FXML
    private ImageView loadI;
    @FXML
    private TextField name;
    @FXML
    private TextField pass;
    @FXML
    private ComboBox<String> combo;
    @FXML
    private ImageView exit;
    @FXML
    private ImageView mini;
    @FXML
    private ImageView bac;
    @FXML
    private Label lbl;
    private PreparedStatement pst;
    private double xOffset=0;
    private double yOffset=0;
    public ControllerSignUp(){}
    private Service service;

    public void setService(Service service) {
        loadI.setVisible(false);
        this.service=service;
        lbl.setVisible(false);
        loadComboBox();
    }
    public void loadComboBox(){
        List<String> listaNume=new ArrayList<>();
        listaNume.add("Student");
        listaNume.add("Profesor");
        listaNume.add("Admin");
        ObservableList<String> observable= FXCollections.observableList(listaNume);
        combo.setItems(observable);
    }
    public void cliclBtnSignUp(ActionEvent event) throws IOException  {
        loadI.setVisible(true);

        try {
            if(combo.getValue()=="Student")
                service.findStudentDupaEmail(name.getText());
            if(combo.getValue()=="Profesor")
                service.findProfEmail(name.getText());
            String parola = BCrypt.hashpw(pass.getText(), BCrypt.gensalt());
            service.addUser( new User(name.getText(), parola, combo.getValue().toLowerCase()));
            lbl.setVisible(true);
        }
        catch (ValidationException  | NullPointerException ex){
            lbl.setVisible(false);
            loadI.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare sign up");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Datele introduse nu sunt corecte sau deja exista");
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

    public void minimize(MouseEvent mouseEvent) {
        Stage stage = (Stage) mini.getScene().getWindow();
        stage.setIconified(true);
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        bac.getScene().getWindow().hide();;
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
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene=new Scene(root, 345,442);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }






























 /*       Connection connection = datahandler.getConnection();
        String insert = "INSERT INTO admini values(" + name.getText() + "," + pass.getText() + ",admini)";
        Statement statement = connection.createStatement();
        statement.executeUpdate(insert);


    }

        connection=datahandler.getConnection();
        try {
            pst=connection.prepareStatement(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            pst.setString(1,name.getText());
            pst.setString(2,pass.getText());
            pst.setString(3,"admin");
            pst.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }}*/
}
