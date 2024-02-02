package client.client.Soziales;

import client.client.Zwischenspeicher;

public class FremderUser {



    private static FremderUser speicher;
    private String email;

    private String vorname;

    private String nachname;

    private String sichtbarkeit;

    public static FremderUser getSpeicher(){
        if(speicher == null){
            speicher = new FremderUser();
        }

        return speicher;
    }

    FremderUser(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSichtbarkeit() {
        return sichtbarkeit;
    }

    public void setSichtbarkeit(String sichtbarkeit) {
        this.sichtbarkeit = sichtbarkeit;
    }
}
