package client.client.Soziales.Chat;

import Controller.ClientServerHandler;
import client.client.Soziales.Chat.entities.ChatUser;
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

public class OpenNewChatController {


    public ListView<ChatUser> userlist;
    public Button newchat_button;
    public Button startview;
    public Button back_button;
    public Button reload_button;
    public Label Fehlermeldung;

    private ClientServerHandler csh=new ClientServerHandler();
    String clientemail = AngemeldeterUser.getSpeicher().getEmail();

    public OpenNewChatController() throws MalformedURLException {
    }

    public void newchat(ActionEvent actionEvent) throws IOException, InterruptedException {
            if(userlist.getSelectionModel().getSelectedItem()!=null){
            ChatUser chatUser=userlist.getSelectionModel().getSelectedItem();
            String wanteduseremail = chatUser.getEmail();

            JSONArray chats = csh.sendGETRequest("/controller/chat");
            if (chats.length() > 0) {
                boolean chatalreadyexists=false;

                for (int i = 0; i < chats.length(); i++) {
                    JSONObject chat = chats.getJSONObject(i);
                    String email1 = chat.get("participant1Email").toString();
                    String email2 = chat.get("participant2Email").toString();

                    if ((email1.equals(clientemail) && email2.equals(wanteduseremail))
                            || (email1.equals(wanteduseremail) && email2.equals(clientemail))) {

                        chatalreadyexists=true;

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Ups!");
                        alert.setContentText("Es existiert bereits ein Chat mit dem ausgewählten Nutzer! Bitte wähle einen anderen Nutzer für einen neuen Chat aus!");
                        alert.showAndWait();
                        initialize();
                        break;
                    }
                }
                if(!chatalreadyexists){
                    JSONObject newchat = new JSONObject();
                    newchat.put("participant1Email", clientemail);
                    newchat.put("participant2Email", wanteduseremail);
                    csh.sendPostRequest("/controller/chat/add", newchat);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Erfolg!");
                    alert.setContentText("Es wurde ein neuer Chat angelegt!");
                    alert.showAndWait();

                    initialize();
                }

            }else{
                JSONObject newchat = new JSONObject();
                newchat.put("participant1Email", clientemail);
                newchat.put("participant2Email", wanteduseremail);
                csh.sendPostRequest("/controller/chat/add", newchat);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Erfolg!");
                alert.setContentText("Es wurde ein neuer Chat angelegt!");
                alert.showAndWait();

                initialize();
            }
        }else{
           Alert alert=new Alert(Alert.AlertType.WARNING);
           alert.setHeaderText("Ungültige Angabe!");
           alert.setContentText("Es wurde kein Nutzer zum Chatten ausgewählt!");
           alert.showAndWait();

            initialize();
        }
    }
    public void aktualisieren(ActionEvent actionEvent) throws IOException {
        initialize();
    }

    public void switchtostartview(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startview.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void initialize() throws IOException {
        userlist.getItems().clear();
        JSONArray alleNutzer=csh.sendGETRequest("/controller/nutzer");
        /*
        idee: zwei listen, welche beide eine temporäre id
        für die angezeigten nutzer besitzen und anhand deren
        dann der nutzer ausgewählt wird, damit man nicht die email des
        anderen nutzers sieht
        (nur wenn nötig, ansonsten bleibt die email sichtbar)
         */
        for(int i=0; i< alleNutzer.length(); i++){
            JSONObject nutzer=alleNutzer.getJSONObject(i);
            if(!nutzer.get("email").equals(AngemeldeterUser.getSpeicher().getEmail())){

                ChatUser chatUser =new ChatUser();
                chatUser.setVorname(nutzer.get("vorname").toString());
                chatUser.setNachname(nutzer.get("nachname").toString());
                chatUser.setEmail(nutzer.get("email").toString());

                userlist.getItems().add(chatUser);
            }
        }
        if(userlist.getItems().size()==0){
            Fehlermeldung.setText("Es wurden keine Nutzer gefunden!");
        }else{
            Fehlermeldung.setText("");
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Chat_MenueView.fxml"));
        Stage stage = (Stage) back_button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
}
