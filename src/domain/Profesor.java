package domain;

import java.util.Objects;

public class Profesor implements HasID<String>{
    private String id;
    private String nume;
    private String email;

    public Profesor(String id, String nume, String email) {
        this.id = id;
        this.nume = nume;
        this.email = email;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public void setID(String id) {
        this.id=id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profesor)) return false;
        Profesor profesor = (Profesor) o;
        return Objects.equals(id, profesor.id) &&
                Objects.equals(getNume(), profesor.getNume()) &&
                Objects.equals(getEmail(), profesor.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getNume(), getEmail());
    }

    @Override
    public String toString() {
        return id+"/"+nume+"/"+email;
    }
}
