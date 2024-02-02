package client.client.Soziales.Discussion;

import Controller.ClientServerHandler;
import client.client.Soziales.Discussion.entities.Einladung;
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

public class Disc_HandleInviteController {

    public Button startButton, backButton, refreshButton, declineButton, acceptButton;
    public ListView<Einladung> ListView_invitation;
    public Label Fehlermeldung;
    String clientEmail= AngemeldeterUser.getSpeicher().getEmail();
    ClientServerHandler csh = new ClientServerHandler();

    public Disc_HandleInviteController() throws MalformedURLException {
    }

    public void startPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void backPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_MenueView.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void acceptPressed(ActionEvent actionEvent) throws IOException, InterruptedException {
        if(ListView_invitation.getSelectionModel().getSelectedItem()!=null){
            Einladung einladungUser1=ListView_invitation.getSelectionModel().getSelectedItem();

            JSONObject teilnahme=new JSONObject();
            JSONObject einladungtodelete=new JSONObject();
            //emailFreund bei der Einladung ist der Client, da dieser die Einladung ursprünglich bekommen hat
            //und somit als Freund diese annimmt
            teilnahme.put("groupID", einladungUser1.getGroupID());
            teilnahme.put("participant", einladungUser1.getRecipient());

            einladungtodelete.put("id", einladungUser1.getId());
            einladungtodelete.put("diskussionsId", einladungUser1.getGroupName());
            einladungtodelete.put("recipient", einladungUser1.getRecipient());
            einladungtodelete.put("sender", einladungUser1.getSender());

            csh.sendPostRequest("/controller/diskussion/teilnehmer/add", teilnahme);
            csh.sendDeleteRequest("/controller/diskussionseinladung/delete/"+ einladungUser1.getId());


            //Isabel: hinzugefügt für die Nachrichten im Chat
            JSONObject msgObj = new JSONObject();

            msgObj.put("participant", AngemeldeterUser.getSpeicher().getEmail());
            msgObj.put("participantFirstName", AngemeldeterUser.getSpeicher().getVorname().toUpperCase());
            msgObj.put("participantLastname", AngemeldeterUser.getSpeicher().getNachname().toUpperCase());
            msgObj.put("groupID", einladungUser1.getGroupID());
            msgObj.put("content", "IST BEIGETRETEN.");
            csh.sendPostRequest("/controller/diskussion/nachrichten/add", msgObj);

            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Einladung angenommen!");
            alert.setContentText("Du hast "
                    +einladungUser1.getSender()+"'s Einladung angenommen!");
            alert.showAndWait();

        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Falsche Auswahl!");
            alert.setContentText("Es wurde keine Einladung ausgewählt!");
            alert.showAndWait();
        }
        initialize();

    }

    public void declinePressed(ActionEvent actionEvent) throws IOException, InterruptedException {
        if(ListView_invitation.getSelectionModel().getSelectedItem()!=null){
            Einladung einladungUser2=ListView_invitation.getSelectionModel().getSelectedItem();
            Integer einladungsID=einladungUser2.getId();
            JSONObject diskussionseinladung=new JSONObject();
            diskussionseinladung.put("id", einladungUser2.getId());
            diskussionseinladung.put("sender", einladungUser2.getSender());
            diskussionseinladung.put("recipient", einladungUser2.getRecipient());
            diskussionseinladung.put("diskussionsID", einladungUser2.getGroupID());

            csh.sendDeleteRequest("/controller/diskussionseinladung/delete/"+ einladungUser2.getId());

            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Einladung abgelehnt!");
            alert.setContentText("Du hast "+ einladungUser2.getSender()+
                    "'s Einladung abgelehnt!");
            alert.showAndWait();

            initialize();
        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Falsche Auswahl!");
            alert.setContentText("Es wurde keine Einladung ausgewählt!");
            alert.showAndWait();
        }
        initialize();
    }

    public void refreshPressed(ActionEvent actionEvent) throws IOException {
        initialize();
    }

    public void initialize() throws IOException {
        ListView_invitation.getItems().clear();
        JSONArray allinvitations=csh.sendGETRequest("/controller/diskussionseinladung");

        for(int i=0; i<allinvitations.length();i++){
            JSONObject einladung=allinvitations.getJSONObject(i);

            //sammelt nur die Einladungen, die von anderen ans uns geschickt wurden
            //deswegen die if-Abfrage auf client=freund, dem die Anfrage geschickt wurde!
            if(clientEmail.equals(einladung.get("recipient").toString())){
                String emailSender=einladung.get("sender").toString();

                Integer groupID=einladung.getInt("diskussionsID");
                JSONObject sender=csh.sendGETRequest("/controller/nutzer/email/"+emailSender).getJSONObject(0);

                Einladung einladung1=new Einladung();
                einladung1.setGroupID(einladung.getInt("diskussionsID"));
                einladung1.setSender(einladung.getString("sender"));
                einladung1.setRecipient(einladung.getString("recipient"));
                einladung1.setId(einladung.getInt("id"));

                JSONArray groups=csh.sendGETRequest("/controller/diskussion/gruppe/filteredGroupsByID/"+einladung1.getGroupID());
                einladung1.setGroupName(groups.getJSONObject(0).getString("name"));

                ListView_invitation.getItems().add(einladung1);
                break;
            }
        }
    }
}
