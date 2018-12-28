package GUI;

import domain.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import repository.Repository;
import repository.file.*;
import service.*;
import utils.PasswordStorage;
import validator.*;

import static java.lang.System.exit;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GUI/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {

        System.out.println("Hello");
        Repository<String, Utilizator> repoU =new UtilizatorRepositoryInFile("./src/data/files/Utilizatori.txt",new ValidatorUtilizator());
        Repository<String, Student> repoS =new StudentRepositoryInFile("./src/data/files/Studenti.txt",new ValidatorStudent());
        Repository<String, Tema> repoT =new TemaRepositoryInFile("./src/data/files/Teme.txt",new ValidatorTema());
        Repository<String, Profesor> repoP =new ProfesorRepositoryInFile("./src/data/files/Profesori.txt",new ValidatorProfesor());
        Repository<Pair<String,String>, Nota> repoN =new NotaRepositoryInFile("./src/data/files/Catalog.txt",new ValidatorNota());

        UtilizatorService utilizatorService=new UtilizatorService(repoU);
        NoteService noteService=new NoteService(repoN,repoS,repoT);
        ProfesorService profesorService=new ProfesorService(repoP,repoS,utilizatorService);
        StudentService studentService=new StudentService(repoS,repoT,repoN,utilizatorService);
        TemaService temaService=new TemaService(repoT,repoS);

//        noteService.add("1001","1","10","Foarte bine lucrat!",true);
//        noteService.add("1002","1","10","Mai bine nu",false);
//        noteService.add("1012","1","10","Mai bine nu",false);
//        noteService.update("1001","1","9","Remediere nota",true);

//        try {
//            utilizatorService.createUser();
//        } catch (PasswordStorage.CannotPerformOperationException e) {
//            e.printStackTrace();
//        }
//
//        for(Student student:repoS.findAll()){
//            try {
//                utilizatorService.createUser(student);
//            } catch (PasswordStorage.CannotPerformOperationException e) {
//                e.printStackTrace();
//            }
//        }
//        for(Profesor profesor:repoP.findAll()){
//            try {
//                utilizatorService.createUser(profesor);
//            } catch (PasswordStorage.CannotPerformOperationException e) {
//                e.printStackTrace();
//            }
//        }


        //launch(args);
        exit(0);
    }
}
