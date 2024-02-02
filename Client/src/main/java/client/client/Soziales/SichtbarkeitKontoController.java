package client.client.Soziales;

import Controller.ClientServerHandler;
import Controller.JsonController;
import client.client.login.AngemeldeterUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class SichtbarkeitKontoController {

    public Button backButton;
    public RadioButton meButton;
    public RadioButton friendButton;
    public RadioButton allButton;
    public Button saveButton;
    public Text invalidSave;

    public ToggleGroup group = new ToggleGroup();

    Alert popUp = new Alert(Alert.AlertType.CONFIRMATION);

    JSONObject angemeldeterUser = new JSONObject();
    JSONArray alleNutzer = new JSONArray();

    ClientServerHandler handler = new ClientServerHandler();

    JsonController jsc = new JsonController();

    public SichtbarkeitKontoController() throws MalformedURLException {
    }

    public void setJustMe(ActionEvent actionEvent) {
        meButton.setToggleGroup(group);
    }

    public void setFriends(ActionEvent actionEvent) {
        friendButton.setToggleGroup(group);
    }

    public void setAll(ActionEvent actionEvent) {
        allButton.setToggleGroup(group);
    }

    public void saveSettings(ActionEvent actionEvent) throws IOException, InterruptedException {

        alleNutzer = handler.sendGETRequest("/controller/nutzer");
        for(int i = 0; i<alleNutzer.length(); i++){
            JSONObject temp = alleNutzer.getJSONObject(i);
            if(temp.get("email").toString().equals(AngemeldeterUser.getSpeicher().getEmail())){
                this.angemeldeterUser=temp;
            }
        }

        if(meButton.isSelected()){

            angemeldeterUser.put("sichtbarkeitsstufen", "0");
            handler.sendPutRequest("/controller/nutzer/aendern", angemeldeterUser);

            popUp.setHeaderText("Erfolgreich!");
            popUp.setContentText("Sichtbarkeit wurde geändert");
            popUp.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Nutzeruebersicht.fxml"));
            Stage stg = (Stage) saveButton.getScene().getWindow();
            Scene sc = new Scene(loader.load());
            stg.setScene(sc);
        }

        if(allButton.isSelected()){

            angemeldeterUser.put("sichtbarkeitsstufen", "2");
            handler.sendPutRequest("/controller/nutzer/aendern", angemeldeterUser);

            popUp.setHeaderText("Erfolgreich!");
            popUp.setContentText("Sichtbarkeit wurde geändert");
            popUp.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Nutzeruebersicht.fxml"));
            Stage stg = (Stage) saveButton.getScene().getWindow();
            Scene sc = new Scene(loader.load());
            stg.setScene(sc);
        }

        if(friendButton.isSelected()){

            angemeldeterUser.put("sichtbarkeitsstufen", "1");
            handler.sendPutRequest("/controller/nutzer/aendern", angemeldeterUser);

            popUp.setHeaderText("Erfolgreich!");
            popUp.setContentText("Sichtbarkeit wurde geändert");
            popUp.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Nutzeruebersicht.fxml"));
            Stage stg = (Stage) saveButton.getScene().getWindow();
            Scene sc = new Scene(loader.load());
            stg.setScene(sc);
        }
        else{
            invalidSave.setText("Bitte Auswahl treffen");
        }
    }


    public void backWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Nutzeruebersicht.fxml"));
        Stage stg = (Stage) backButton.getScene().getWindow();
        Scene sc = new Scene(loader.load());
        stg.setScene(sc);
    }
}
