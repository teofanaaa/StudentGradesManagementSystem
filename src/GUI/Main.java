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

import java.io.File;

import static java.lang.System.exit;
import static java.lang.System.setOut;

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

        Rapoarte rapoarte=new Rapoarte(studentService,temaService,noteService);
        try {
            rapoarte.generateReport(new File("./src/data/raport/complet.pdf"),
                    "Toate grupele",false,false,false,false);
        } catch (Exception e) {
            e.printStackTrace();

        }


        //launch(args);
        exit(0);
    }
}
