package client.client.Anzeigen;

import Controller.ClientServerHandler;
import client.client.Soziales.FremderUser;
import client.client.Statistik.StatistikModel;
import client.client.Zwischenspeicher;
import client.client.login.AngemeldeterUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class BewertungenAnzeigenController {


    @FXML
    public Button startseiteButton, titeleingabeButton, profilButton;

    public ListView<String> bewertungenListe;


    public Alert popUp = new Alert(Alert.AlertType.INFORMATION);
    public Text filmNameText;

    ClientServerHandler CSHandler = new ClientServerHandler();

    ObservableList bewertungen = FXCollections.observableArrayList();

    private JSONObject user = new JSONObject();

    private JSONArray alleUser = new JSONArray();


    public BewertungenAnzeigenController() throws IOException {

    }


    public void initialize() throws IOException {
        insertBewertungen();
    }

    public void insertBewertungen() throws IOException {

        bewertungenListe.getItems().clear();
        bewertungen.clear();

        alleUser = CSHandler.sendGETRequest("/controller/nutzer");

        JSONArray bewertungenarray = CSHandler.sendGETRequest("/controller/bewertung/" + Zwischenspeicher.getSpeicher().getFilmId());


        if (bewertungenarray == null || bewertungenarray.isEmpty()) {

            JSONArray filmarray = CSHandler.sendGETRequest("/controller/film/id/" + Zwischenspeicher.getSpeicher().getFilmId());
            JSONObject film = filmarray.getJSONObject(0);

                    String leer = "Es gibt noch keine Bewertungen";
                    System.out.println(leer);
                    bewertungen.add(leer);

        }

        JSONArray film = CSHandler.sendGETRequest("/controller/film/id/"+Zwischenspeicher.getSpeicher().getFilmId());
        filmNameText.setText(" Filmname: "+film.getJSONObject(0).get("name").toString());

        for (int i = 0; i < bewertungenarray.length(); i++) {
            JSONObject tmp = bewertungenarray.getJSONObject(i);
            JSONArray getSpecificUser = CSHandler.sendGETRequest("/controller/nutzer/email/"+tmp.get("nutzerEmail").toString());
            JSONObject nameNutzer = getSpecificUser.getJSONObject(0);
            bewertungen.add("Nutzer: "+tmp.get("nutzerEmail").toString()+", "+ nameNutzer.get("vorname").toString()+" " + nameNutzer.get("nachname").toString()+ " --- Sterne: " + tmp.get("sterne").toString() + " , Bewertungstext: " + tmp.get("bewertungsText").toString());

        }

        bewertungenListe.getItems().addAll(bewertungen);

    }


    public void startseitePressed() throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void zurueckPressed() throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/anzeigenlistview.fxml"));
        Stage stage = (Stage) titeleingabeButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }


    public void profilPressed(ActionEvent actionEvent) throws IOException {
        alleUser = CSHandler.sendGETRequest("/controller/nutzer");
        if (bewertungenListe.getSelectionModel().getSelectedItem() == null) {
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte Nutzer in Liste auswählen");
            popUp.showAndWait();
        } else {
            if (!bewertungenListe.getSelectionModel().getSelectedItem().equals("Keine User gefunden")) {
                String email = cropMail(bewertungenListe.getSelectionModel().getSelectedItem());
                System.out.println(email); //Zum checken der gecropten E-Mail
                if (email.equals("Es")) {
                    popUp.setAlertType(Alert.AlertType.ERROR);
                    popUp.setHeaderText("Fehler!");
                    popUp.setContentText("Zu dem Film gibs keine Bewertungen");
                    popUp.showAndWait();
                } else {
                    for (int i = 0; i < alleUser.length(); i++) {
                        JSONObject tmp = alleUser.getJSONObject(i);
                        if (tmp.get("email").toString().equalsIgnoreCase(email)) {
                            user = tmp;
                            //System.out.println(user.toString());
                        }
                    }
                    FremderUser.getSpeicher().setEmail(user.get("email").toString());
                    FremderUser.getSpeicher().setVorname(user.get("vorname").toString());
                    FremderUser.getSpeicher().setNachname(user.get("nachname").toString());
                    FremderUser.getSpeicher().setSichtbarkeit(user.get("sichtbarkeitsstufen").toString());
                    //System.out.println(FremderUser.getSpeicher().getSichtbarkeit().toString());

                    FXMLLoader laden = new FXMLLoader(getClass().getResource("/AndersProfil.fxml"));
                    Stage stage = (Stage) profilButton.getScene().getWindow();
                    Scene scene = new Scene(laden.load());
                    stage.setScene(scene);
                }
            } else {
                popUp.setAlertType(Alert.AlertType.ERROR);
                popUp.setHeaderText("Fehler!");
                popUp.setContentText("Bitte Film eingeben");
                popUp.showAndWait();
            }
        }
    }

    private String cropMail(String toCrop) {
        String email = "";
        int counter;

        for (int i = 8; i < toCrop.length(); i++) {
            if (String.valueOf(toCrop.charAt(i)).equals(",")) {
                break;
            }
            email = email + toCrop.charAt(i);
        }
        return email;
    }


}