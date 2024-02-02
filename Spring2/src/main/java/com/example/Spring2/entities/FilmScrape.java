package com.example.Spring2.entities;

public class FilmScrape {
    //ohne id und kein table
    //nur damit die Attribute stimmen
    private String name;

    private String kategorie;

    private String filmlaenge;

    private String erscheinungsdatum;

    private String regisseur;

    private String drehbuchautor;

    private String cast;

    public FilmScrape() {
    }

    public FilmScrape( String name, String kategorie,
                       String filmlaenge, String erscheinungsdatum,
                       String regisseur, String drehbuchautor,
                       String cast) {

        this.name = name;
        this.kategorie = kategorie;
        this.filmlaenge = filmlaenge;
        this.erscheinungsdatum = erscheinungsdatum;
        this.regisseur = regisseur;
        this.drehbuchautor = drehbuchautor;
        this.cast = cast;

    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getKategorie() {
        return kategorie;
    }
    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public String getFilmlaenge() {
        return filmlaenge;
    }
    public void setFilmlaenge(String filmlaenge) {
        this.filmlaenge = filmlaenge;
    }

    public String getErscheinungsdatum() {
        return erscheinungsdatum;
    }
    public void setErscheinungsdatum(String erscheinungsdatum) {
        this.erscheinungsdatum = erscheinungsdatum;
    }

    public String getRegisseur() {
        return regisseur;
    }
    public void setRegisseur(String regisseur) {
        this.regisseur = regisseur;
    }

    public String getDrehbuchautor() {
        return drehbuchautor;
    }
    public void setDrehbuchautor(String drehbuchautor) {
        this.drehbuchautor = drehbuchautor;
    }

    public String getCast() {
        return cast;
    }
    public void setCast(String cast) {
        this.cast = cast;
    }

    @Override
    public String toString() {
        return  "Name: " + name +" "+
                erscheinungsdatum  +
                "\nKategorie: " + kategorie +
                "\nFilmlänge: " + filmlaenge +
                "\nRegisseur: " + regisseur +
                "\nDrehbuchautor: " + drehbuchautor +
                "\nCast: " + cast;
    }
}

