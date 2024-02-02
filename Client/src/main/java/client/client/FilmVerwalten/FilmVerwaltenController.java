package client.client.FilmVerwalten;

import Controller.ClientServerHandler;
import client.client.Zwischenspeicher;
import client.client.login.AngemeldeterUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class FilmVerwaltenController {

    @FXML
    public Button backButton, startseiteButton, filmBewertenButton;
    public TextField zahlenBewertungTextField, textBewertungTextField;

    Alert popUp;
    JSONObject angemeldeterUser;
    JSONArray alleFilme = new JSONArray();
    JSONArray alleNutzer = new JSONArray();
    JSONArray alleBewertungen = new JSONArray();
    ClientServerHandler csh = new ClientServerHandler();

    public FilmVerwaltenController() throws MalformedURLException {
    }

    public void initialize(){
        //damit der JUNIt test nicht meckert
        popUp=new Alert(Alert.AlertType.INFORMATION);
    }
    //Von https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
    public boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public void filmBewertenPressed() throws IOException, InterruptedException {

        int speicherID = Zwischenspeicher.getSpeicher().getFilmId();

        JSONArray eachMovie = csh.sendGETRequest("/controller/film");



    //überprüft ob Pflichtfeld eine Zahl enthält
    if(isNumeric(zahlenBewertungTextField.getText())==false){
        popUp.setAlertType(Alert.AlertType.WARNING);
        popUp.setHeaderText("Fehler");
        popUp.setContentText("Bitte eine Zahl eingeben!");
        popUp.showAndWait();
        return;
    }

        //überprüft ob Eingabe im Gültigkeitsbereich (1-5 Sterne) ist
        if(Integer.parseInt(zahlenBewertungTextField.getText())>0 && Integer.parseInt(zahlenBewertungTextField.getText())<6){

            //füllt den JSON Array mit allen Nutzern
            alleNutzer = csh.sendGETRequest("/controller/nutzer");

            //Überprüft den angemeldeten Nutzer
            for(int i = 0; i<alleNutzer.length(); i++){
                JSONObject temp = alleNutzer.getJSONObject(i);
                if(temp.get("email").toString().equals(AngemeldeterUser.getSpeicher().getEmail())){
                    this.angemeldeterUser=temp;
                }
            }

            // Füllt das JsonObject "bewertung" mit der eingegeben Rezension und der Email zur Identifizierung
            JSONObject bewertung = new JSONObject();
            bewertung.put("sterne", zahlenBewertungTextField.getText());
            bewertung.put("bewertungsText", textBewertungTextField.getText());
            bewertung.put("nutzerEmail", AngemeldeterUser.getSpeicher().getEmail());

            //füllt den JSON Array mit allen Bewertungen
            alleBewertungen = csh.sendGETRequest("/controller/bewertung");

            //Überprüft ob eine Bewertung des Nutzer zum Film vorhanden ist
                for(int i = 0; i<alleBewertungen.length(); i++){
                    JSONObject temp2 = alleBewertungen.getJSONObject(i);
                    JSONObject film = temp2.getJSONObject("filmId");
                    int filmId = (int) film.get("filmId");
                    //if abfrage ob bereits eine bewertung existiert mit "email" und "filmId" leitet zu bewertungAktualisieren
                    if(temp2.get("nutzerEmail").toString().equals(bewertung.get("nutzerEmail").toString()) && filmId==speicherID){
                        bewertung.put("filmId", ((JSONObject) temp2.get("filmId")).get("filmId").toString());
                        bewertungAktualisieren(bewertung);
                        return;
                    }
                }

            alleFilme = csh.sendGETRequest("/controller/film");

                    //sendet die Bewertung an den Server in den Pfad
                    csh.sendPostRequest("/controller/bewertung/add/"+speicherID, bewertung);
                    popUp.setAlertType(Alert.AlertType.INFORMATION);
                    popUp.setHeaderText("Bestätigung");
                    popUp.setContentText("Deine Film Bewertung wurde abgesendet");
                    popUp.showAndWait();
                //}
            //}
        }

        else{
            popUp.setAlertType(Alert.AlertType.WARNING);
            popUp.setHeaderText("Fehler");
            popUp.setContentText("Bitte eine Bewertungszahl im Bereich von 1-5 eingeben!");
            popUp.showAndWait();
        }
    }

    public void bewertungAktualisieren(JSONObject bewertung) throws IOException, InterruptedException {
        //füllt das JSONObject Banjo mit allen benötigten Daten zum Ändern der Bewertung
        JSONObject banjo = new JSONObject();
        banjo.put("sterne", bewertung.get("sterne"));
        banjo.put("bewertungsText", bewertung.get("bewertungsText"));
        banjo.put("nutzerEmail", bewertung.get("nutzerEmail"));

        //füllt den JSON Array mit allen Bewertungen
        alleBewertungen = csh.sendGETRequest("/controller/bewertung");

        for(int i = 0; i<alleBewertungen.length(); i++){
            JSONObject speicher = alleBewertungen.getJSONObject(i);
            //if abfrage mit "email" und "filmId" um die BewertungsID herauszufinden
            if(speicher.get("nutzerEmail").toString().equals(bewertung.get("nutzerEmail").toString()) && ((JSONObject) speicher.get("filmId")).get("filmId").toString().equals(bewertung.get("filmId").toString())){
                bewertung.put("bewertungsId", speicher.get("bewertungsId"));
            }
        }

        //Wandelt "bewertungsId" vom Typ Object in einen int Wert um, um die Putrequest an den richtigen Pfad zu senden
        int pfad = Integer.parseInt(bewertung.get("bewertungsId").toString());

        csh.sendPutRequest("/bewertungs/"+pfad,banjo);
        popUp.setAlertType(Alert.AlertType.INFORMATION);
        popUp.setHeaderText("Bestätigung");
        popUp.setContentText("Deine Film Bewertung wurde aktualisiert");
        popUp.showAndWait();
    }

    public void startseitePressed() throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void backButtonPressed() throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/anzeigenlistview.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }


    //Sendet die Daten an den Server
    public JSONObject testMethod(Integer sterne, String bewertungsText, Integer filmId, String email) throws IOException, InterruptedException {
        //handler sucht nach dem Objekt mit der jeweiligen id
        //Mapping ist beim Controller
        JSONArray allFilms=csh.sendGETRequest("/controller/film/id/"+filmId);
        JSONObject film=allFilms.getJSONObject(0);
        JSONObject bewertung = new JSONObject();
        bewertung.put("bewertungsText", bewertungsText);
        bewertung.put("filmId", film);
        bewertung.put("sterne", sterne);
        bewertung.put("nutzerEmail", email);

        csh.sendPostRequest("/controller/bewertung/add", bewertung);

        return bewertung;
    }
}
