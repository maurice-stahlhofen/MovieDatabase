package client.client.Soziales.Einladungen;

import Controller.ClientServerHandler;
import client.client.Soziales.Einladungen.Entities.EinladungUser;
import client.client.login.AngemeldeterUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class EinladungVerwaltungController {
    public ListView<EinladungUser> ListView_invitation;
   // public ComboBox<EinladungUser> invitation_combobox;
    public Button inviation_yes_button;
    public Button invitation_no_button;
    public Button backbutton;
    public Button startviewbutton;
    public Label Fehlermeldung;


    String clientEmail= AngemeldeterUser.getSpeicher().getEmail();
    ClientServerHandler csh=new ClientServerHandler();

    public EinladungVerwaltungController() throws MalformedURLException {
    }

    public void initialize() throws IOException {
        ListView_invitation.getItems().clear();
        JSONArray allinvitations=csh.sendGETRequest("/controller/freundeseinladungen");

        for(int i=0; i<allinvitations.length();i++){
            JSONObject einladung=allinvitations.getJSONObject(i);

            //sammelt nur die Einladungen, die von anderen ans uns geschickt wurden
            //deswegen die if-Abfrage auf client=freund, dem die Anfrage geschickt wurde!
            if(clientEmail.equals(einladung.get("emailFreund").toString())){
                String emailSender=einladung.get("emailNutzer").toString();

                Integer einladungsID=Integer.parseInt(einladung.get("id").toString());
                JSONObject sender=csh.sendGETRequest("/controller/nutzer/email/"+emailSender).getJSONObject(0);
                EinladungUser einladungUser=new EinladungUser();
                einladungUser.setEmail(sender.get("email").toString());
                einladungUser.setVorname(sender.get("vorname").toString());
                einladungUser.setNachname(sender.get("nachname").toString());
                einladungUser.setEinladungID(einladungsID);
                ListView_invitation.getItems().add(einladungUser);
            }
        }
        if(ListView_invitation.getItems().size()==0){
            Fehlermeldung.setText("Es wurden keine Einladungen gefunden!");
        }else{
            Fehlermeldung.setText("");
        }
    }

    public void invitation_yes(ActionEvent actionEvent) throws IOException, InterruptedException {
        if(ListView_invitation.getSelectionModel().getSelectedItem()!=null){
            EinladungUser einladungUser1=ListView_invitation.getSelectionModel().getSelectedItem();
            String freundEmail=einladungUser1.getEmail();
            Integer einladungsID=einladungUser1.getEinladungID();

            JSONObject freundschaft=new JSONObject();
            JSONObject reverseFreundschaft=new JSONObject();
            //emailFreund bei der Einladung ist der Client, da dieser die Einladung ursprünglich bekommen hat
            //und somit als Freund diese annimmt
            freundschaft.put("emailNutzer", freundEmail);
            freundschaft.put("emailFreund", clientEmail);

            reverseFreundschaft.put("emailNutzer", clientEmail);
            reverseFreundschaft.put("emailFreund", freundEmail);
            
            csh.sendPostRequest("/controller/freundesliste/add", freundschaft);
            csh.sendPostRequest("/controller/freundesliste/add", reverseFreundschaft);

            csh.sendDeleteRequest("/controller/freundeseinladungen/delete/"+einladungsID);

            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Einladung angenommen!");
            alert.setContentText("Du hast "
                    +einladungUser1.getVorname()+" "+einladungUser1.getNachname()+
                    "'s Einladung angenommen!");
            alert.showAndWait();

        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Falsche Auswahl!");
            alert.setContentText("Es wurde keine Einladung ausgewählt!");
            alert.showAndWait();
        }
        initialize();

    }

    public void invitation_no(ActionEvent actionEvent) throws IOException, InterruptedException {
        if(ListView_invitation.getSelectionModel().getSelectedItem()!=null){
            EinladungUser einladungUser2=ListView_invitation.getSelectionModel().getSelectedItem();
            Integer einladungsID=einladungUser2.getEinladungID();

            csh.sendDeleteRequest("/controller/freundeseinladungen/delete/"+einladungsID);

            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Einladung abgelehnt!");
            alert.setContentText("Du hast "
                    +einladungUser2.getVorname()+" "+einladungUser2.getNachname()+
                    "'s Einladung abgelehnt!");
            alert.showAndWait();
        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Falsche Auswahl!");
            alert.setContentText("Es wurde keine Einladung ausgewählt!");
            alert.showAndWait();
        }
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
        Stage stage = (Stage)startviewbutton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void aktualisieren(ActionEvent actionEvent) throws IOException {
        initialize();
    }
}
