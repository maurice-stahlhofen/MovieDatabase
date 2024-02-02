package com.example.Spring2.entities;

import com.example.Spring2.Sichtbarkeitsstufen;

import javax.persistence.*;
import java.util.Date;

import static com.example.Spring2.Sichtbarkeitsstufen.ALLE;

@Entity
/*
Controller, Annotationen bei den Entitäten, Repositories
sind an diesen Aufbau angelehnt:
https://www.baeldung.com/rest-http-put-vs-post
 */
public class Nutzer {
    @Id
    private String email;
    private String vorname;

    private String nachname;

    private String gebDatum;

    private String passwort;

    @Enumerated(EnumType.ORDINAL)
    private Sichtbarkeitsstufen sichtbarkeitsstufen;

    private final int userType;

    @Lob
    private byte[] zProfilbild;


    public Nutzer(String vorname, String nachname, String gebDatum, String passwort, String emailadresse, byte[] profilbild) {
        this.gebDatum = gebDatum;
        this.zProfilbild = profilbild;
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
        this.email = emailadresse;
        this.userType = 2;
        this.sichtbarkeitsstufen = ALLE;

    }


    public int getUserType() {
        return userType;
    }

    public Nutzer() {
        setSichtbarkeitsstufen(ALLE);
        this.userType = 2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGebDatum() {
        return gebDatum;
    }

    public void setGebDatum(String gebDatum) {
        this.gebDatum = gebDatum;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public Sichtbarkeitsstufen getSichtbarkeitsstufen() {
        return sichtbarkeitsstufen;
    }

    public byte[] getzProfilbild() {
        return zProfilbild;
    }

    public void setzProfilbild(byte[] zProfilbild) {
        this.zProfilbild = zProfilbild;
    }

    public void setSichtbarkeitsstufen(Sichtbarkeitsstufen sichtbarkeitsstufen) {
        this.sichtbarkeitsstufen = sichtbarkeitsstufen;
    }


}