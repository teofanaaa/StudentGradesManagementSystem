package utils;

import com.itextpdf.kernel.colors.Lab;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import domain.Profesor;
import domain.Student;
import domain.Tema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import validator.ValidationException;

import java.io.File;
import java.util.List;

public class GUIUtils {

    //Field profesor
    public static JFXTextField textFieldIdProf = new JFXTextField();
    public static JFXTextField textFieldNumeProf = new JFXTextField();
    public static JFXTextField textFieldEmailProf = new JFXTextField();

    //Field student
    public static JFXTextField textFieldIdStudent = new JFXTextField();
    public static JFXTextField textFieldNumeStudent = new JFXTextField();
    public static JFXTextField textFieldPrenumeStudent = new JFXTextField();
    public static JFXTextField textFieldGrupaStudent = new JFXTextField();
    public static JFXTextField textFieldEmailStudent = new JFXTextField();
    public static JFXComboBox<String> comboBoxProfStudent = new JFXComboBox<>();

    //Field tema
    public static JFXTextField textFieldIdTema =new JFXTextField();
    public static JFXTextField textFieldDescriereTema=new JFXTextField();
    public static JFXComboBox<String> comboBoxDeadlineTema= new JFXComboBox<>();
    public static JFXComboBox<String> comboBoxPredareTema= new JFXComboBox<>();

    //Field nota
    public static JFXComboBox<String> comboBoxIdStudentNota=new JFXComboBox<>();
    public static JFXComboBox<String> comboBoxIdTemaNota= new JFXComboBox<>();
    public static JFXTextField textFieldNota=new JFXTextField();
    public static JFXTextField textAreaFeedbackNota=new JFXTextField();
    public static JFXCheckBox checkBoxMotivatNota=new JFXCheckBox();

