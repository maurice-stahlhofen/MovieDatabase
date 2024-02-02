package com.example.Spring2.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class GeseheneFilme {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int geseheneFilmeId;

    private String emailNutzer;

    @OneToOne
    @JoinColumn(name = "FK_film_id")
    private Film filmId;

    public Film getFilmId() {
        return filmId;
    }

    public void setFilmId(Film filmId) {
        this.filmId = filmId;
    }

    private LocalDate date;

    public GeseheneFilme(){

    }

    public String getEmailNutzer() {
        return emailNutzer;
    }

    public void setEmailNutzer(String emailNutzer) {
        this.emailNutzer = emailNutzer;
    }



    public int getGeseheneFilmeId() {
        return geseheneFilmeId;
    }

    public LocalDate getDate() { return date; }
}
