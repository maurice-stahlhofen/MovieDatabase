package com.example.Spring2.entities;

import javax.persistence.*;

//import java.sql.Blob;
@Entity
/*
Controller, Annotationen bei den Entitäten, Repositories
sind an diesen Aufbau angelehnt:
https://www.baeldung.com/rest-http-put-vs-post
 */
public class Film {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int filmId;

    private String name;

    private String kategorie;

    private String filmlaenge;

    private String erscheinungsdatum;

    private String regisseur;

    private String drehbuchautor;

    private String cast;
    @Lob
    private byte[] filmbanner;

    public Film(int filmId, String name, String kategorie, String filmlaenge, String erscheinungsdatum, String regisseur, String drehbuchautor, String cast, byte[] filmbanner) {
        this.filmId= filmId;
        this.name = name;
        this.kategorie = kategorie;
        this.filmlaenge = filmlaenge;
        this.erscheinungsdatum = erscheinungsdatum;
        this.regisseur = regisseur;
        this.drehbuchautor = drehbuchautor;
        this.cast = cast;
        this.filmbanner = filmbanner;
    }

    public Film(String name, String kategorie, String filmlaenge, String erscheinungsdatum, String regisseur, String drehbuchautor, String cast, byte[] filmbanner) {
        this.name = name;
        this.kategorie = kategorie;
        this.filmlaenge = filmlaenge;
        this.erscheinungsdatum = erscheinungsdatum;
        this.regisseur = regisseur;
        this.drehbuchautor = drehbuchautor;
        this.cast = cast;
        this.filmbanner = filmbanner;
    }
    public Film(int filmId, String name, String kategorie, String filmlaenge, String erscheinungsdatum, String regisseur, String drehbuchautor, String cast) {
        this.filmId= filmId;
        this.name = name;
        this.kategorie = kategorie;
        this.filmlaenge = filmlaenge;
        this.erscheinungsdatum = erscheinungsdatum;
        this.regisseur = regisseur;
        this.drehbuchautor = drehbuchautor;
        this.cast = cast;
    }

    public Film() {}

    public int getFilmId() {
        return filmId;
    }

    public String getName() {
        return name;
    }

    public String getKategorie() {
        return kategorie;
    }

    public String getFilmlaenge() {
        return filmlaenge;
    }

    public String getErscheinungsdatum() {
        return erscheinungsdatum;
    }

    public String getRegisseur() {
        return regisseur;
    }

    public String getDrehbuchautor() {
        return drehbuchautor;
    }

    public String getCast() {
        return cast;
    }

    public byte[] getFilmbanner() {
        return filmbanner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public void setFilmlaenge(String filmlaenge) {
        this.filmlaenge = filmlaenge;
    }

    public void setErscheinungsdatum(String erscheinungsdatum) {
        this.erscheinungsdatum = erscheinungsdatum;
    }

    public void setRegisseur(String regisseur) {
        this.regisseur = regisseur;
    }

    public void setDrehbuchautor(String drehbuchautor) {
        this.drehbuchautor = drehbuchautor;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public void setFilmbanner(byte[] filmbanner) {
        this.filmbanner = filmbanner;
    }
}

