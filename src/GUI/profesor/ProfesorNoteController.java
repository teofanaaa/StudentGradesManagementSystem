package GUI.profesor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import domain.Nota;
import domain.Profesor;
import domain.Student;
import domain.Tema;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.util.Pair;
import service.NoteService;
import service.StudentService;
import service.TemaService;
import utils.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProfesorNoteController implements Observer<DataChanged> {
    private final ObservableList<Nota> model = FXCollections.observableArrayList();
    @FXML
    TableView<Nota> tableView;
    @FXML
    TableColumn<Nota, String> ColumnIdTema;
    @FXML
    TableColumn<Nota, String> ColumnIdStudent;
    @FXML
    TableColumn<Nota, String> ColumnNumeStudent;
    @FXML
    TableColumn<Nota, String> ColumnNota;
    @FXML
    TableColumn<Nota, String> ColumnData;
    @FXML
    Pagination pagination;
    private int itemsPerPage = 7;
    private int currentPageIndex ;
    @FXML
    AnchorPane mainPane;
    @FXML
    JFXButton notaButton;

    @FXML
    private JFXComboBox<String> comboBoxLabFilter;
    @FXML
    private JFXComboBox<String> comboBoxNumeFilter;
    @FXML
    private JFXComboBox<String> comboBoxGroupFilter;
    @FXML
    private JFXDatePicker datePickerFilter1;
    @FXML
    private JFXDatePicker datePickerFilter2;
    @FXML
    private JFXButton buttonClear;


    NoteService serviceNote;
    StudentService serviceStudent;
    TemaService serviceTema;
    Profesor profesor;

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
        if(serviceStudent.filtreazaStudentiProf(profesor.getID()).size()==0)
            this.notaButton.setDisable(true);
        else this.notaButton.setDisable(false);
    }

    public void setService(NoteService serviceNote, StudentService serviceStudent, TemaService serviceTema,Profesor profesor){
        this.serviceNote=serviceNote;
        this.serviceStudent=serviceStudent;
        this.serviceTema=serviceTema;
        this.profesor=profesor;
        ColumnNumeStudent.setCellValueFactory(cellData -> {
            Nota current_item = cellData.getValue();
            Student student=serviceStudent.find(current_item.getStudentID());
            return new ReadOnlyStringWrapper(student.getNume()+ " "+student.getPrenume());
        });

        model.setAll(this.serviceNote.getAll());
        setLastPage();

        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage
                        + itemsPerPage : model.size())));
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            currentPageIndex = newValue.intValue();
            updatePersonView();
        });

        if(serviceStudent.filtreazaStudentiProf(profesor.getID()).size()==0)
            this.notaButton.setDisable(true);
        else this.notaButton.setDisable(false);

        initComboBox();
    }

    @FXML
    public void handleNotare(){
        Node body= GUIUtils.addNotaContent();
        GUIUtils.comboBoxIdStudentNota.setDisable(false);
        ObservableList<String> data = FXCollections.observableArrayList(
                serviceTema.getAll().stream().map(Tema::getID).collect(Collectors.toList())
        );
        ObservableList<String> dataStudent = FXCollections.observableArrayList(
                serviceStudent.filtreazaStudentiProf(profesor.getID())
                .stream()
                .map(s->s.getID()+":"+s.getNume()+" "+s.getPrenume())
                .collect(Collectors.toList())
        );
        GUIUtils.comboBoxIdStudentNota.setItems(dataStudent);
        if(dataStudent.size()>0) GUIUtils.comboBoxIdStudentNota.setValue(dataStudent.get(0));
        GUIUtils.comboBoxIdTemaNota.setItems(data);
        GUIUtils.comboBoxIdTemaNota.setValue(data.get(0));
        GUIUtils.textAreaFeedbackNota.setText(" ");

        EventHandler notare= event -> serviceNote.add(GUIUtils.comboBoxIdStudentNota.getValue().split(":")[0],
                GUIUtils.comboBoxIdTemaNota.getValue(),
                GUIUtils.textFieldNota.getText(),
                GUIUtils.textAreaFeedbackNota.getText(),
                GUIUtils.checkBoxMotivatNota.isSelected());
        GUIUtils.setDialouge(mainPane,"Notare studenti", notare,body);
    }

    @FXML
    public void initialize() {
        ColumnIdStudent.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        ColumnIdTema.setCellValueFactory(new PropertyValueFactory<>("temaID"));
        ColumnNota.setCellValueFactory(new PropertyValueFactory<>("notaProf"));
        ColumnData.setCellValueFactory(new PropertyValueFactory<>("dataCurenta"));

        this.comboBoxNumeFilter.valueProperty().addListener(o->handleFilter());
        this.comboBoxLabFilter.valueProperty().addListener(o->handleFilter());
        this.comboBoxGroupFilter.valueProperty().addListener(o->handleFilter());
        this.datePickerFilter1.valueProperty().addListener(o->handleFilter());
        this.datePickerFilter2.valueProperty().addListener(o->handleFilter());

    }

    @Override
    public void update(DataChanged dataChanged) {
        model.setAll(this.serviceNote.getAll());
        setLastPage();
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage
                        + itemsPerPage : model.size())));
        if(serviceStudent.filtreazaStudentiProf(profesor.getID()).size()==0)
            this.notaButton.setDisable(true);
        else this.notaButton.setDisable(false);
    }

    private Integer getDate1(){
        if(this.datePickerFilter1.getValue()!=null){
            LocalDate date1 = this.datePickerFilter1.getValue(); // input from your date picker
            Integer week1= Config.getWeek(date1);
            if(Config.getWeekUni(week1)==null)
                return 0;
            else Config.getWeekUni(week1);
        }
        return 0;
    }

    private Integer getDate2(){
        if(this.datePickerFilter2.getValue()!=null){
            LocalDate date2 = this.datePickerFilter2.getValue(); // input from your date picker
            Integer week2=Config.getWeek(date2);
            if(Config.getWeekUni(week2)==null)
                return 15;
            else Config.getWeekUni(week2);
        }
        return 15;
    }

    @FXML
    private void handleFilter() {
        Predicate<Nota> p4 = n->
                serviceNote.find(n.getID())
                        .getDataCurenta().isEmpty() ||
                        this.datePickerFilter1.getValue()==null ||
                        this.datePickerFilter2.getValue()==null ||
                        Integer.parseInt(serviceNote.find(n.getID()).getDataCurenta()) >=getDate1() &&
                                Integer.parseInt(serviceNote.find(n.getID()).getDataCurenta()) <=getDate2();

        Predicate<Nota> p1 = n->
                this.comboBoxLabFilter.getSelectionModel().getSelectedItem()==null ||
                        n.getTemaID()
                                .contains(this.comboBoxLabFilter.getSelectionModel().getSelectedItem());

        Predicate<Nota> p2 = n->
                this.comboBoxNumeFilter.getSelectionModel().getSelectedItem()==null ||
                        this.serviceStudent.find(n.getStudentID()).getPrenume()
                                .contains(this.comboBoxNumeFilter.getSelectionModel().getSelectedItem()) ||
                        this.comboBoxNumeFilter.getSelectionModel().getSelectedItem()
                                .contains(this.serviceStudent.find(n.getStudentID()).getPrenume())||
                        this.serviceStudent.find(n.getStudentID()).getNume()
                                .contains(this.comboBoxNumeFilter.getSelectionModel().getSelectedItem()) ||
                        this.comboBoxNumeFilter.getSelectionModel().getSelectedItem()
                                .contains(this.serviceStudent.find(n.getStudentID()).getNume());

        Predicate<Nota> p3 = n->
                this.comboBoxGroupFilter.getSelectionModel().getSelectedItem()==null ||
                        this.serviceStudent.find(n.getStudentID()).getGrupa()
                                .contains(this.comboBoxGroupFilter.getSelectionModel().getSelectedItem());

        model.setAll(serviceNote.getAll().stream()
                .filter(p4.and(p3).and(p2).and(p1))
                .collect(Collectors.toList()));
        updatePersonView();
    }

    @FXML
    private void handleClear(){
        model.setAll(this.serviceNote.getAll());
        this.comboBoxLabFilter.setValue(null);
        this.comboBoxNumeFilter.setValue(null);
        this.comboBoxGroupFilter.setValue(null);
        this.datePickerFilter2.setValue(null);
        this.datePickerFilter1.setValue(null);
    }

    private void initComboBox(){
        List<String> lista= serviceStudent.getAll().stream().
                map(student->student.getNume()+" "+student.getPrenume()+" (ID:"+student.getID()+")").collect(Collectors.toList());
        ObservableList<String> data = FXCollections.observableArrayList(lista);

        this.comboBoxNumeFilter.setItems(data);
        new AutoCompleteComboBoxListener<>(this.comboBoxNumeFilter);
        new AutoCompleteComboBoxListener<>(this.comboBoxLabFilter);
        new AutoCompleteComboBoxListener<>(this.comboBoxNumeFilter);

        List<String> grupe=serviceStudent.getGrupe();
        ObservableList<String> dataFilter = FXCollections.observableArrayList(
                grupe);
        this.comboBoxGroupFilter.setItems(dataFilter);

        List<String> lab=serviceTema.getAll().stream()
                .map(t->t.getID()).collect(Collectors.toList());
        ObservableList<String> dataLab = FXCollections.observableArrayList(lab);
        this.comboBoxLabFilter.setItems(dataLab);
    }
}
