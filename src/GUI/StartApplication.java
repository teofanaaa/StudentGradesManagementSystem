package GUI;

import GUI.login.Controller;
import domain.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import repository.Repository;
import repository.database.*;
import repository.file.*;
import service.*;
import utils.PasswordStorage;
import validator.*;

import java.io.File;
import java.io.IOException;


public class StartApplication extends Application {

//    Repository<String, Utilizator> repoU =new UtilizatorRepositoryInFile("./src/data/files/Utilizatori.txt",new ValidatorUtilizator());
//    Repository<String, Student> repoS =new StudentRepositoryInFile("./src/data/files/Studenti.txt",new ValidatorStudent());
//    Repository<String, Tema> repoT =new TemaRepositoryInFile("./src/data/files/Teme.txt",new ValidatorTema());
//    Repository<String, Profesor> repoP =new ProfesorRepositoryInFile("./src/data/files/Profesori.txt",new ValidatorProfesor());
//    Repository<Pair<String,String>, Nota> repoN =new NotaRepositoryInFile("./src/data/files/Catalog.txt",new ValidatorNota());

    DatabaseCreator databaseCreator=new DatabaseCreator("./src/data/Catalog_db");
    Repository<String, Utilizator> repoU = new UtilizatorRepositoryDatabase(databaseCreator);
    Repository<String, Student> repoS =new StudentRepositoryDatabase(databaseCreator);
    Repository<String, Tema> repoT =new TemaRepositoryDatabase(databaseCreator);
    Repository<String, Profesor> repoP =new ProfesorRepositoryDatabase(databaseCreator);
    Repository<Pair<String,String>, Nota> repoN =new NotaRepositoryDatabase(databaseCreator);

    UtilizatorService utilizatorService=new UtilizatorService(repoU);
    NoteService noteService=new NoteService(repoN,repoS,repoT);
    ProfesorService profesorService=new ProfesorService(repoP,repoS,repoN,utilizatorService);
    StudentService studentService=new StudentService(repoS,repoT,repoN,utilizatorService);
    TemaService temaService=new TemaService(repoT,repoS,repoN);

    Rapoarte rapoarte=new Rapoarte(studentService,temaService,noteService);

    private void utilizatori(){
        System.out.println("Utilizatori: ");
        System.out.println("ADMINISTRATOR");
        System.out.println("user: admin - parola: admin");
        System.out.println("PROFESORI");
        for(Profesor p:profesorService.getAll()){
            System.out.println("user: "+p.getEmail().split("@")[0]+" - parola: "+p.getNume());
        }
        System.out.println("STUDENTI");
        for(Student s:studentService.getAll()){
            System.out.println("user: "+s.getEmail().split("@")[0]+" - parola: " + s.getNume());
        }
    }

    private void init(Stage primaryStage) throws IOException, PasswordStorage.CannotPerformOperationException {
       // utilizatorService.createUser();
//        profesorService.add(new Profesor("1","Serban Camelia","camelia_ubb@yahoo.com"));
//        studentService.add(new Student("1001","Enachioiu","Teofana","223",
//                "teofanaenachioiu@yahoo.com","1"));
//        temaService.add(new Tema("1","Introducere in Java","2","1"));
//        noteService.add("1001","1","10","Foarte bine!",true);
 //       noteService.remove(new Pair<>("1021","1"));
//        studentService.remove("1021");

        utilizatori();
        System.out.println("Total:" +utilizatorService.getAll().size());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/login/sample.fxml"));
        AnchorPane rootLayout = loader.load();
        Scene mainMenuScene = new Scene(rootLayout,800,500);

        Controller controllerLogin=loader.getController();
        controllerLogin.init(utilizatorService,studentService,temaService,profesorService,noteService,rapoarte,mainMenuScene);

        controllerLogin.setPrimaryStage(primaryStage);

        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        primaryStage.setTitle("Catalog Metode Avansate de Programare");
        primaryStage.getIcons().add(new Image(new File("teacher.png").toURI().toString()));
        init(primaryStage);

    }


    public static void main(String[] args) {

        launch(args);
    }
}
