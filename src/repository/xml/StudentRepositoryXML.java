package repository.xml;

import domain.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import repository.xml.AbstractRepositoryXML;
import validator.Validator;

public class StudentRepositoryXML extends AbstractRepositoryXML<String, Student> {

    public StudentRepositoryXML(String fileName, Validator<Student> validator) {
        super(fileName, validator);
    }

    @Override
    public Element createElementfromEntity(Document document, Student entity) {
        Element e = document.createElement("student");
        e.setAttribute("idStudent", entity.getID());

        Element nume = document.createElement("nume");
        nume.setTextContent(entity.getNume());
        e.appendChild(nume);

        Element prenume = document.createElement("prenume");
        prenume.setTextContent(entity.getPrenume());
        e.appendChild(prenume);

        Element grupa = document.createElement("grupa");
        grupa.setTextContent(entity.getGrupa());
        e.appendChild(grupa);

        Element email = document.createElement("email");
        email.setTextContent(entity.getEmail());
        e.appendChild(email);

        Element indrumatorLab = document.createElement("indrumatorLab");
        indrumatorLab.setTextContent(entity.getIndrumatorLab());
        e.appendChild(indrumatorLab);

        return e;
    }

    @Override
    public Student createEntityFromElement(Element element) {
        String studentId = element.getAttribute("idStudent");
        NodeList nods = element.getChildNodes();
        String nume =element.getElementsByTagName("nume")
                .item(0)
                .getTextContent();

        String prenume =element.getElementsByTagName("prenume")
                .item(0)
                .getTextContent();

        String grupa =element.getElementsByTagName("grupa")
                .item(0)
                .getTextContent();

        String email =element.getElementsByTagName("email")
                .item(0)
                .getTextContent();

        String indrumatorLab =element.getElementsByTagName("indrumatorLab")
                .item(0)
                .getTextContent();

        return new Student(studentId,nume,prenume,grupa,email,indrumatorLab);
    }
}
