package GUI.admin;

import GUI.student.StudentNoteController;
import GUI.student.StudentTemeController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;

public class AdminMainController {
    @FXML
    JFXButton buttonLogout;
    @FXML
    AnchorPane detailsPane;

    private Stage primaryStage;
    private Scene loginScene;
    StudentService serviceStudent;
    ProfesorService serviceProfesor;
    UtilizatorService serviceUser;

    AnchorPane studentPane;
    AnchorPane profesorPane;
    AnchorPane userPane;


    public void setService(StudentService serviceStudent, ProfesorService serviceProfesor,
                           UtilizatorService serviceUser) throws IOException {
        this.serviceProfesor=serviceProfesor;
        this.serviceStudent=serviceStudent;
        this.serviceUser=serviceUser;

        FXMLLoader loaderUser = new FXMLLoader();
        loaderUser.setLocation(getClass().getResource("/GUI/admin/AdminUsersView.fxml"));
        this.userPane = loaderUser.load();
        AdminUsersController userController=loaderUser.getController();
        userController.setService(serviceUser);

        FXMLLoader loaderProfesor = new FXMLLoader();
        loaderProfesor.setLocation(getClass().getResource("/GUI/admin/AdminProfesoriView.fxml"));
        this.profesorPane = loaderProfesor.load();
        AdminProfesoriController profesorController=loaderProfesor.getController();
        profesorController.setService(serviceProfesor);

        FXMLLoader loaderStudent = new FXMLLoader();
        loaderStudent.setLocation(getClass().getResource("/GUI/admin/AdminStudentiView.fxml"));
        this.studentPane = loaderStudent.load();
        AdminStudentiController studentController=loaderStudent.getController();
        studentController.setService(serviceStudent,serviceProfesor);

        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(this.studentPane);

        this.serviceProfesor.addObserver(profesorController);
        this.serviceProfesor.addObserver(userController);
        this.serviceProfesor.addObserver(studentController);

        this.serviceStudent.addObserver(userController);
        this.serviceStudent.addObserver(studentController);
        this.serviceStudent.addObserver(profesorController);
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
    public void handleUserPane() {
        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(userPane);
    }

    @FXML
    public void handleStudentPane() {
        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(studentPane);
    }

    @FXML
    public void handleProfesorPane() {
        this.detailsPane.getChildren().clear();
        this.detailsPane.getChildren().add(profesorPane);
    }
}
