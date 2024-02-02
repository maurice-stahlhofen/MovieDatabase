package client.client.Anlegen;

import Controller.ClientServerHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;

import java.io.IOException;

public class AutomatischAnlegenController {
    @FXML
    public TextField zeitraumVonField, zeitraumBisField, kategorieField, anzahlField;
    public Button anlegenButton, startseiteButton;

    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    ClientServerHandler CShandler = new ClientServerHandler();


    public AutomatischAnlegenController() throws IOException{
    }


    public void startseitePressed() throws IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteAdminView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void automatischAnlegenPressed() throws IOException,InterruptedException{

        //Sollte die scraper methode keine liste mit filmobjekten liefern sondern JSONObjects, einfach den Typ der "filme" liste auf JSONOBjects ändern und in den for schleifen die JSONObject erstellung + die film.put blöcke entfernen
        //und in den PostRequests einfach statt "film" "filme.get(i)" einfügen

        try {
            //Wenn keine Anzahl eingegeben wird wird eine Fehlermeldung ausgegeben
            if (anzahlField.getText() == "") {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setHeaderText("Eingabe Fehler");
                alert.setContentText("Bitte eine Anzahl eingeben");
                alert.showAndWait();
            }


            //Fall zum Scrapen basierend auf Kategorie
            if (anzahlField.getText() != "" && kategorieField.getText() != "" && zeitraumVonField.getText() == "" && zeitraumBisField.getText() == "") {
                if (Integer.parseInt(anzahlField.getText()) >= 0) {
                  //  if(kategorieField.getText()!=""){ //wird schon oben abgefragt
                        if(!checkKategorie(kategorieField.getText())){
                            alert.setAlertType(Alert.AlertType.WARNING);
                            alert.setHeaderText("Eingabe Fehler");
                            alert.setContentText("Bitte eine gültige Kategorie eingeben");
                            alert.showAndWait();
                        }else{
                            JSONArray filmarray = new JSONArray();
                            filmarray = CShandler.sendGETRequest("/scrape/genres=" + kategorieField.getText() + "/" + Integer.parseInt(anzahlField.getText()));
                            //hier filmarray=CSH.sendGETRequest zum zuweisen der gescrapten liste
                            for (int i = 0; i < filmarray.length(); i++) {
                                CShandler.sendPostRequest("/controller/film/add", filmarray.getJSONObject(i));
                            }
                            alert.setAlertType(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Anlegen erfolgreich!");
                            alert.setContentText("Ihre Anfrage wurde korrekt verarbeitet!\nSie können nun weitere Filme importieren.");
                            alert.showAndWait();
                        }
//                    }else {       //wird nie auftreten weil die obere if abfrage (jetzt auch auskommentiert) immer true sein wird
//                        //scrapen mit (kategorieField.getText(),0,0,Integer.parseInt(anzahlField.getText()))
//                        JSONArray filmarray = new JSONArray();
//                        filmarray = CShandler.sendGETRequest("/scrape/genres=" + kategorieField.getText() + "/" + Integer.parseInt(anzahlField.getText()));
//                        //hier filmarray=CSH.sendGETRequest zum zuweisen der gescrapten liste
//                        for (int i = 0; i < filmarray.length(); i++) {
//
//                            //System.out.println(filmarray.getJSONObject(i));
//                            CShandler.sendPostRequest("/controller/film/add", filmarray.getJSONObject(i));
//
//                        }
//                    }
                } else {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setHeaderText("Eingabe Fehler");
                    alert.setContentText("Anzahl kleiner als 0");
                    alert.showAndWait();
                }
            }

            //Fall zum Scrapen basierend auf Kategorie und einem Zeitraum/zwei Daten
            if (anzahlField.getText() != "" && kategorieField.getText() != "" && zeitraumVonField.getText() != "" && zeitraumBisField.getText() != "") {
                if (Integer.parseInt(anzahlField.getText()) >= 0 && Integer.parseInt(zeitraumVonField.getText()) >= 1980 && Integer.parseInt(zeitraumVonField.getText()) <= 2023
                        && Integer.parseInt(zeitraumBisField.getText()) >= 1980 && Integer.parseInt(zeitraumBisField.getText()) <= 2023) {
                    if (checkKategorie(kategorieField.getText())) {

                        //scrapen mit (kategorieField.getTExt(),Integer.parseInt(zeitraumVonField.getText()),Integer.parseInt(zeitraumBisField.getText()),Integer.parseInt(anzahlField.getText()))
                        if (Integer.parseInt(zeitraumVonField.getText()) <= Integer.parseInt(zeitraumBisField.getText())) {
                            JSONArray filmarray = new JSONArray();
                            //hier filmarray=CSH.sendGETRequest zum zuweisen der gescrapten liste
                            filmarray = CShandler.sendGETRequest("/scrape/year=" + Integer.parseInt(zeitraumVonField.getText()) + "," + Integer.parseInt(zeitraumBisField.getText()) + "/genres=" + kategorieField.getText() + "/" + Integer.parseInt(anzahlField.getText()));
                            for (int i = 0; i < filmarray.length(); i++) {

                                //System.out.println(filmarray.getJSONObject(i));
                                CShandler.sendPostRequest("/controller/film/add", filmarray.getJSONObject(i));
                            }
                            alert.setAlertType(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Anlegen erfolgreich!");
                            alert.setContentText("Ihre Anfrage wurde korrekt verarbeitet!\nSie können nun weitere Filme importieren.");
                            alert.showAndWait();
                        } else {
                            alert.setAlertType(Alert.AlertType.WARNING);
                            alert.setHeaderText("Achtung Jahreszahlen");
                            alert.setContentText("Der Anfangszeitraum muss kleiner als der Endzeitraum sein!");
                            alert.showAndWait();
                        }
                    } else {
                        alert.setAlertType(Alert.AlertType.WARNING);
                        alert.setHeaderText("Eingabe Fehler");
                        alert.setContentText("Bitte eine gültige Kategorie eingeben");
                        alert.showAndWait();
                    }

                }else {
                        alert.setAlertType(Alert.AlertType.WARNING);
                        alert.setHeaderText("Eingabe Fehler");
                        alert.setContentText("Anzahl oder Daten kleiner als 1980 oder Daten größer als 2023");
                        alert.showAndWait();

                }
            }

            //Fall zum Scrapen basierend auf einem Zeitraum/ zwei Daten
            if (anzahlField.getText() != "" && kategorieField.getText() == "" && zeitraumVonField.getText() != "" && zeitraumBisField.getText() != "") {
                if (Integer.parseInt(anzahlField.getText()) >= 0 && Integer.parseInt(zeitraumVonField.getText()) >= 1980 && Integer.parseInt(zeitraumVonField.getText()) <= 2023 && Integer.parseInt(zeitraumBisField.getText()) >= 1980 && Integer.parseInt(zeitraumBisField.getText()) <= 2023) {
                    //scrapen mit ("",Integer.parseInt(zeitraumVonField.getText()),Integer.parseInt(zeitraumBisField.getText()),Integer.parseInt(anzahlField.getText()))
                    if (Integer.parseInt(zeitraumVonField.getText()) <= Integer.parseInt(zeitraumBisField.getText())) {
                        JSONArray filmarray = new JSONArray();
                        //hier filmarray=CSH.sendGETRequest zum zuweisen der gescrapten liste
                        filmarray = CShandler.sendGETRequest("/scrape/year=" + Integer.parseInt(zeitraumVonField.getText()) + "," + Integer.parseInt(zeitraumBisField.getText()) + "/" + Integer.parseInt(anzahlField.getText()));
                        for (int i = 0; i < filmarray.length(); i++) {

                            //System.out.println(filmarray.getJSONObject(i));
                            CShandler.sendPostRequest("/controller/film/add", filmarray.getJSONObject(i));
                        }
                        alert.setAlertType(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Anlegen erfolgreich!");
                        alert.setContentText("Ihre Anfrage wurde korrekt verarbeitet!\nSie können nun weitere Filme importieren.");
                        alert.showAndWait();
                    }else{
                        alert.setAlertType(Alert.AlertType.WARNING);
                        alert.setHeaderText("Achtung Jahreszahlen");
                        alert.setContentText("Der Anfangszeitraum muss kleiner als der Endzeitraum sein!");
                        alert.showAndWait();
                    }
                } else {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setHeaderText("Eingabe Fehler");
                    alert.setContentText("Anzahl oder Datum kleiner als 1980 oder Datum größer als 2023");
                    alert.showAndWait();
                }
            }
            //Wenn nur eine Anzahl eingegeben wird
            if (anzahlField.getText() != "" && kategorieField.getText() == "" && zeitraumVonField.getText() == "" && zeitraumBisField.getText() == "") {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setHeaderText("Eingabe Fehler");
                alert.setContentText("Bitte mindestens einen Parameter eingeben");
                alert.showAndWait();

            }

            if(zeitraumVonField.getText()!=""){
                if(Integer.parseInt(zeitraumVonField.getText())<0){
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setHeaderText("Eingabe Fehler");
                    alert.setContentText("Keine Zahl über -1 eingegeben");
                    alert.showAndWait();
                }
            }

            if(zeitraumVonField.getText()!=""&&zeitraumBisField.getText()==""){
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setHeaderText("Eingabe Fehler");
                alert.setContentText("Bitte eine zweite Jahreszahl eingeben");
                alert.showAndWait();
            }

            if(zeitraumBisField.getText()!=""&&zeitraumVonField.getText()==""){
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setHeaderText("Eingabe Fehler");
                alert.setContentText("Bitte eine start Jahr eingeben");
                alert.showAndWait();
            }
        }catch(NumberFormatException e){
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setHeaderText("Eingabe Fehler");
            alert.setContentText("Es wurde anstatt einer Zahl Buchstaben eingetragen!");
            alert.showAndWait();
        }
    }
     //Von https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
    public boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    private boolean checkKategorie(String kategorie) {
        String[] genre={"Comedy", "Sci-Fi", "Horror", "Romance", "Action", "Thriller", "Drama", "Mystery",
                "Crime", "Animation", "Adventure", "Fantasy", "Superhero"};
        for(String check:genre){
            if(kategorie.equals(check)){
                return true;
            }
        }
        return false;
    }


}