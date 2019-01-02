package GUI.login;

import GUI.admin.AdminMainController;
import GUI.profesor.ProfesorMainController;
import GUI.student.StudentMainController;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import domain.Profesor;
import domain.Student;
import domain.Utilizator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;


public class Controller {

    @FXML
    JFXTextField fieldUser;
    @FXML
    JFXPasswordField fieldPassword;
    @FXML
    Label errorLabel;

    UtilizatorService serviceUser;
    StudentService serviceStudent;
    TemaService serviceTema;
    ProfesorService serviceProfesor;
    NoteService serviceNote;
    Rapoarte rapoarte;

    private Stage primaryStage;
    private Scene studentScene;
    private Scene adminScene;
    private Scene profesorScene;
    private Scene loginScene;
    private StudentMainController studentMainController;
    private ProfesorMainController profesorMainController;
    private AdminMainController adminMainController;

    private void initViewAdmin() throws IOException {
        FXMLLoader loaderAdmin = new FXMLLoader();
        loaderAdmin.setLocation(getClass().getResource("/GUI/admin/AdminMainView.fxml"));
        AnchorPane adminLayout = loaderAdmin.load();
        Scene sceneAdmin = new Scene(adminLayout,800,500);

        this.adminMainController=loaderAdmin.getController();
        adminMainController.setService(serviceStudent,serviceProfesor,serviceUser);
        adminMainController.setLoginScene(loginScene);
        adminMainController.setPrimaryStage(primaryStage);

        this.setAdminScene(sceneAdmin);
    }

    private void initViewProfesor(Profesor profesor) throws IOException {
        FXMLLoader loaderProfesor = new FXMLLoader();
        loaderProfesor.setLocation(getClass().getResource("/GUI/profesor/ProfesorMainView.fxml"));
        AnchorPane profesorLayout = loaderProfesor.load();
        Scene sceneProfesor = new Scene(profesorLayout,800,500);

        this.profesorMainController=loaderProfesor.getController();
        profesorMainController.setProfesor(profesor);
        profesorMainController.setService(serviceStudent,serviceTema,serviceNote,rapoarte,serviceUser);
        profesorMainController.setLoginScene(loginScene);
        profesorMainController.setPrimaryStage(primaryStage);
        this.setProfesorScene(sceneProfesor);
    }

    private void initViewStudent(Student student) throws IOException {
        FXMLLoader loaderStudent = new FXMLLoader();
        loaderStudent.setLocation(getClass().getResource("/GUI/student/StudentMainView.fxml"));
        AnchorPane studentLayout = loaderStudent.load();
        Scene sceneStudent = new Scene(studentLayout,800,500);

        this.studentMainController=loaderStudent.getController();
        studentMainController.setStudent(student);
        studentMainController.setService(serviceTema,serviceNote);
        studentMainController.setLoginScene(loginScene);
        studentMainController.setPrimaryStage(primaryStage);

        this.setStudentScene(sceneStudent);
    }

    public void init(UtilizatorService serviceUser, StudentService serviceStudent,
                           TemaService serviceTema, ProfesorService serviceProfesor,
                           NoteService serviceNote,Rapoarte rapoarte,Scene loginScene){

        this.serviceUser=serviceUser;
        this.serviceStudent=serviceStudent;
        this.serviceTema=serviceTema;
        this.serviceProfesor=serviceProfesor;
        this.serviceNote=serviceNote;
        this.loginScene=loginScene;
        this.rapoarte=rapoarte;
    }

    public void setStudentScene(Scene studentScene) {
        this.studentScene = studentScene;
    }

    public void setProfesorScene(Scene profesorScene) {
        this.profesorScene = profesorScene;
    }

    public void setAdminScene(Scene adminScene) {
        this.adminScene = adminScene;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage=primaryStage;
    }

    @FXML
    public void handleLogin() throws IOException {
        String username = "";
        String password = "";

        errorLabel.setVisible(false);

        if (fieldUser.getText() != null)
            username = fieldUser.getText().trim();
        if (fieldPassword.getText() != null)
            password = fieldPassword.getText();

        boolean valid=serviceUser.verificareParola(username, password);
        if (valid) {
            Utilizator utilizator=serviceUser.find(username);
            if(utilizator.getTip().equals(Utilizator.TipUtilizator.ADMIN)){
                initViewAdmin();
                handleAdminWindow();
            }
            else if(utilizator.getTip().equals(Utilizator.TipUtilizator.STUDENT)) {
                Student std=serviceStudent.findByUsername(username);
                initViewStudent(std);
                handleStudentWindow();
            }
            else{
                Profesor prof=serviceProfesor.findByUsername(username);
                initViewProfesor(prof);
                handleProfesorWindow();
            }
        } else {
            errorLabel.setVisible(true);
        }
        fieldPassword.clear();
        fieldUser.clear();
        fieldUser.requestFocus();

    }

    private void handleStudentWindow()
    {
        this.primaryStage.setScene(this.studentScene);
    }

    private void handleProfesorWindow()
    {
        this.primaryStage.setScene(this.profesorScene);
    }

    private void handleAdminWindow()
    {
        this.primaryStage.setScene(this.adminScene);
    }

}
