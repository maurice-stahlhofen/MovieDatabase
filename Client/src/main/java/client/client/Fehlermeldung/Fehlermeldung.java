package client.client.Fehlermeldung;

import javafx.beans.property.SimpleStringProperty;

public class Fehlermeldung {

    private SimpleStringProperty filmName;
    private SimpleStringProperty nachricht;
    private SimpleStringProperty date;
    private SimpleStringProperty id;

    public Fehlermeldung(String filmname, String nachricht, String date, String id) {
        this.filmName = new SimpleStringProperty(filmname);
        this.nachricht = new SimpleStringProperty(nachricht);
        this.date = new SimpleStringProperty(date);
        this.id = new SimpleStringProperty(id);
    }

    public String getFilmName() { return filmName.get(); }

    public void setFilmName(String filmname) {
        this.filmName = new SimpleStringProperty(filmname);
    }

    public String getNachricht() { return nachricht.get(); }

    public void setNachricht(String nachricht) {
        this.nachricht = new SimpleStringProperty(nachricht);
    }

    public String getDate() { return date.get(); }

    public void setDate(String date) { this.date = new SimpleStringProperty(date); }

    public String getId() { return id.get(); }

    public void setId(String id) { this.id = new SimpleStringProperty(id); }
}
