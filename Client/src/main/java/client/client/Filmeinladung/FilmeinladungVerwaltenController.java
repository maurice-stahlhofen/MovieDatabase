package client.client.Filmeinladung;


import Controller.ClientServerHandler;
import client.client.login.AngemeldeterUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class FilmeinladungVerwaltenController {

    public Button startseiteButton;

    public Button zurueckButton;

    public ListView<String> ausstehendeEinladungen;

    public ListView<String> angenommeneEinladungen;

    public ObservableList ausstehendeListe = FXCollections.observableArrayList();

    public ObservableList angenommeneListe = FXCollections.observableArrayList();

    public Alert popUp = new Alert(Alert.AlertType.WARNING);


    public void initialize() throws IOException, MalformedURLException{
        angenommeneEinladungen.getItems().clear();
        ausstehendeEinladungen.getItems().clear();
        angenommeneLaden();
        ausstehendeLaden();

    }



    public void startseitePressed() throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void zurueckPressed() throws IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/filmeinladung3.1.1.fxml"));
        Stage stage = (Stage) zurueckButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void annehmenPressed() throws IOException, InterruptedException{
        if(ausstehendeEinladungen.getSelectionModel().getSelectedItem() != null){
            JSONObject gesuchteEinladung = new JSONObject();
            ClientServerHandler csh = new ClientServerHandler();
            JSONArray alleEinladungen=  csh.sendGETRequest("/controller/filmeinladung");

            for(int i=0; i<alleEinladungen.length();i++){
                JSONObject tmp=alleEinladungen.getJSONObject(i);
                if(ausstehendeEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("datum").toString())&&ausstehendeEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("nachricht").toString())&&ausstehendeEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("uhrzeit").toString())&&ausstehendeEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("vonNutzerEmail").toString())&&ausstehendeEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("film").toString())){
                    gesuchteEinladung=tmp;
                }
            }
            JSONObject angenommeneEinladung = new JSONObject();
            angenommeneEinladung.put("anNutzerEmail", gesuchteEinladung.get("anNutzerEmail"));
            angenommeneEinladung.put("angenommen", "true");
            angenommeneEinladung.put("datum", gesuchteEinladung.get("datum"));
            angenommeneEinladung.put("nachricht", gesuchteEinladung.get("nachricht"));
            angenommeneEinladung.put("uhrzeit", gesuchteEinladung.get("uhrzeit"));
            angenommeneEinladung.put("vonNutzerEmail", gesuchteEinladung.get("vonNutzerEmail"));
            angenommeneEinladung.put("film", gesuchteEinladung.get("film"));

            csh.sendPutRequest("/filmeinladungs/"+gesuchteEinladung.get("id"),angenommeneEinladung);

            csh.sendPostRequest("/api/sendAngenommen", angenommeneEinladung);

            popUp.setAlertType(Alert.AlertType.INFORMATION);
            popUp.setHeaderText("Erfolg!");
            popUp.setContentText("Einladung angenommen");
            popUp.showAndWait();

            aktualisierenPressed();




        }

    }

    public void ablehnenPressed() throws IOException, MalformedURLException, InterruptedException{
        if(ausstehendeEinladungen.getSelectionModel().getSelectedItem()==null){
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte eine Einladung auswählen");
            popUp.showAndWait();
        }else {
            JSONObject gesuchteEinladung = new JSONObject();
            ClientServerHandler csh= new ClientServerHandler();
            JSONArray alleEinladungen=  csh.sendGETRequest("/controller/filmeinladung");

            for(int i=0; i<alleEinladungen.length();i++){
                JSONObject tmp=alleEinladungen.getJSONObject(i);
                if(ausstehendeEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("datum").toString())&&ausstehendeEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("nachricht").toString())&&ausstehendeEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("uhrzeit").toString())&&ausstehendeEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("vonNutzerEmail").toString())&&ausstehendeEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("film").toString())){
                    gesuchteEinladung=tmp;
                }
            }

            csh.sendDeleteRequest("/controller/filmeinladung/delete/"+gesuchteEinladung.get("id").toString());

            popUp.setAlertType(Alert.AlertType.INFORMATION);
            popUp.setHeaderText("Erfolg!");
            popUp.setContentText("Einladung abgelehnt");
            popUp.showAndWait();

            aktualisierenPressed();
        }

    }

    public void loeschenPressed() throws IOException, InterruptedException{
        if(angenommeneEinladungen.getSelectionModel().getSelectedItem()==null){
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte eine Einladung auswählen");
            popUp.showAndWait();
        }else {
            JSONObject gesuchteEinladung = new JSONObject();
            ClientServerHandler csh= new ClientServerHandler();
            JSONArray alleEinladungen=  csh.sendGETRequest("/controller/filmeinladung");

            for(int i=0; i<alleEinladungen.length();i++){
                JSONObject tmp=alleEinladungen.getJSONObject(i);
                if(angenommeneEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("datum").toString())&&angenommeneEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("nachricht").toString())&&angenommeneEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("uhrzeit").toString())&&angenommeneEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("vonNutzerEmail").toString())&&angenommeneEinladungen.getSelectionModel().getSelectedItem().toString().contains(tmp.get("film").toString())){
                    gesuchteEinladung=tmp;
                }
            }

            csh.sendDeleteRequest("/controller/filmeinladung/delete/"+gesuchteEinladung.get("id").toString());

            popUp.setAlertType(Alert.AlertType.INFORMATION);
            popUp.setHeaderText("Erfolg!");
            popUp.setContentText("Einladung erledigt");
            popUp.showAndWait();

            aktualisierenPressed();
        }
    }

    public void ausstehendeLaden() throws IOException {
        ausstehendeEinladungen.getItems().clear();
        ausstehendeListe.clear();

        ClientServerHandler csh= new ClientServerHandler();

        JSONArray ausstehend = csh.sendGETRequest("/controller/filmeinladung");

        for(int i=0; i< ausstehend.length();i++){
            JSONObject tmp= ausstehend.getJSONObject(i);
            if(tmp.get("angenommen").toString()=="false" && tmp.get("anNutzerEmail").toString().equals(AngemeldeterUser.getSpeicher().getEmail())){
                ausstehendeListe.add(tmp.get("vonNutzerEmail").toString() + " " + tmp.get("film").toString() + " " + tmp.get("datum").toString() + " " + tmp.get("uhrzeit").toString() + " " + tmp.get("nachricht").toString());
            }
        }

        ausstehendeEinladungen.getItems().addAll(ausstehendeListe);


    }

    public void angenommeneLaden() throws MalformedURLException, IOException{
        angenommeneEinladungen.getItems().clear();
        angenommeneListe.clear();

        ClientServerHandler csh= new ClientServerHandler();

        JSONArray angenommen = csh.sendGETRequest("/controller/filmeinladung");

        for(int i=0; i< angenommen.length();i++){
            JSONObject tmp= angenommen.getJSONObject(i);
            if((tmp.get("angenommen").toString()=="true" && tmp.get("anNutzerEmail").toString().equals(AngemeldeterUser.getSpeicher().getEmail()))|| (tmp.get("vonNutzerEmail").toString().equals(AngemeldeterUser.getSpeicher().getEmail()) && tmp.get("angenommen").toString()=="true") ){
                angenommeneListe.add(tmp.get("vonNutzerEmail").toString() + " " + tmp.get("film").toString() + " " + tmp.get("datum").toString() + " " + tmp.get("uhrzeit").toString() + " " + tmp.get("nachricht").toString());
            }
        }

        angenommeneEinladungen.getItems().addAll(angenommeneListe);
    }

    public void aktualisierenPressed() throws IOException, MalformedURLException{
        initialize();



    }
}
