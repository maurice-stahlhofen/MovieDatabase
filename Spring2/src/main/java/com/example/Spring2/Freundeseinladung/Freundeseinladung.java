package com.example.Spring2.Freundeseinladung;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Freundeseinladung {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String emailNutzer;
    private String emailFreund;

    public Freundeseinladung() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailNutzer() {
        return emailNutzer;
    }

    public void setEmailNutzer(String emailNutzer) {
        this.emailNutzer = emailNutzer;
    }

    public String getEmailFreund() {
        return emailFreund;
    }

    public void setEmailFreund(String emailFreund) {
        this.emailFreund = emailFreund;
    }
}
