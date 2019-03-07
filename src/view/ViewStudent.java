package view;/*
package view;

import domain.Student;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import sample.ControllerStudent;
import validation.ValidationException;

public class ViewStudent {
    private ControllerStudent c;
    private HBox box;
    private TextField id;
    private TextField nume;
    private TextField grupa;
    private TextField email;
    private  TableView<Student>tableView;

    public ViewStudent(ControllerStudent c)  {
        tableView=new TableView<Student>();
        this.c = c;
    }

    public BorderPane getView(){

        BorderPane borderPane=new BorderPane();
        borderPane.setLeft(createTableStudents());
        borderPane.setRight(parteDr());
        return borderPane;
    }

    private GridPane parteDr() {
        GridPane gridPane=new GridPane();
        gridPane.add(new Label("id"),0,0);
        gridPane.add(id=new TextField(),1,0);
        gridPane.add(new Label("nume"),0,1);
        gridPane.add(nume=new TextField(),1,1);
        gridPane.add(new Label("grupa"),0,2);
        gridPane.add(grupa=new TextField(),1,2);
        gridPane.add(new Label("email"),0,3);
        gridPane.add(email=new TextField(),1,3);

        box=new HBox();
        Button add=new Button("add");
        Button del=new Button("delete");
        Button upd=new Button("update");

        box.getChildren().addAll(add,del,upd);
        gridPane.add(box,1,5,2,2);
        add.setOnAction(event -> {
            this.addHandler();
        });

        upd.setOnAction(event -> {
            this.updHandler();
        });
        del.setOnAction(event -> {
            this.delHandler();
        });
        return gridPane;
    }

    private void updHandler() {
            c.updStudent(id.getText(), nume.getText(), Integer.parseInt(grupa.getText()),email.getText());
    }

    private void delHandler() {
            c.delStudent(id.getText());
    }



    private void addHandler() {
            c.addStudent(id.getText(), nume.getText(), Integer.parseInt(grupa.getText()),email.getText());
    }

    private StackPane createTableStudents() {
        StackPane stackPane=new StackPane();
        stackPane.getChildren().add(tableView);
        initTableView();
        return stackPane;

    }

    private void initTableView() {
        TableColumn<Student,String> idcol=new TableColumn<>("ID");
        TableColumn<Student,String> numecol=new TableColumn<>("NUME");
        TableColumn<Student,Integer> grupacol=new TableColumn<>("GRUPA");
        TableColumn<Student,String> emailcol=new TableColumn<>("EMAIL");
        tableView.getColumns().addAll(idcol,numecol,grupacol,emailcol);
        idcol.setCellValueFactory(new PropertyValueFactory< Student,String>("ID"));
        numecol.setCellValueFactory(new PropertyValueFactory< Student,String>("Nume"));
        grupacol.setCellValueFactory(new PropertyValueFactory< Student,Integer>("Grupa"));
        emailcol.setCellValueFactory(new PropertyValueFactory< Student,String>("Email"));
        tableView.setItems(c.getList());
        tableView.getSelectionModel().selectedItemProperty().addListener((observer, oldData, newData)-> showDetails(newData));

    }

    private void showDetails(Student st) {
        if(st!=null){
            id.setText(st.getID());
            nume.setText(st.getNume());
            email.setText(st.getEmail());
            grupa.setText(String.valueOf(st.getGrupa()));
        }
    }
}
*/