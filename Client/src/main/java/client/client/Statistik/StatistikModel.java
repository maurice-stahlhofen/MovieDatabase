package client.client.Statistik;

import com.itextpdf.text.pdf.PdfPCell;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class StatistikModel {

    private SimpleIntegerProperty filmID;

    private SimpleStringProperty filmName;

    private SimpleDoubleProperty durchBewertung;

    private SimpleIntegerProperty anzBewertung;

    private SimpleIntegerProperty anzGesehenFilm;

    public StatistikModel(Integer filmID, String filmName, Double durchBewertung, Integer anzBewertung, Integer anzGesehenFilm){
        this.filmID=new SimpleIntegerProperty(filmID);
        this.filmName=new SimpleStringProperty(filmName);
        this.durchBewertung=new SimpleDoubleProperty(durchBewertung);
        this.anzBewertung=new SimpleIntegerProperty(anzBewertung);
        this.anzGesehenFilm=new SimpleIntegerProperty(anzGesehenFilm);

    }

    public int getFilmID() {
        return filmID.get();
    }

    public SimpleIntegerProperty filmIDProperty() {
        return filmID;
    }

    public void setFilmID(int filmID) {
        this.filmID.set(filmID);
    }

    public String getFilmName() {
        return filmName.get();
    }

    public SimpleStringProperty filmNameProperty() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName.set(filmName);
    }

    public double getDurchBewertung() {
        return durchBewertung.get();
    }

    public SimpleDoubleProperty durchBewertungProperty() {
        return durchBewertung;
    }

    public void setDurchBewertung(double durchBewertung) {
        this.durchBewertung.set(durchBewertung);
    }

    public void setDurchBewertung(int durchBewertung) {
        this.durchBewertung.set(durchBewertung);
    }

    public int getAnzBewertung() {
        return anzBewertung.get();
    }

    public SimpleIntegerProperty anzBewertungProperty() {
        return anzBewertung;
    }

    public void setAnzBewertung(int anzBewertung) {
        this.anzBewertung.set(anzBewertung);
    }

    public int getAnzGesehenFilm() {
        return anzGesehenFilm.get();
    }

    public SimpleIntegerProperty anzGesehenFilmProperty() {
        return anzGesehenFilm;
    }

    public void setAnzGesehenFilm(int anzGesehenFilm) {
        this.anzGesehenFilm.set(anzGesehenFilm);
    }
}
