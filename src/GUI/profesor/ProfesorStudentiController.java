package GUI.profesor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import domain.Profesor;
import domain.Student;
import domain.Tema;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import service.NoteService;
import service.ProfesorService;
import service.StudentService;
import service.TemaService;
import utils.Config;
import utils.DataChanged;
import utils.GUIUtils;
import utils.Observer;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ProfesorStudentiController implements Observer<DataChanged> {
    private final ObservableList<Student> model = FXCollections.observableArrayList();

    @FXML
    Pagination pagination;
    private int itemsPerPage = 7;
    private int currentPageIndex ;
    private StudentService serviceStudent;
    private TemaService serviceTema;
    private NoteService serviceNote;
   // private ProfesorService serviceProf;
    @FXML
    TableView<Student> tableView;
    @FXML
    TableColumn<Student, String> ColumnId;
    @FXML
    TableColumn<Student, String> ColumnNume;
    @FXML
    TableColumn<Student, String> ColumnPrenume;
    @FXML
    TableColumn<Student, String> ColumnGrupa;
    @FXML
    TableColumn<Student, String> ColumnEmail;
    @FXML
    JFXButton notaButton;
    @FXML
    AnchorPane mainPane;
    @FXML
    JFXTextField searchBar;
    private Profesor profesor;

    private void setLastPage() {
        int numOfPages = 1;
        if (model.size() % itemsPerPage == 0) {
            numOfPages = model.size() / itemsPerPage;
        } else if (model.size() > itemsPerPage) {
            numOfPages = model.size() / itemsPerPage + 1;
        }
        pagination.setPageCount(numOfPages);
    }

    private void updatePersonView() {
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage +
                        itemsPerPage : model.size())));
    }

    public void setService(StudentService serviceStudent,NoteService serviceNote, TemaService serviceTema,Profesor profesor){
        this.serviceStudent=serviceStudent;
        this.serviceNote=serviceNote;
        this.serviceTema=serviceTema;
        this.profesor=profesor;
        model.setAll(this.serviceStudent.filtreazaStudentiProf(profesor.getID()));
        setLastPage();

        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage
                        + itemsPerPage : model.size())));
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            currentPageIndex = newValue.intValue();
            updatePersonView();
        });
    }

    @FXML
    public void initialize() {
        notaButton.setDisable(true);
        ColumnId.setCellValueFactory(cellData -> {
            Student current_item = cellData.getValue();
            return new ReadOnlyStringWrapper(current_item.getID());
        });
        ColumnNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        ColumnPrenume.setCellValueFactory(new PropertyValueFactory<>("prenume"));
        ColumnGrupa.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        ColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

    }

    @Override
    public void update(DataChanged dataChanged) {
        model.setAll(this.serviceStudent.filtreazaStudentiProf(profesor.getID()));
        setLastPage();
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage
                        + itemsPerPage : model.size())));
    }

    @FXML
    public void handlePane(){
        notaButton.setDisable(true);
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleNoteaza(){
        Node body=GUIUtils.addNotaContent();
        GUIUtils.comboBoxIdStudentNota.setDisable(true);
        ObservableList<String> data = FXCollections.observableArrayList(
            serviceTema.getAll().stream().map(Tema::getID).collect(Collectors.toList())
        );
        Student student=tableView.getSelectionModel().getSelectedItem();
        GUIUtils.comboBoxIdStudentNota.setValue(student.getID()+":"+student.getNume()+" "+student.getPrenume());
        GUIUtils.comboBoxIdTemaNota.setItems(data);
        GUIUtils.comboBoxIdTemaNota.setValue(data.get(0));

        EventHandler notare= event -> serviceNote.add(GUIUtils.comboBoxIdStudentNota.getValue().split(":")[0],
                GUIUtils.comboBoxIdTemaNota.getValue().split(":")[0],
                GUIUtils.textFieldNota.getText(),
                GUIUtils.textAreaFeedbackNota.getText(),
                GUIUtils.checkBoxMotivatNota.isSelected());
        GUIUtils.setDialouge(mainPane,"Notare studenti", notare,body);
    }

    @FXML
    public void handleTable(){
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                notaButton.setDisable(true);
            }
            else{
                notaButton.setDisable(false);
            }
        });
    }

    @FXML
    public void handleSearch(){
        model.setAll(this.serviceStudent.filtreazaStudentiProfKeyword(profesor.getID(),this.searchBar.getText()));
        setLastPage();
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage
                        + itemsPerPage : model.size())));
    }
}
