package GUI.profesor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.text.Text;
import service.TemaService;
import utils.Config;
import utils.DataChanged;
import utils.GUIUtils;
import utils.Observer;
import java.util.Arrays;

public class ProfesorTemeController implements Observer<DataChanged> {
    private final ObservableList<Tema> model = FXCollections.observableArrayList();

    @FXML
    Pagination pagination;
    private int itemsPerPage = 6;
    private int currentPageIndex ;
    private TemaService service;
    @FXML
    TableView<Tema> tableView;
    @FXML
    TableColumn<Tema, String> ColumnId;
    @FXML
    TableColumn<Tema, String> ColumnDescriere;
    @FXML
    TableColumn<Tema, String> ColumnDeadline;
    @FXML
    TableColumn<Tema, String> ColumnDataPredare;
    @FXML
    JFXButton addButton;
    @FXML
    JFXButton editButton;
    @FXML
    JFXButton deleteButton;
//    @FXML
//    JFXButton notaButton;
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

    private void updatePersonView() {
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage +
                        itemsPerPage : model.size())));
    }

    public void setService(TemaService service){
        this.service=service;
        model.setAll(this.service.getAll());
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
        deleteButton.setDisable(true);
        editButton.setDisable(true);
       // notaButton.setDisable(true);
        ColumnId.setCellValueFactory(cellData -> {
            Tema current_item = cellData.getValue();
            return new ReadOnlyStringWrapper(current_item.getID());
        });
        ColumnDescriere.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        ColumnDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        ColumnDataPredare.setCellValueFactory(new PropertyValueFactory<>("dataPredare"));
    }

    @Override
    public void update(DataChanged dataChanged) {
        model.setAll(this.service.getAll());
        setLastPage();
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage
                        + itemsPerPage : model.size())));
    }

    @FXML
    public void handlePane(){
        deleteButton.setDisable(true);
        editButton.setDisable(true);
        //notaButton.setDisable(true);
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleNoteaza(){
//        Node body=new Text("Mhahah");
//
//        GUIUtils.setDialouge(mainPane,"Notare studenti", notare,body);
    }

    @FXML
    public void handleTable(){
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                deleteButton.setDisable(true);

                //notaButton.setDisable(true);
                editButton.setDisable(true);
            }
            else
                {

                editButton.setDisable(false);
                //notaButton.setDisable(false)
                //;
                deleteButton.setDisable(false);
            }
        });
    }

    @FXML
    public void handleSearch(){
        model.setAll(this.service.filtreazaTemaKeyword(this.searchBar.getText()));
        setLastPage();
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage
                        + itemsPerPage : model.size())));
    }

    @FXML
    public void handleAdd(){
        ObservableList<String> data = FXCollections.observableArrayList(
                Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12","13","14")
        );
        GUIUtils.comboBoxDeadlineTema.setItems(data);
        GUIUtils.comboBoxPredareTema.setItems(data);
        if(Config.getWeekUni()!=null) {
            GUIUtils.comboBoxDeadlineTema.setValue(Config.getWeekUni().toString());
            GUIUtils.comboBoxPredareTema.setValue(Config.getWeekUni().toString());
        }
        else{
            GUIUtils.comboBoxDeadlineTema.setValue(data.get(data.size()-1));
            GUIUtils.comboBoxPredareTema.setValue(data.get(data.size()-1));
        }
        Node body= GUIUtils.addTemeContent();

       EventHandler add= event -> service.add(
                new Tema(GUIUtils.textFieldIdTema.getText(),
                        GUIUtils.textFieldDescriereTema.getText(), GUIUtils.comboBoxDeadlineTema.getValue(),
                        GUIUtils.comboBoxPredareTema.getValue())
        );
        GUIUtils.setDialouge(mainPane,"Adaugare tema", add,body);
    }

    @FXML
    public void handleEdit(){
        Tema tema = tableView.getSelectionModel().getSelectedItem();
        Node body = GUIUtils.editTemaContent(tema);
        ObservableList<String> data = FXCollections.observableArrayList(
                Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12","13","14")
        );
        GUIUtils.comboBoxDeadlineTema.setItems(data);
        GUIUtils.comboBoxPredareTema.setItems(data);
        GUIUtils.comboBoxDeadlineTema.setValue(tema.getDeadline());
        GUIUtils.comboBoxPredareTema.setValue(tema.getDataPredare());

        EventHandler edit = event -> service.update(new Tema(tema.getID(),
                GUIUtils.textFieldDescriereTema.getText(),
                GUIUtils.comboBoxDeadlineTema.getValue(),
                GUIUtils.comboBoxPredareTema.getValue())
        );
        GUIUtils.setDialouge(mainPane, "Modificare deadline", edit, body);
    }

    @FXML
    public void handleDelete(){
        Tema tema=tableView.getSelectionModel().getSelectedItem();
        EventHandler sterge= event -> service.remove(tema.getID());
        Node body=new Text("Se vor sterge datele de la tema "+ tema.getID()+"!");
        GUIUtils.setDialouge(mainPane,"Stergere", sterge,body);
    }
}
