package GUI.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import domain.Profesor;
import domain.Student;
import domain.Utilizator;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import service.ProfesorService;
import service.StudentService;
import service.UtilizatorService;
import utils.DataChanged;
import utils.GUIUtils;
import utils.Observer;

import static utils.GUIUtils.comboBoxProfStudent;

public class AdminStudentiController implements Observer<DataChanged> {
    private final ObservableList<Student> model = FXCollections.observableArrayList();

    @FXML
    Pagination pagination;
    private int itemsPerPage = 6;
    private int currentPageIndex ;
    private ProfesorService serviceProf;
    private StudentService serviceStudent;

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
    TableColumn<Student, String> ColumnProfesor;
    @FXML
    JFXButton addButton;
    @FXML
    JFXButton editButton;
    @FXML
    JFXButton deleteButton;
    @FXML
    AnchorPane mainPane;
    @FXML
    JFXTextField searchBar;

    private void setLastPage() {
        int numOfPages = 1;
        if (model.size() % itemsPerPage == 0) {
            numOfPages = model.size() / itemsPerPage;
        } else if (model.size() > itemsPerPage) {
            numOfPages = model.size() / itemsPerPage + 1;
        }
        pagination.setPageCount(numOfPages);
    }

    public void updatePersonView() {
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage +
                        itemsPerPage : model.size())));
    }

    public void setService(StudentService serviceStudent, ProfesorService serviceProf){
        this.serviceStudent=serviceStudent;
        this.serviceProf=serviceProf;
        model.setAll(this.serviceStudent.getAll());
        setLastPage();

        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            currentPageIndex = newValue.intValue();
            updatePersonView();
        });
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage
                        + itemsPerPage : model.size())));
    }

    @FXML
    public void initialize() {

        ColumnId.setCellValueFactory(cellData -> {
            Student current_item = cellData.getValue();
            return new ReadOnlyStringWrapper(current_item.getID());
        });
        ColumnNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        ColumnPrenume.setCellValueFactory(new PropertyValueFactory<>("prenume"));
        ColumnGrupa.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        ColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        ColumnProfesor.setCellValueFactory(cellData -> {
            Student current_item = cellData.getValue();
            Profesor profesor=serviceProf.find(current_item.getIndrumatorLab());
            if(profesor==null) return new ReadOnlyStringWrapper("-");
            else return new ReadOnlyStringWrapper(profesor.getNume());
        });
    }

    @Override
    public void update(DataChanged dataChanged) {
        model.setAll(this.serviceStudent.getAll());
        setLastPage();
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage
                        + itemsPerPage : model.size())));
    }


    @FXML
    public void handlePane(){
        deleteButton.setDisable(true);
        editButton.setDisable(true);
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleTable(){
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                deleteButton.setDisable(true);
                editButton.setDisable(true);
            }
            else{
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            }
        });
    }

    @FXML
    public void handleSearch(){
        model.setAll(this.serviceStudent.filtreazaStudentiKeyword(this.searchBar.getText()));
        setLastPage();
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage
                        + itemsPerPage : model.size())));
    }

    @FXML
    public void handleAdd(){
        Node body= GUIUtils.addStudentContent();
        ObservableList<String> dataProfi = FXCollections.observableArrayList(serviceProf.listaNumeId());
        comboBoxProfStudent.setItems(dataProfi);
        comboBoxProfStudent.setValue(dataProfi.get(0));
        EventHandler edit= event -> serviceStudent.add(
                new Student(GUIUtils.textFieldIdStudent.getText(),
                        GUIUtils.textFieldNumeStudent.getText(), GUIUtils.textFieldPrenumeStudent.getText(),
                        GUIUtils.textFieldGrupaStudent.getText(),GUIUtils.textFieldEmailStudent.getText(),
                        comboBoxProfStudent.getValue().split(":")[1])
        );
        GUIUtils.setDialouge(mainPane,"Adaugare student", edit,body);
    }

    @FXML
    public void handleEdit(){
        Student student = tableView.getSelectionModel().getSelectedItem();
        Node body = GUIUtils.editStudentContent(student);
        ObservableList<String> dataProfi = FXCollections.observableArrayList(serviceProf.listaNumeId());
        comboBoxProfStudent.setItems(dataProfi);
        Profesor profesor=serviceProf.find(student.getIndrumatorLab());
        comboBoxProfStudent.setValue(profesor.getNume()+":"+profesor.getID());

        EventHandler edit = event -> serviceStudent.update(new Student(student.getID(),
                GUIUtils.textFieldNumeStudent.getText(), GUIUtils.textFieldPrenumeStudent.getText(),
                GUIUtils.textFieldGrupaStudent.getText(),GUIUtils.textFieldEmailStudent.getText(),
                comboBoxProfStudent.getValue().split(":")[1]
        ));
        GUIUtils.setDialouge(mainPane, "Actualizare date", edit, body);
    }

    @FXML
    public void handleDelete(){
        Student student=tableView.getSelectionModel().getSelectedItem();
        EventHandler sterge= event -> serviceStudent.remove(student.getID());
        Node body=new Text("Se vor sterge datele studentului "+ student.getNume()+" "+student.getPrenume()+"!");
        GUIUtils.setDialouge(mainPane,"Stergere", sterge,body);
    }
}
