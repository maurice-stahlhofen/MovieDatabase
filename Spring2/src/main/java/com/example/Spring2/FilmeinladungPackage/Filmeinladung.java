package com.example.Spring2.FilmeinladungPackage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Filmeinladung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String film;

    private String vonNutzerEmail;

    private String anNutzerEmail;

    private String datum;

    private String uhrzeit;

    private String nachricht;

    private Boolean angenommen;

    public Filmeinladung(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVonNutzerEmail() {
        return vonNutzerEmail;
    }

    public void setVonNutzerEmail(String vonNutzerEmail) {
        this.vonNutzerEmail = vonNutzerEmail;
    }

    public String getAnNutzerEmail() {
        return anNutzerEmail;
    }

    public void setAnNutzerEmail(String anNutzerEmail) {
        this.anNutzerEmail = anNutzerEmail;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getUhrzeit() {
        return uhrzeit;
    }

    public void setUhrzeit(String uhrzeit) {
        this.uhrzeit = uhrzeit;
    }

    public String getNachricht() {
        return nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public Boolean getAngenommen() {
        return angenommen;
    }

    public void setAngenommen(Boolean angenommen) {
        this.angenommen = angenommen;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }
}
