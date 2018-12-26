package domain;

import java.util.Objects;

public class Student implements HasID<String> {
    private String idStudent;
    private String nume;
    private String prenume;
    private String grupa;
    private String email;
    private String indrumatorLab;

    public Student(String idStudent, String nume, String prenume, String grupa, String email, String indrumatorLab) {
        this.idStudent = idStudent;
        this.nume = nume;
        this.prenume = prenume;
        this.grupa = grupa;
        this.email = email;
        this.indrumatorLab = indrumatorLab;
    }

    public String getNume() {
        return this.nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() { return prenume; }

    public void setPrenume(String prenume) { this.prenume = prenume; }

    public String getGrupa() {
        return this.grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIndrumatorLab() {
        return this.indrumatorLab;
    }

    public void setIndrumatorLab(String indrumatorLab) {
        this.indrumatorLab = indrumatorLab;
    }

    @Override
    public String getID() {
        return this.idStudent;
    }

    @Override
    public void setID(String integer) {
        this.idStudent=integer;
    }

    @Override
    public String toString() {
        return this.idStudent +
                "/" + this.nume +
                "/" + this.prenume +
                "/" + this.grupa +
                "/" + this.email +
                "/" + this.indrumatorLab;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(idStudent, student.idStudent) &&
                Objects.equals(getNume(), student.getNume()) &&
                Objects.equals(getPrenume(), student.getPrenume()) &&
                Objects.equals(getGrupa(), student.getGrupa()) &&
                Objects.equals(getEmail(), student.getEmail()) &&
                Objects.equals(getIndrumatorLab(), student.getIndrumatorLab());
    }

    @Override
    public int hashCode() {
        return Objects.hash(idStudent, getNume(), getPrenume(), getGrupa(), getEmail(), getIndrumatorLab());
    }
}