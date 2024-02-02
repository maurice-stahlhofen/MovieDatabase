package com.example.Spring2.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Freundesliste {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String emailNutzer;

    private String emailFreund;


    public Freundesliste(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
