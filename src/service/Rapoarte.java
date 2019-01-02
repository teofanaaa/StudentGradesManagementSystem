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
import javafx.util.Pair;
import utils.DataChanged;
import utils.Observer;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Config.filterAndSorter;
import static utils.Config.pathRaport;

public class Rapoarte {
    public StudentService getStudentService() {
        return studentService;
    }

    private StudentService studentService;
    private TemaService temaService;
    private NoteService notaService;

    String grupa = null;
    String baseUri = pathRaport;
    String beginHtml = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <title>Raport</title>\n" +
            "    <link href=\"reportStylesheet.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
            "</head>\n" +
            "<body>\n" +
            "    <h1>Raport</h1>\n" +
            "    <hr>";
    String endHtml = "</body>\n" +
            "</html>";
    String beginCard = "<table class = \"Card\">\n" +
            "        <tr>";
    String endCard = "</tr>\n" +
            "    </table>\n" +
            "    <br>";
    String beginTableData = "<td><table class = \"DataTable\">" +
            "<tr><th>Nume</th><th>Media</th></tr>";
    String endTableData = "</table></td>";

    public Rapoarte(StudentService studentService, TemaService temaService, NoteService notaService) {
        this.studentService = studentService;
        this.temaService = temaService;
        this.notaService = notaService;
    }

    private String generateDataColumn(String... text) {
        String dataCol = "<td class=\"DataColumn LeftColumn\">";

        for (String t : text) {
            dataCol =dataCol + "<p>" + t + "</p>";
        }

        dataCol = dataCol+ "</td>";
        return dataCol;
    }

    private String generateTableRow(String... text) {
        String dataCol = "<tr>";

        for (String t : text) {
            dataCol += "<td>" + t + "</td>";
        }

        dataCol += "</tr>";
        return dataCol;
    }

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
            html += beginCard;
            html += generateDataColumn("Media", "studentilor", "la", "laborator");

            html += beginTableData;
            for (Student student : allStudents) {
                html += generateTableRow(student.getNume()+ " "+ student.getPrenume(),
                        //String.valueOf(student.getGrupa()),
                        String.format("%.2f", notaService.getMediaStudent(student.getID())));
            }
            html += endTableData;

            html += endCard;
        }

        if (studentiNuExamen) {
            html += beginCard;
            html += generateDataColumn("Studentii", "care nu", "pot intra", "in examen");

            html += beginTableData;
            for (Student student : allStudents) {
                Double media = notaService.getMediaStudent(student.getID());
                if (media < 5)
                    html += generateTableRow(student.getNume(), //String.valueOf(student.getGrupa()),
                            String.format("%.2f", media));
            }
            html += endTableData;

            html += endCard;
        }

        if(mediaLaboratoare){
            html += beginCard;
            html += generateDataColumn("Media", "notelor", "la", "laborator");

            html += beginTableData;
            for (Tema tema : allTeme) {
                html += generateTableRow("Laborator "+tema.getID(),
                        //String.valueOf(tema.),
                        String.format("%.2f", notaService.getMediaLaborator(tema,allStudents)));
            }
            html += endTableData;

            html += endCard;
        }

        if(studentiTemePredateLaTimp){
            html += beginCard;
            html += generateDataColumn("Studentii", "care au", "predat", "toate","temele");

            int nr=0;
            html += beginTableData;

            for(Student student:allStudents)
                for (Tema tema : temaService.getAll()){
                    if(notaService.find(new Pair<>(student.getID(),tema.getID()))!=null) {
                        nr++;
                }
                if(nr==temaService.getAll().size())
                    html += generateTableRow(student.getNume() + " " + student.getPrenume(),
                        //    String.valueOf(student.getGrupa()),
                            String.format("%.2f", notaService.getMediaStudent(student.getID())));
            }
            html += endTableData;

            html += endCard;
        }

        html += endHtml;
        return html;
    }



    public void generateReport(File file, String grupa, boolean mediaStudenti, boolean studentiNuExamen,
                               boolean mediaLaboratoare, boolean studentiTemePredateLaTimp) throws Exception {
        if (! grupa.equals("Toate grupele"))
            this.grupa = grupa;
        else
            this.grupa = null;

        String html = constructReport(mediaStudenti, studentiNuExamen, mediaLaboratoare, studentiTemePredateLaTimp);

        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(baseUri);
        properties.setFontProvider(new DefaultFontProvider(true, true, true));
        HtmlConverter.convertToPdf(html, new FileOutputStream(file), properties);

        System.out.println("PDF Created!");
    }

}
