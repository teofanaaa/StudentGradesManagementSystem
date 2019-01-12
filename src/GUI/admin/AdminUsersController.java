package GUI.admin;

import com.jfoenix.controls.JFXTextField;
import domain.Utilizator;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import service.UtilizatorService;
import utils.DataChanged;
import utils.Observer;

public class AdminUsersController implements Observer<DataChanged> {
    private final ObservableList<Utilizator> model = FXCollections.observableArrayList();

    @FXML
    Pagination pagination;
    private int itemsPerPage = 7;
    private int currentPageIndex ;
    private UtilizatorService service;
    @FXML
    JFXTextField searchBar;

    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator, String> ColumnUsername;
    @FXML
    TableColumn<Utilizator, String> ColumnNume;
    @FXML
    TableColumn<Utilizator, String> ColumnTip;

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
        //System.out.println("updatePersonView");
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage +
                        itemsPerPage : model.size())));
    }

    public void setService(UtilizatorService service){
        this.service=service;
        model.setAll(this.service.getAll());
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
        ColumnUsername.setCellValueFactory(cellData -> {
            Utilizator current_item = cellData.getValue();
            return new ReadOnlyStringWrapper(current_item.getID());
        });
        ColumnTip.setCellValueFactory(new PropertyValueFactory<>("tip"));
        ColumnNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
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
    public void handleSearch(){
        model.setAll(this.service.filtreazaUseriKeyword(this.searchBar.getText()));
        setLastPage();
        tableView.getItems().setAll(model.subList(currentPageIndex * itemsPerPage,
                ((currentPageIndex * itemsPerPage + itemsPerPage <= model.size()) ? currentPageIndex * itemsPerPage
                        + itemsPerPage : model.size())));
    }
}
