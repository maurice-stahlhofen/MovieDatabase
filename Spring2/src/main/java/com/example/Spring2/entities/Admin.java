package com.example.Spring2.entities;

import javax.persistence.*;

@Entity

/*
Controller, Annotationen bei den Entitäten, Repositories
sind an diesen Aufbau angelehnt:
https://www.baeldung.com/rest-http-put-vs-post
 */

public class Admin {
    private String vorname;

    private String nachname;
    @Id
    private String email;

    private String passwort;

    private final int userType;



    public Admin(String vorname, String nachname, String email, String passwort) {

        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.passwort = passwort;
        userType = 1;
    }

    public Admin() {

        this.userType = 1;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public int getUserType() {
        return userType;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setNachnme(String nachname) {
        this.nachname = nachname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}
