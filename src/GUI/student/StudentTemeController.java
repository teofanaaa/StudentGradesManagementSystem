package GUI.student;

import domain.Tema;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import service.TemaService;

public class StudentTemeController {
    private final ObservableList<Tema> model = FXCollections.observableArrayList();
    @FXML
    TableView<Tema> tableView;
    @FXML
    TableColumn<Tema, String> ColumnId;
    @FXML
    TableColumn<Tema, String> ColumnDescriere;
    @FXML
    TableColumn<Tema, String> ColumnDeadline;
    @FXML
    TableColumn<Tema, Void> ColumnPredare;

    private TemaService service;

    public void setService(TemaService service){
        this.service=service;
        model.setAll(this.service.getAll());
    }

    @FXML
    public void initialize() {
        ColumnId.setCellValueFactory(cellData -> {
            Tema current_item = cellData.getValue();
            return new ReadOnlyStringWrapper("L"+current_item.getID());
        });
        ColumnDescriere.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        ColumnDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        ColumnPredare.setCellValueFactory(new PropertyValueFactory<>("dataPredare"));

        tableView.setItems(model);
    }

}
