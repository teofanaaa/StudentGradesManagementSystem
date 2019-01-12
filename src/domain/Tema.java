package domain;

import java.util.Objects;

/**
 * Clasa Tema
 */
public class Tema implements HasID<String>{
    private String idTema;
    private String descriere;
    private String deadline;
    private String dataPredare;

    /**
     * Constructorul clasei
     * @param idTema - string (id tema)
     * @param descriere - string (detalii despre tema)
     * @param deadline - string (data limita la care poate fi adusa tema: 1...14)
     * @param dataPredare - string (data la care a fost data tema: 1...14)
     */
    public Tema(String idTema, String descriere, String deadline,String dataPredare) {
        this.idTema = idTema;
        this.descriere = descriere;
        this.deadline = deadline;
        this.dataPredare = dataPredare;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDataPredare() {
        return dataPredare;
    }

    public void setDataPredare(String dataPredare) {
        this.dataPredare = dataPredare;
    }

    @Override
    public String getID() {
        return this.idTema;
    }

    @Override
    public void setID(String integer) {
        this.idTema=integer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tema)) return false;
        Tema tema = (Tema) o;
        return Objects.equals(idTema, tema.idTema) &&
                Objects.equals(getDescriere(), tema.getDescriere()) &&
                Objects.equals(getDeadline(), tema.getDeadline()) &&
                Objects.equals(getDataPredare(), tema.getDataPredare());
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTema, getDescriere(), getDeadline(), getDataPredare());
    }

    @Override
    public String toString() {
        return idTema + '/' +
                descriere + '/' +
                deadline + '/' +
                dataPredare;
    }
}