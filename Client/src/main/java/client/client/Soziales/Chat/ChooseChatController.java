package client.client.Soziales.Chat;

import Controller.ClientServerHandler;
import client.client.Soziales.Chat.entities.AusgewaehlterChat;
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

public class ChooseChatController {

    public Button openchatbutton;
    public Button backbutton;
    public Button startviewbutton;
    public ListView<ChatUser> chatlist;
    public Button reload_button;
    public Label Fehlermeldung;

    String nutzerEmail= AngemeldeterUser.getSpeicher().getEmail();

    ClientServerHandler csh=new ClientServerHandler();

    public ChooseChatController() throws MalformedURLException {
    }


    public void openchat(ActionEvent actionEvent) throws IOException{
        if(chatlist.getSelectionModel().getSelectedItem()!=null){
            ChatUser chatUser =chatlist.getSelectionModel().getSelectedItem();
            Integer chatid= chatUser.getChatID();
            AusgewaehlterChat.getAusgewaehlterChat().setChatID(chatid);
            AusgewaehlterChat.getAusgewaehlterChat().setParticipant(chatUser);

            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Erfolg!");
            alert.setContentText("Der Chat wurde erfolgreich ausgewählt!");
            alert.showAndWait();

            FXMLLoader laden = new FXMLLoader(getClass().getResource("/Chat_Movie-Chat.fxml"));
            Stage stage = (Stage) openchatbutton.getScene().getWindow();
            Scene scene = new Scene(laden.load());
            stage.setScene(scene);


        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Ungültige Angabe!");
            alert.setContentText("Es wurde kein Chat ausgewählt!\n" +
                    "Wenn kein Chat zur Auswahl stand, lege bitte einen an!");
            alert.showAndWait();
        }

    }


    public void initialize() throws IOException {
        chatlist.getItems().clear();
        JSONArray allchats=csh.sendGETRequest("/controller/chat");

        for(int i=0; i<allchats.length();i++){
            JSONObject chat=allchats.getJSONObject(i);
            String participant1Email=chat.get("participant1Email").toString();
            String participant2Email=chat.get("participant2Email").toString();

            if(nutzerEmail.equals(participant1Email)){
                Integer chatID=Integer.parseInt(chat.get("id").toString());
                JSONObject andererNutzer=csh.sendGETRequest("/controller/nutzer/email/"+participant2Email).getJSONObject(0);
                ChatUser chatUser =new ChatUser();
                chatUser.setEmail(andererNutzer.get("email").toString());
                chatUser.setVorname(andererNutzer.get("vorname").toString());
                chatUser.setNachname(andererNutzer.get("nachname").toString());
                chatUser.setChatID(chatID);
                chatlist.getItems().add(chatUser);
            }
            else if(nutzerEmail.equals(participant2Email)){
                Integer chatID=Integer.parseInt(chat.get("id").toString());
                JSONObject andererNutzer=csh.sendGETRequest("/controller/nutzer/email/"+participant1Email).getJSONObject(0);
                ChatUser chatUser =new ChatUser();
                chatUser.setEmail(andererNutzer.get("email").toString());
                chatUser.setVorname(andererNutzer.get("vorname").toString());
                chatUser.setNachname(andererNutzer.get("nachname").toString());
                chatUser.setChatID(chatID);
                chatlist.getItems().add(chatUser);
            }
        }
        if(chatlist.getItems().size()==0){
            Fehlermeldung.setText("Es wurden keine Chats gefunden!");
        }else{
            Fehlermeldung.setText("");
        }
    }
    public void aktualisieren(ActionEvent actionEvent) throws IOException {
        initialize();
    }


    public void back(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Chat_MenueView.fxml"));
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
