package GUI.profesor;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import domain.Student;
import domain.Tema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import service.Rapoarte;
import service.StudentService;
import utils.DataChanged;
import utils.Observable;
import utils.Observer;

import java.io.File;
import java.io.FileOutputStream;
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

        FileOutputStream file = null;
        Stage stage = new Stage ();
        if(numeRaport.equals("")){
            return;
        }
        try{
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Open Resource File");
            File selectedDirectory = directoryChooser.showDialog(stage);
            if(selectedDirectory == null) //daca nu e selectat un director
                return;

            String path = selectedDirectory.toPath().toString().replace( '\\','/');

            File filePath = new File(path+"/"+denumire);
            if(filePath.exists()){
                filePath.delete();
            }
            file = new FileOutputStream(filePath);

            rapoarte.generateReport(path,new File(denumire),grupe.getValue().toString(),mediaStudenti.isSelected(),studentiNuExamen.isSelected(),
                    mediaLaboratoare.isSelected(),studentiTemePredateLaTimp.isSelected());

            file.close();
        }catch (Exception e) {
            e.printStackTrace();
        }


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
