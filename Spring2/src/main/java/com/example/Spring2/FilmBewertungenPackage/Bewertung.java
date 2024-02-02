package com.example.Spring2.FilmBewertungenPackage;

import com.example.Spring2.entities.Film;

import javax.persistence.*;

@Entity
public class Bewertung {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int bewertungsId;

    @OneToOne
    @JoinColumn(name = "FK_film_id")
    private Film filmId;

    private int sterne;

    private String bewertungsText;

    private String nutzerEmail;

    public Bewertung(){

    }



    public int getBewertungsId() {
        return bewertungsId;
    }

    public Film getFilmId() {
        return filmId;
    }

    public void setFilmId(Film filmId) {
        this.filmId = filmId;
    }

    public int getSterne() {
        return sterne;
    }

    public void setSterne(int sterne) {
        this.sterne = sterne;
    }

    public String getBewertungsText() {
        return bewertungsText;
    }

    public void setBewertungsText(String bewertungsText) {
        this.bewertungsText = bewertungsText;
    }

    public String getNutzerEmail(){
        return nutzerEmail;
    }

    public void setNutzerEmail(String nutzerEmail){
        this.nutzerEmail=nutzerEmail;
    }
}
