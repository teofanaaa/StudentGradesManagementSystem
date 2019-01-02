package GUI.profesor;

import domain.Tema;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import service.NoteService;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProfesorStatisticiController  {
    private NoteService service;
//    @FXML
//    JFXRadioButton mediaStudenti;
//    @FXML
//    JFXRadioButton mediaLaboratoare;
    @FXML
    BarChart<String, Number> chartStudent;
    @FXML
    BarChart<String, Number> chartTema;
    public void setService(NoteService service){
        this.service=service;
        setChartStudent();
        setChartTema();
    }

    private void setChartStudent(){
        XYChart.Series series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data("sub 5", service.nrStudentiMedie(0,5)));
        series1.getData().add(new XYChart.Data("5-6", service.nrStudentiMedie(5,6)));
        series1.getData().add(new XYChart.Data("6-7", service.nrStudentiMedie(6,7)));
        series1.getData().add(new XYChart.Data("7-8", service.nrStudentiMedie(7,8)));
        series1.getData().add(new XYChart.Data("8-9", service.nrStudentiMedie(8,9)));
        series1.getData().add(new XYChart.Data("9-10", service.nrStudentiMedie(9,10)));
        series1.getData().add(new XYChart.Data("10", service.nrStudentiMedie(10,11)));

        chartStudent.getData().addAll(series1);
    }

    private void setChartTema(){
        XYChart.Series series1 = new XYChart.Series();


        for(Tema tema:service.getRepoT().findAll()){
            series1.getData().add(new XYChart.Data("L"+tema.getID(), service.getMediaLaborator(tema,
                    StreamSupport.stream(service.getRepoS().findAll().spliterator(),false).collect(Collectors.toList())
                    )));
        }
        chartTema.getData().addAll(series1);
    }

}
