package client.client.login;

public class AngemeldeterUser {



    private static AngemeldeterUser speicher;
    private String email;

    private String vorname;

    private String nachname;

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    private String passwort;

    public static AngemeldeterUser getSpeicher(){
        if(speicher == null){
            speicher = new AngemeldeterUser();
        }

        return speicher;
    }

    private AngemeldeterUser(){

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
}
