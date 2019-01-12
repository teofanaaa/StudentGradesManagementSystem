package domain;

import java.io.Serializable;

/**
 * Clasa Utilizator
 */
public class Utilizator implements Serializable, HasID<String> {
    private String username;
    private String hash;
    private TipUtilizator tip;
    private String nume;

    /**
     * Constructorul clasei
     * @param username - string (numele de utilizator)
     * @param hash - string (parola criptata)
     * @param nume - string (numele utilizatorului)
     */
    public Utilizator(String username, String hash, String nume) {
        this.username = username;
        this.hash = hash;
        this.tip = TipUtilizator.ADMIN;
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public enum TipUtilizator {
        ADMIN, PROFESOR, STUDENT
    }

    @Override
    public String getID() {
        return username;
    }

    @Override
    public void setID(String s) {
        this.username=s;
    }

    public Utilizator(String username, String hash, TipUtilizator type,String nume) {
        this.username = username;
        this.hash = hash;
        this.tip = type;
        this.nume=nume;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public TipUtilizator getTip() {
        return tip;
    }

    public void setTip(TipUtilizator tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return username + "/" + hash + "/" + tip+"/"+nume;
    }


}