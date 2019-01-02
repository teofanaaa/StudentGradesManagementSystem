package GUI.student;

import domain.Nota;
import domain.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import service.NoteService;

import java.util.ArrayList;

public class StudentNoteController {
    private final ObservableList<Nota> model = FXCollections.observableArrayList();
    @FXML
    TableView<Nota> tableView;
    @FXML
    TableColumn<Nota, String> ColumnIdTema;
    @FXML
    TableColumn<Nota, String> ColumnNota;
    @FXML
    TableColumn<Nota, String> ColumnData;
    @FXML
    Label nota;
    @FXML
    BarChart<String, Number> chartStudent;

    NoteService service;
    Student student;

    private void chart(){
        XYChart.Series series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data("sub 5", service.nrStudentiMedie(0,5)));
        series1.getData().add(new XYChart.Data("5-6", service.nrStudentiMedie(5,6)));
        series1.getData().add(new XYChart.Data("6-7", service.nrStudentiMedie(6,7)));
        series1.getData().add(new XYChart.Data("7-8", service.nrStudentiMedie(7,8)));
        series1.getData().add(new XYChart.Data("8-9", service.nrStudentiMedie(8,9)));
        series1.getData().add(new XYChart.Data("9-10", service.nrStudentiMedie(9,10)));
        series1.getData().add(new XYChart.Data("10", service.nrStudentiMedie(10,11)));

        chartStudent.getData().addAll(series1);
    }

    public void setService(NoteService service, Student student){
        this.service=service;
        this.student=student;
        ArrayList<Nota> rez=this.service.listaNoteStudent(this.student.getID());
        model.setAll(rez);
        String media= String.format("%.2f", service.getMediaStudent(student.getID()));
        this.nota.setText(media);
        chart();
    }

    @FXML
    public void initialize() {
        ColumnIdTema.setCellValueFactory(new PropertyValueFactory<>("temaID"));
        ColumnNota.setCellValueFactory(new PropertyValueFactory<>("notaProf"));
        ColumnData.setCellValueFactory(new PropertyValueFactory<>("dataCurenta"));
        tableView.setItems(model);
    }
}
