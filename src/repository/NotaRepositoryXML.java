package repository;

import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import sun.reflect.generics.repository.AbstractRepository;
import validator.Validator;

public class NotaRepositoryXML extends AbstractRepositoryXML<Pair<Student, Tema>, Nota> {
    /**
     * Constructorul clasei
     *
     * @param fileName
     * @param validator - entitate Validator (validarea datelor)
     */
    public NotaRepositoryXML(String fileName, Validator<Nota> validator) {
        super(fileName, validator);
    }

    @Override
    public Element createElementfromEntity(Document document, Nota entity) {
        Element e = document.createElement("nota");
        e.setAttribute("idStudent", entity.getID().getKey().getID());
        e.setAttribute("idTema", entity.getID().getValue().getID());

        Element data = document.createElement("dataCurenta");
        data.setTextContent(entity.getDataCurenta());
        e.appendChild(data);

        Element notaProf = document.createElement("notaProf");
        notaProf.setTextContent(entity.getNotaProf().toString());
        e.appendChild(notaProf);

        return e;
    }

    @Override
    public Nota createEntityFromElement(Element element) {
        String studentId = element.getAttribute("idStudent");
        String temaId = element.getAttribute("idTema");
        NodeList nods = element.getChildNodes();
        String data =element.getElementsByTagName("dataCurenta")
                .item(0)
                .getTextContent();

        String notaProf =element.getElementsByTagName("notaProf")
                .item(0)
                .getTextContent();

        Student s=new Student(studentId,"fake","fake","222","fake@yahoo.com","fake");
        Tema t=new Tema(temaId,"fara descriere","1","1");

        return new Nota(s,t,data,notaProf);
    }
}
