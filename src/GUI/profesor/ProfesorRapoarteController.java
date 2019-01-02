package GUI.profesor;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import domain.Student;
import domain.Tema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import service.Rapoarte;
import service.StudentService;
import utils.DataChanged;
import utils.Observable;
import utils.Observer;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ProfesorRapoarteController implements Observer<DataChanged> {
    private Rapoarte rapoarte;
    @FXML
    JFXCheckBox mediaStudenti;
    @FXML
    JFXCheckBox studentiNuExamen;
    @FXML
    JFXCheckBox mediaLaboratoare;
    @FXML
    JFXCheckBox studentiTemePredateLaTimp;
    @FXML
    JFXComboBox grupe;
    @FXML
    JFXTextField numeRaport;


    public void setRapoarte(Rapoarte rapoarte){
        this.rapoarte=rapoarte;
        StudentService service=rapoarte.getStudentService();


        List<String> grupeList=service.getGrupe();
        grupeList.add("Toate grupele");

        ObservableList<String> data = FXCollections.observableArrayList(grupeList);
        grupe.setItems(data);
        grupe.setValue(data.get(data.size()-1));
    }
    @FXML
    public void handleGenerare() throws Exception {

        if(this.numeRaport==null)
            this.numeRaport.setText("raport");
        String denumire=this.numeRaport.getText()+".pdf";
        rapoarte.generateReport(new File(denumire),grupe.getValue().toString(),mediaStudenti.isSelected(),studentiNuExamen.isSelected(),
                mediaLaboratoare.isSelected(),studentiTemePredateLaTimp.isSelected());
        this.mediaLaboratoare.setSelected(false);
        this.mediaStudenti.setSelected(false);
        this.studentiNuExamen.setSelected(false);
        this.studentiTemePredateLaTimp.setSelected(false);
    }

    @Override
    public void update(DataChanged dataChanged) {
        StudentService service=rapoarte.getStudentService();
        List<String> grupeList=service.getGrupe();
        grupeList.add("Toate grupele");

        ObservableList<String> data = FXCollections.observableArrayList(grupeList);
        grupe.setItems(data);
        grupe.setValue(data.get(data.size()-1));
    }
}
