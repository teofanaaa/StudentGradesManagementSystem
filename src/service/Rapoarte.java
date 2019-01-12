package service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.element.Image;
import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.util.Pair;
import utils.DataChanged;
import utils.Observer;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Config.filterAndSorter;
import static utils.Config.pathRaport;

/***
 * Class Rapoarte
 * Generator de rapoarte
 */
public class Rapoarte {
    public StudentService getStudentService() {
        return studentService;
    }

    private StudentService studentService;
    private TemaService temaService;
    private NoteService notaService;

    String grupa = null;
    String beginHtml = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <title>Raport</title>\n" +
            "<link href=\"D:/StudentGradesManagementSystem/src/data/raport/mytablestyle.css\" rel=\"stylesheet\" type=\"text/css\" >\n"+
            "</head>\n" +
            "<body>\n" +
            "    <h1>Raport</h1>\n" +
            "    <hr>"+
            "    <br>";
    String endHtml = "</body>\n" +
            "</html>";

    /**
     * Constructorul clasei
     * @param studentService - Service de student
     * @param temaService - Service de teme
     * @param notaService - Service de note
     */
    public Rapoarte(StudentService studentService, TemaService temaService, NoteService notaService) {
        this.studentService = studentService;
        this.temaService = temaService;
        this.notaService = notaService;
    }

    /***
     * Initializeaza tabelele
     * @param numeRaport - titlul raportului
     * @param numeColoane - coloanele tabelului
     * @return string (cod html)
     */
    private String initTable(String numeRaport, String... numeColoane){
        String html="";
        html += "<table class=\"zui-table\">\n";
        html += " <tr id=\"titlu\">\n";
        html += "     <th COLSPAN=\"3\">\n";
        html += "         <h3> <br> "+ numeRaport +"</h3>\n";
        html += "     </th>\n";
        html += " </tr>\n";
        for(String coloana:numeColoane){
            html+="<th>"+coloana+"</th>";
        }
        return html;
    }

    /***
     * Adauga randuri in tabel
     * @param date datele tabelului
     * @return string (cod html)
     */
    private String addContentToTable(String... date){
        String dataCol = "<tr>";

        for (String t : date) {
            dataCol += "<td>" + t + "</td>";
        }

        dataCol += "</tr>";
        return dataCol;
    }

    /***
     * Sfarsitul tabelului
     * @return string (cod html)
     */
    private String endTable(){
        return " </table><br><br>";
    }

    /**
     * Constructorul raportului
     * @param mediaStudenti - boolean ce indica daca se va construi sau nu acest raport
     * @param studentiNuExamen - boolean ce indica daca se va construi sau nu acest raport
     * @param mediaLaboratoare - boolean ce indica daca se va construi sau nu acest raport
     * @param studentiTemePredateLaTimp - boolean ce indica daca se va construi sau nu acest raport
     * @return string (cod html)
     */
    private String constructReport(boolean mediaStudenti, boolean studentiNuExamen,
                                   boolean mediaLaboratoare, boolean studentiTemePredateLaTimp) {
        String html = beginHtml;

        List<Student> allStudents;
        if (grupa == null)
            allStudents = studentService.getAll();
        else
            allStudents = studentService.filtreazaStudentiGrupa(grupa);

        List<Tema> allTeme = temaService.getAll();

        if (mediaStudenti) {
            html += initTable("Medie studenti","Nume","Grupa","Media");
            for (Student student : allStudents) {
                html += addContentToTable(student.getNume()+ " "+ student.getPrenume(),
                         String.valueOf(student.getGrupa()),
                        String.format("%.2f", notaService.getMediaStudent(student.getID())));
            }
            html+=endTable();
        }

        if (studentiNuExamen) {
            html += initTable("Studentii care nu intra in examen","Nume","Grupa","Media");
            for (Student student : allStudents) {
                Double media = notaService.getMediaStudent(student.getID());
                if (media < 5)
                    html += addContentToTable(student.getNume(),
                            String.valueOf(student.getGrupa()),
                            String.format("%.2f", media));
            }
            html+=endTable();
        }

        if(mediaLaboratoare){
            html += initTable("Media laboratoarelor","Laborator","Media");
            for (Tema tema : allTeme) {
                html += addContentToTable("Laborator "+tema.getID(),
                        String.format("%.2f", notaService.getMediaLaborator(tema,allStudents)));
            }
            html+=endTable();
        }

        if(studentiTemePredateLaTimp){
            html += initTable("Studentii care au predat toate temele","Nume","Grupa","Media");

            int nr=0;
            for(Student student:allStudents){
                for (Tema tema : temaService.getAll()) {
                    if (notaService.find(new Pair<>(student.getID(), tema.getID())) != null) {
                        nr++;
                    }
                }
                if (nr == temaService.getAll().size())
                    html += addContentToTable(student.getNume() + " " + student.getPrenume(),
                            String.valueOf(student.getGrupa()),
                            String.format("%.2f", notaService.getMediaStudent(student.getID())));
                nr = 0;

            }
            html+=endTable();
        }

        html += endHtml;
        return html;
    }

    /**
     * Generare raport pdf
     * @param adresa - calea pana la fila
     * @param file - fila
     * @param grupa - grupa pe care se face raportul
     * @param mediaStudenti - boolean ce indica daca se va construi sau nu acest raport
     * @param studentiNuExamen - boolean ce indica daca se va construi sau nu acest raport
     * @param mediaLaboratoare - boolean ce indica daca se va construi sau nu acest raport
     * @param studentiTemePredateLaTimp - boolean ce indica daca se va construi sau nu acest raport
     * @throws Exception
     */
    public void generateReport(String adresa, File file, String grupa, boolean mediaStudenti, boolean studentiNuExamen,
                               boolean mediaLaboratoare, boolean studentiTemePredateLaTimp) throws Exception {
        if (! grupa.equals("Toate grupele"))
            this.grupa = grupa;
        else
            this.grupa = null;

        String html = constructReport(mediaStudenti, studentiNuExamen, mediaLaboratoare, studentiTemePredateLaTimp);

        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(adresa);
        properties.setFontProvider(new DefaultFontProvider(true, true, true));
        HtmlConverter.convertToPdf(html, new FileOutputStream(file), properties);

        System.out.println("PDF Created!");
    }

}
