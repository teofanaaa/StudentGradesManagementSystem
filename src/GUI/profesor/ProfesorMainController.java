package GUI.profesor;

import com.jfoenix.controls.JFXButton;
import domain.Profesor;
import domain.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;

public class ProfesorMainController {
    @FXML
    JFXButton buttonLogout;
    @FXML
    AnchorPane detailsPane;
    @FXML
    Label Nume;

    private Stage primaryStage;
    private Scene loginScene;
    UtilizatorService serviceUser;
    NoteService serviceNote;
    StudentService serviceStudent;
    TemaService serviceTema;
    Rapoarte rapoarte;

    private Profesor profesor;

    AnchorPane notePane;
    AnchorPane temePane;
    AnchorPane studentiPane;
    AnchorPane statisticiPane;
    AnchorPane rapoartePane;

    public void setProfesor (Profesor profesor){
        this.profesor=profesor;
        Nume.setText(profesor.getNume());
    }

    public void setService(StudentService serviceStudent, TemaService serviceTema,NoteService serviceNote, Rapoarte rapoarte,
                           UtilizatorService serviceUser) throws IOException {
        this.serviceStudent=serviceStudent;
        this.serviceNote=serviceNote;
        this.serviceUser=serviceUser;
        this.rapoarte=rapoarte;
        this.serviceTema=serviceTema;

        FXMLLoader loaderTeme = new FXMLLoader();
        loaderTeme.setLocation(getClass().getResource("/GUI/profesor/ProfesorTemeController.fxml"));
        this.temePane = loaderTeme.load();
        ProfesorTemeController temeController=loaderTeme.getController();
        temeController.setService(serviceTema);

        FXMLLoader loaderStudenti = new FXMLLoader();
        loaderStudenti.setLocation(getClass().getResource("/GUI/profesor/ProfesorStudentiView.fxml"));
        this.studentiPane = loaderStudenti.load();
        ProfesorStudentiController studentiController=loaderStudenti.getController();
        studentiController.setService(serviceStudent,serviceNote,serviceTema,profesor);

        FXMLLoader loaderNote = new FXMLLoader();
        loaderNote.setLocation(getClass().getResource("/GUI/profesor/ProfesorNoteView.fxml"));
        this.notePane = loaderNote.load();
        ProfesorNoteController noteController=loaderNote.getController();
        noteController.setService(serviceNote,serviceStudent,serviceTema,profesor);

        FXMLLoader loaderRapoarte = new FXMLLoader();
        loaderRapoarte.setLocation(getClass().getResource("/GUI/profesor/ProfesorRapoarteView.fxml"));
        this.rapoartePane = loaderRapoarte.load();
        ProfesorRapoarteController rapoarteController=loaderRapoarte.getController();
        rapoarteController.setRapoarte(rapoarte);

        FXMLLoader loaderStatistici = new FXMLLoader();
        loaderStatistici.setLocation(getClass().getResource("/GUI/profesor/ProfesorStatisticiView.fxml"));
        this.statisticiPane = loaderStatistici.load();
        ProfesorStatisticiController statisticiController=loaderStatistici.getController();
        statisticiController.setService(serviceNote);


        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(this.temePane);

        this.serviceTema.addObserver(temeController);
        this.serviceStudent.addObserver(studentiController);

        this.serviceStudent.addObserver(rapoarteController);
        this.serviceNote.addObserver(rapoarteController);
        this.serviceTema.addObserver(rapoarteController);

//        this.serviceStudent.addObserver(statisticiController);
//        this.serviceNote.addObserver(statisticiController);
//        this.serviceTema.addObserver(statisticiController);
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage=primaryStage;
    }

    public void setLoginScene(Scene scene){
        this.loginScene=scene;
    }

    @FXML
    public void handleLogout(){
        this.primaryStage.setScene(loginScene);
    }

    @FXML
    public void handleClose(){
        this.primaryStage.close();
    }

    @FXML
    public void handleTemePane() {
        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(temePane);
    }

    @FXML
    public void handleStudentPane() {
        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(studentiPane);
    }

    @FXML
    public void handleNotePane() {
        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(notePane);
    }

    @FXML
    public void handleStatisticiPane() {
        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(statisticiPane);
    }

    @FXML
    public void handleRapoartePane() {
        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(rapoartePane);
    }
}
