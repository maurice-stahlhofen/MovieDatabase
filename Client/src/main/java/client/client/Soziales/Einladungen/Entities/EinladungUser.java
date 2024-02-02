package client.client.Soziales.Einladungen.Entities;

public class EinladungUser {
    private String vorname;
    private String nachname;
    private String email;
    private Integer einladungID;

    public EinladungUser() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEinladungID() {
        return einladungID;
    }

    public void setEinladungID(Integer einladungID) {
        this.einladungID = einladungID;
    }

    @Override
    public String toString() {
        return vorname +" "+nachname+"; " +email;
    }

    public void delete(){
        setVorname(null);
        setNachname(null);
        setEmail(null);
        setEinladungID(null);
    }
}
