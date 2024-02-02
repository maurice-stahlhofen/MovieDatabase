package client.client.Soziales.Einladungen;

import Controller.ClientServerHandler;
import client.client.Soziales.Einladungen.Entities.EinladungUser;
import client.client.login.AngemeldeterUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class EinladungenVerschickenController {
    public ListView<EinladungUser> ListView_invitation;
    public Button inviation_send_button;
    public Button backbutton;
    public Button startviewbutton;
    public Label Fehlermledung;

    ClientServerHandler csh=new ClientServerHandler();
    String clientemail= AngemeldeterUser.getSpeicher().getEmail();

    public EinladungenVerschickenController() throws MalformedURLException {
    }

    public void initialize() throws IOException {
        ListView_invitation.getItems().clear();
        JSONArray alleNutzer = csh.sendGETRequest("/controller/nutzer");
        for (int i = 0; i < alleNutzer.length(); i++) {
            JSONObject nutzer = alleNutzer.getJSONObject(i);
            if (!nutzer.get("email").equals(AngemeldeterUser.getSpeicher().getEmail())) {

                EinladungUser einladungUser=new EinladungUser();
                einladungUser.setVorname(nutzer.get("vorname").toString());
                einladungUser.setNachname(nutzer.get("nachname").toString());
                einladungUser.setEmail(nutzer.get("email").toString());

                ListView_invitation.getItems().add(einladungUser);
            }
        }
        if(ListView_invitation.getItems().size()==0){
            Fehlermledung.setText("Es wurden keine Nutzer gefunden!");
        }else{
            Fehlermledung.setText("");
        }
    }

    //verschickt Einladungen
    public void invitation_send(ActionEvent actionEvent) throws IOException, InterruptedException {
        if(ListView_invitation.getSelectionModel().getSelectedItem()!=null){
            EinladungUser einladungUser=ListView_invitation.getSelectionModel().getSelectedItem();
            String wanteduseremail=einladungUser.getEmail();

            //überprüft bereits existente Freundschaften
            JSONArray freundschaften=csh.sendGETRequest("/controller/freundesliste");
            if(freundschaften.length()>0){
                for(int i=0; i<freundschaften.length(); i++){
                    JSONObject freunde=freundschaften.getJSONObject(i);
                    String nutzerEmail=freunde.get("emailNutzer").toString();
                    String freundEmail=freunde.get("emailFreund").toString();

                    //gegenseitig die emails abchecken
                    if((clientemail.equals(nutzerEmail) && wanteduseremail.equals(freundEmail))
                    || wanteduseremail.equals(nutzerEmail) && clientemail.equals(freundEmail)){

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Fehler!");
                        alert.setContentText("Ihr seid bereits Freunde!");
                        alert.showAndWait();
                        return;
                    }
                }
            }

            //laedt alle existenten Einladungen
            JSONArray einladungen=csh.sendGETRequest("/controller/freundeseinladungen");
            if(einladungen.length()>0){
                boolean einladungexists=false;
                for(int i=0; i<einladungen.length(); i++){
                    JSONObject einladung=einladungen.getJSONObject(i);
                    String emailClient=einladung.get("emailNutzer").toString();
                    String emailFreund=einladung.get("emailFreund").toString();

                    //Überprüft, ob es eine Einladung schon gibt
                    //1.Fall: Client schickt erneut eine Einladung
                    if (emailClient.equals(clientemail) && emailFreund.equals(wanteduseremail)) {

                        einladungexists=true;

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Ups!");
                        alert.setContentText("Du hast diesem Nutzer bereits eine Freundeseinladung geschickt.\n" +
                                "Bitte wähle einen anderen Nutzer aus!");
                        alert.showAndWait();

                        //2.Fall: Client hat bereits eine Einladung von dem ausgewählten Nutzer
                        // bekommen, also sollte er diese auch anschauen,
                        // bevor er eine eigene verschickt
                    }else if((emailClient.equals(wanteduseremail) && emailFreund.equals(clientemail))){
                        einladungexists=true;

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Ups!");
                        alert.setContentText("Dieser Nutzer hat dir bereits eine Einladung geschickt!");
                        alert.showAndWait();

                    }
                }
                //Es existiert keine Einladung
                if(!einladungexists){
                    JSONObject neweinladung = new JSONObject();
                    neweinladung.put("emailNutzer", clientemail);
                    neweinladung.put("emailFreund", wanteduseremail);
                    csh.sendPostRequest("/controller/freundeseinladungen/add", neweinladung);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Erfolg!");
                    alert.setContentText("Die Einladung wurde verschickt!");
                    alert.showAndWait();

                }
                //es existieren noch überhaupt keine Einladungen
            }else{
                JSONObject neweinladung = new JSONObject();
                neweinladung.put("emailNutzer", clientemail);
                neweinladung.put("emailFreund", wanteduseremail);
                csh.sendPostRequest("/controller/freundeseinladungen/add", neweinladung);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Erfolg!");
                alert.setContentText("Die Einladung wurde verschickt!");
                alert.showAndWait();
            }

        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Ungültige Angabe!");
            alert.setContentText("Es wurde kein Nutzer ausgewählt, dem eine Einladung zugeschickt werden soll!");
            alert.showAndWait();
        }
    }
    public void aktualisieren() throws IOException {
        initialize();
    }



    public void back(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Freunde_EinladungenMenueView.fxml"));
        Stage stage = (Stage) backbutton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void startview(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startviewbutton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
}