    /**
     * Fereastra de eroare
     * @param text - string (textul erorii)
     */
    public static void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Eroare");
        message.setContentText(text);
        message.getDialogPane().setStyle("" +
                "-fx-font-family: \"Trebuchet MS\", Helvetica, sans-serif;\n" +
                "-fx-text-fill: white;\n" +
                "-fx-background-color: rgba(172,20,39,0.5);"+
                "");
        message.showAndWait();

    }

    /**
     * Fereastra de adaugare profesor
     * @return gridPane (fereastra)
     */
    public static Node addProfesorContent(){
        Label text1 = new Label("Nr. leg.");
        Label text2 = new Label("Nume");
        Label text3=new Label("Email");

        textFieldIdProf.setText("");
        textFieldNumeProf.setText("");
        textFieldEmailProf.setText("");

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(300, 200);
        gridPane.setPadding(new Insets(10, 5, 10, 5));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(text1, 0, 0);
        gridPane.add(textFieldIdProf, 1, 0);
        gridPane.add(text2, 0, 1);
        gridPane.add(textFieldNumeProf, 1, 1);
        gridPane.add(text3, 0, 2);
        gridPane.add(textFieldEmailProf, 1, 2);
        return gridPane;
    }

    /**
     * Fereastra de modificare date profesor
     * @return gridPane (fereastra)
     */
    public static Node editProfesorContent(Profesor profesor){
        Label text1 = new Label("Nr. leg.");
        Label text2 = new Label("Nume");
        Label text3=new Label("Email");

        textFieldIdProf.setDisable(true);//il dezactivez
        textFieldIdProf.setText(profesor.getID());
        textFieldNumeProf.setText(profesor.getNume());
        textFieldEmailProf.setText(profesor.getEmail());

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(300, 200);
        gridPane.setPadding(new Insets(10, 5, 10, 5));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(text1, 0, 0);
        gridPane.add(textFieldIdProf, 1, 0);
        gridPane.add(text2, 0, 1);
        gridPane.add(textFieldNumeProf, 1, 1);
        gridPane.add(text3, 0, 2);
        gridPane.add(textFieldEmailProf, 1, 2);
        return gridPane;
    }

    /**
     * Fereastra de adaugare student
     * @return gridPane (fereastra)
     */
    public static Node editStudentContent(Student student){
        Label text1 = new Label("Nr. matr.");
        Label text2 = new Label("Nume");
        Label text3=new Label("Prenume");
        Label text4=new Label("Grupa");
        Label text5=new Label("Email");
        Label text6=new Label("Profesor");

        textFieldIdStudent.setDisable(true);//il dezactivez
        textFieldIdStudent.setText(student.getID());
        textFieldNumeStudent.setText(student.getNume());
        textFieldPrenumeStudent.setText(student.getPrenume());
        textFieldGrupaStudent.setText(student.getGrupa());
        textFieldEmailStudent.setText(student.getEmail());

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(300, 200);
        gridPane.setPadding(new Insets(10, 5, 10, 5));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(text1, 0, 0);
        gridPane.add(textFieldIdStudent, 1, 0);
        gridPane.add(text2, 0, 1);
        gridPane.add(textFieldNumeStudent, 1, 1);
        gridPane.add(text3, 0, 2);
        gridPane.add(textFieldPrenumeStudent, 1, 2);
        gridPane.add(text4, 0, 3);
        gridPane.add(textFieldGrupaStudent, 1, 3);
        gridPane.add(text5, 0, 4);
        gridPane.add(textFieldEmailStudent, 1, 4);
        gridPane.add(text6, 0, 5);
        gridPane.add(comboBoxProfStudent, 1, 5);
        return gridPane;
    }

    /**
     * Fereastra de editare date tema
     * @return gridPane (fereastra)
     */
    public static Node editTemaContent(Tema tema){
        Label text1 = new Label("Nr. lab.");
        Label text2 = new Label("Descriere");
        Label text3=new Label("Deadline");
        Label text4=new Label("Predare");

        textFieldIdTema.setDisable(true);//il dezactivez
        textFieldIdTema.setText(tema.getID());
        textFieldDescriereTema.setText(tema.getDescriere());
        //textFieldDescriereTema.setDisable(true);
        comboBoxPredareTema.setDisable(true);
        GridPane gridPane = new GridPane();

        gridPane.setMinSize(300, 200);
        gridPane.setPadding(new Insets(10, 5, 10, 5));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(text1, 0, 0);
        gridPane.add(textFieldIdTema, 1, 0);
        gridPane.add(text2, 0, 1);
        gridPane.add(textFieldDescriereTema, 1, 1);
        gridPane.add(text3, 0, 2);
        gridPane.add(comboBoxDeadlineTema, 1, 2);
        gridPane.add(text4, 0, 3);
        gridPane.add(comboBoxPredareTema, 1, 3);
        return gridPane;
    }

    /**
     * Fereastra de adaugare tema
     * @return gridPane (fereastra)
     */
    public static Node addTemeContent(){
        Label text1 = new Label("Nr. lab.");
        Label text2 = new Label("Descriere");
        Label text3=new Label("Deadline");
        Label text4=new Label("Predare");

        textFieldIdTema.setDisable(false);
        textFieldIdTema.setText("");
        textFieldDescriereTema.setText("");
        comboBoxPredareTema.setDisable(false);
        GridPane gridPane = new GridPane();

        gridPane.setMinSize(300, 200);
        gridPane.setPadding(new Insets(10, 5, 10, 5));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(text1, 0, 0);
        gridPane.add(textFieldIdTema, 1, 0);
        gridPane.add(text2, 0, 1);
        gridPane.add(textFieldDescriereTema, 1, 1);
        gridPane.add(text3, 0, 2);
        gridPane.add(comboBoxDeadlineTema, 1, 2);
        gridPane.add(text4, 0, 3);
        gridPane.add(comboBoxPredareTema, 1, 3);
        return gridPane;
    }

    /**
     * Fereastra de adaugare nota
     * @return gridPane (fereastra)
     */
    public static Node addNotaContent(){
        Label text1 = new Label("Nr. matr.");
        Label text2 = new Label("Nr. tema");
        Label text3=new Label("Nota");
        Label text4=new Label("Feedback");
        Label text5=new Label("Motivat");

        new AutoCompleteComboBoxListener<>(comboBoxIdStudentNota);
        new AutoCompleteComboBoxListener<>(comboBoxIdTemaNota);
        checkBoxMotivatNota.setSelected(false);
        textFieldNota.setText("");
        textAreaFeedbackNota.setText("");
        GridPane gridPane = new GridPane();

        gridPane.setMinSize(300, 200);
        gridPane.setPadding(new Insets(10, 5, 10, 5));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(text1, 0, 0);
        gridPane.add(comboBoxIdStudentNota, 1, 0);
        gridPane.add(text2, 0, 1);
        gridPane.add(comboBoxIdTemaNota, 1, 1);
        gridPane.add(text3, 0, 2);
        gridPane.add(textFieldNota, 1, 2);
        gridPane.add(text4, 0, 3);
        gridPane.add(textAreaFeedbackNota, 1, 3);
        gridPane.add(text5, 0, 4);
        gridPane.add(checkBoxMotivatNota, 1, 4);
        return gridPane;
    }

    /**
     * Fereastra de adaugare student
     * @return gridPane (fereastra)
     */
    public static Node addStudentContent(){
        Label text1 = new Label("Nr. matr.");
        Label text2 = new Label("Nume");
        Label text3=new Label("Prenume");
        Label text4=new Label("Grupa");
        Label text5=new Label("Email");
        Label text6=new Label("Profesor");

        textFieldIdStudent.setDisable(false);
        textFieldIdStudent.setText("");
        textFieldNumeStudent.setText("");
        textFieldPrenumeStudent.setText("");
        textFieldGrupaStudent.setText("");
        textFieldEmailStudent.setText("");

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(300, 200);
        gridPane.setPadding(new Insets(10, 5, 10, 5));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(text1, 0, 0);
        gridPane.add(textFieldIdStudent, 1, 0);
        gridPane.add(text2, 0, 1);
        gridPane.add(textFieldNumeStudent, 1, 1);
        gridPane.add(text3, 0, 2);
        gridPane.add(textFieldPrenumeStudent, 1, 2);
        gridPane.add(text4, 0, 3);
        gridPane.add(textFieldGrupaStudent, 1, 3);
        gridPane.add(text5, 0, 4);
        gridPane.add(textFieldEmailStudent, 1, 4);
        gridPane.add(text6, 0, 5);
        gridPane.add(comboBoxProfStudent, 1, 5);
        return gridPane;
    }

    /**
     * Configurare fereastra de dialog
     * @param mainPane - AnchorPane (fereastra radacina)
     * @param header - string (titlul ferestrei de dialog)
     * @param eventAction - eventhandler (actiune care se executa a apasarea butonului "Confirmare")
     * @param body - Node (componenetele ferestrei de dialog)
     */
    public static void setDialouge(AnchorPane mainPane, String header, EventHandler eventAction, Node body) {
        JFXDialogLayout content = new JFXDialogLayout();
        Label head=new Label(header);
        head.setStyle("-fx-font-weight: bold; -fx-font-size: 16px");
        content.setHeading(head);
        content.setBody(body);
        StackPane stackPane = new StackPane();
        stackPane.autosize();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, true);
        JFXButton okayBtn = new JFXButton("Confirma");
        okayBtn.addEventHandler(ActionEvent.ACTION, eventAction);
        okayBtn.addEventHandler(ActionEvent.ACTION, (e)-> {
            dialog.close();
        });

        okayBtn.setButtonType(JFXButton.ButtonType.FLAT);
        okayBtn.setPrefHeight(32);
        JFXButton cancelBtn = new JFXButton("Renunta");
        cancelBtn.addEventHandler(ActionEvent.ACTION, (e)-> {
            dialog.close();
        });
        cancelBtn.setButtonType(JFXButton.ButtonType.FLAT);
        cancelBtn.setPrefHeight(32);
        content.setActions( okayBtn,cancelBtn);
        content.setPrefSize(250d, 250d);
        mainPane.getChildren().add(stackPane);
        AnchorPane.setTopAnchor(stackPane, (mainPane.getHeight()-content.getPrefHeight())/2);
        AnchorPane.setLeftAnchor(stackPane, (mainPane.getWidth()-content.getPrefWidth())/2);
        dialog.show();
    }
}
