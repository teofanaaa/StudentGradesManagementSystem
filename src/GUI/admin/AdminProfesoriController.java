package GUI.admin;

import com.jfoenix.controls.JFXButton;
import domain.Profesor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import service.ProfesorService;
import utils.DataChanged;
import utils.EventType;
import utils.GUIUtils;
import utils.Observer;
import validator.ValidationException;

public class AdminProfesoriController implements Observer<DataChanged> {
    private final ObservableList<Profesor> model = FXCollections.observableArrayList();

    private ProfesorService service;

    @FXML
    TableView<Profesor> tableView;
    @FXML
    TableColumn<Profesor, String> ColumnId;
    @FXML
    TableColumn<Profesor, String> ColumnNume;
    @FXML
    TableColumn<Profesor, String> ColumnEmail;
    @FXML
    JFXButton addButton;
    @FXML
    JFXButton editButton;
    @FXML
    JFXButton deleteButton;
    @FXML
    AnchorPane mainPane;


    public void setService(ProfesorService service){
        this.service=service;
        if(this.service.getAll().size()!=0) model.setAll(this.service.getAll());
        tableView.setItems(model);
    }

    @FXML
    public void initialize() {

        ColumnId.setCellValueFactory(cellData -> {
            Profesor current_item = cellData.getValue();
            return new ReadOnlyStringWrapper(current_item.getID());
        });
        ColumnNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        ColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        deleteButton.setDisable(true);
        editButton.setDisable(true);
        tableView.getSelectionModel().clearSelection();
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
            if (newSelection != null) {
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            }
            else{
                deleteButton.setDisable(true);
                editButton.setDisable(true);
            }
        });
    }

    @FXML
    public void handleAdd(){
        Node body=GUIUtils.addProfesorContent();
        EventHandler edit= event -> service.add(new Profesor(GUIUtils.textFieldIdProf.getText(),
                GUIUtils.textFieldNumeProf.getText(),GUIUtils.textFieldEmailProf.getText()
        ));
        GUIUtils.setDialouge(mainPane,"Adaugare profesor", edit,body);
    }

    @FXML
    public void handleEdit(){
        Profesor profesor = tableView.getSelectionModel().getSelectedItem();
        Node body = GUIUtils.editProfesorContent(profesor);

        EventHandler edit = event -> service.update(new Profesor(profesor.getID(),
                GUIUtils.textFieldNumeProf.getText(), GUIUtils.textFieldEmailProf.getText()
        ));
        GUIUtils.setDialouge(mainPane, "Actualizare date", edit, body);
    }

    @FXML
    public void handleDelete(){
        Profesor profesor=tableView.getSelectionModel().getSelectedItem();
        EventHandler sterge= event -> service.remove(profesor.getID());
        Node body=new Text("Se vor sterge datele profesorului "+ profesor.getNume()+"!");
        GUIUtils.setDialouge(mainPane,"Stergere", sterge,body);
    }

    @Override
    public void update(DataChanged dataChanged) {
        model.setAll(this.service.getAll());
        tableView.setItems(model);
    }
}
