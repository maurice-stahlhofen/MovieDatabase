package com.example.Spring2.FehlermeldungPackage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Fehlermeldung {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int fehlermeldungsId;
    private String filmName;
    private String nachricht;
    private String date;

    public Fehlermeldung() { }


    public int getFehlermeldungsId() { return fehlermeldungsId; }
    public String getFilmName() { return filmName; }
    public void setFilmName(String filmname) { this.filmName = filmname; }
    public String getNachricht() { return nachricht; }
    public void setNachricht(String nachricht) { this.nachricht = nachricht; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

}
