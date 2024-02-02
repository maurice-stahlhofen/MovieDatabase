package com.example.Spring2.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Watchlist {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int watchlistId;

    private String emailNutzer;

    private int filmId;

    public Watchlist(){

    }

    public int getWatchlistId() {
        return watchlistId;
    }


    public String getEmailNutzer() {
        return emailNutzer;
    }

    public void setEmailNutzer(String emailNutzer) {
        this.emailNutzer = emailNutzer;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }
}
