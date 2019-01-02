package GUI.student;

import com.jfoenix.controls.JFXButton;
import domain.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.NoteService;
import service.TemaService;

import java.io.IOException;

public class StudentMainController {
    @FXML
    JFXButton buttonLogout;
    @FXML
    JFXButton buttonTeme;
    @FXML
    JFXButton buttonNote;
    @FXML
    AnchorPane detailsPane;
    @FXML
    Label Nume;

    AnchorPane temePane;
    AnchorPane notePane;

    private Stage primaryStage;
    private Scene loginScene;
    private TemaService temaService;
    private NoteService noteService;
    private Student student;

    public  void setStudent(Student student){
        this.student=student;
        Nume.setText(student.getPrenume());
    }
    public void setService(TemaService serviceTema, NoteService serviceNote) throws IOException {
        this.temaService=serviceTema;
        this.noteService=serviceNote;

        FXMLLoader loaderTeme = new FXMLLoader();
        loaderTeme.setLocation(getClass().getResource("/GUI/student/StudentTemeView.fxml"));
        this.temePane = loaderTeme.load();
        StudentTemeController temeController=loaderTeme.getController();
        temeController.setService(temaService);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/student/StudentNoteView.fxml"));
        this.notePane = loader.load();
        StudentNoteController studentNoteController=loader.getController();
        studentNoteController.setService(noteService,student);

        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(notePane);
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage=primaryStage;
    }

    public void setLoginScene(Scene scene){
        this.loginScene=scene;
    }

    @FXML
    public void handleTemePane()  {
        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(temePane);
    }

    @FXML
    public void handleNotePane() {
        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(notePane);
    }

    @FXML
    public void handleLogout(){
        this.primaryStage.setScene(loginScene);
    }

    @FXML
    public void handleClose(){
        this.primaryStage.close();
    }
}
