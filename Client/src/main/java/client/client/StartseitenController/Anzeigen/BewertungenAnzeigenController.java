package client.client.StartseitenController.Anzeigen;

import Controller.ClientServerHandler;
import client.client.login.AngemeldeterUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


public class BewertungenAnzeigenController {


    @FXML
    public Button startseiteButton, titeleingabeButton;

    public TextField IdTextField;

    public ListView bewertungenListe;


    public Alert popUp = new Alert(Alert.AlertType.INFORMATION);

    ClientServerHandler CSHandler = new ClientServerHandler();

    ObservableList bewertungen= FXCollections.observableArrayList();


    public BewertungenAnzeigenController() throws IOException{

    }


    public void addToWatchlist() throws IOException, InterruptedException{

        JSONArray allebewertungen = new JSONArray();
        allebewertungen=CSHandler.sendGETRequest("/controller/watchlist");

        JSONObject watchlisteintrag = new JSONObject();
        watchlisteintrag.put("emailNutzer", AngemeldeterUser.getSpeicher().getEmail());
        watchlisteintrag.put("filmId",IdTextField.getText().toString());

        if(!isNumeric(IdTextField.getText().toString())){
            popUp.setAlertType(Alert.AlertType.WARNING);
            popUp.setHeaderText("Fehler");
            popUp.setContentText("Keine Film Id eingegeben");
            popUp.showAndWait();
            return;
        }

        for(int i=0;i<allebewertungen.length();i++){
            JSONObject tmp=allebewertungen.getJSONObject(i);
            if(tmp.get("emailNutzer").toString().equals(watchlisteintrag.get("emailNutzer").toString()) && tmp.get("filmId").toString().equals(watchlisteintrag.get("filmId").toString())){
                popUp.setAlertType(Alert.AlertType.WARNING);
                popUp.setHeaderText("Fehler");
                popUp.setContentText("Dieser Film befindet sich bereits auf ihrer Watchlist");
                popUp.showAndWait();
                return;
            }
        }







            CSHandler.sendPostRequest("/controller/watchlist/add", watchlisteintrag);
            popUp.setAlertType(Alert.AlertType.WARNING);
            popUp.setHeaderText("Erfolg");
            popUp.setContentText("Film erfolgreich zur Watchlist hinzugefügt");
            popUp.showAndWait();




    }

    public void startseitePressed() throws IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void zurueckPressed() throws IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/anzeigenlistview.fxml"));
        Stage stage = (Stage) titeleingabeButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void anzeigenPressed() throws IOException{

        if(!isNumeric(IdTextField.getText().toString())){
            popUp.setAlertType(Alert.AlertType.WARNING);
            popUp.setHeaderText("Fehler");
            popUp.setContentText("Keine Film Id eingegeben");
            popUp.showAndWait();
            return;
        }



        bewertungenListe.getItems().clear();
        bewertungen.clear();
        JSONArray bewertungenarray = CSHandler.sendGETRequest("/controller/bewertung/"+Integer.parseInt(IdTextField.getText().toString()));
        int letzteFilmId=0;

        JSONArray letzterFilm = CSHandler.sendGETRequest("/controller/film");
        for(int i=0;i<letzterFilm.length();i++){
            JSONObject tmp=letzterFilm.getJSONObject(i);
            if(Integer.parseInt(tmp.get("filmId").toString())>letzteFilmId){
                letzteFilmId=Integer.parseInt(tmp.get("filmId").toString());
            }
        }



        if(Integer.parseInt(IdTextField.getText().toString())>letzteFilmId){
            popUp.setAlertType(Alert.AlertType.WARNING);
            popUp.setHeaderText("Warnung");
            popUp.setContentText("Diesen Film gibt es nicht");
            popUp.showAndWait();
            return;
        }





        if(bewertungenarray.isEmpty()){
            JSONArray alleFilme = CSHandler.sendGETRequest("/controller/film");
            JSONObject dieserFilm;
            for(int i=0;i<alleFilme.length();i++){
                JSONObject tmp=alleFilme.getJSONObject(i);
                if(tmp.get("filmId").toString().equals(IdTextField.getText().toString())){
                    dieserFilm=tmp;
                    String leer="Es gibt zu '"+ dieserFilm.get("name").toString() +"' noch keine Bewertungen";
                    bewertungen.add(leer);
                }
            }

        }







        for(int i=0; i<bewertungenarray.length();i++){
            JSONObject tmp =bewertungenarray.getJSONObject(i);
            bewertungen.add(((JSONObject) tmp.get("filmId")).get("filmId")+" "+"BewertungsId: "+tmp.get("bewertungsId")+" "+ ((JSONObject) tmp.get("filmId")).get("name")+", Anzahl an Sternen: "+tmp.get("sterne").toString()+" Bewertung: "+tmp.get("bewertungsText").toString());
        }

        bewertungenListe.getItems().addAll(bewertungen);

    }

    //Von https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
    public boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?");
    }

}
