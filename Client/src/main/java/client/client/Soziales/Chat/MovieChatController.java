package client.client.Soziales.Chat;

import Controller.ClientServerHandler;
import Controller.JsonController;
import client.client.Soziales.Chat.entities.AusgewaehlterChat;
import client.client.Soziales.Chat.entities.ChatUser;
import client.client.login.AngemeldeterUser;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class MovieChatController {
    public ListView<String> message_view;
    public TextField message;
    public Button back_button;
    public Label participantname;
    public Button startview_button;
    public Button scroll_button;

    ClientServerHandler csh=new ClientServerHandler();
    Integer chatID=AusgewaehlterChat.getAusgewaehlterChat().getChatID();
    String loggedinUser=AngemeldeterUser.getSpeicher().getEmail();
    ChatUser participant=AusgewaehlterChat.getAusgewaehlterChat().getParticipant();

    String teilnehmerVorname=participant.getVorname();
    String teilnehmerNachname=participant.getNachname();
    String teilnehmerEmail=participant.getEmail();

    Task task;
    Thread th;

    //ob die ListView scrollen soll
    private boolean scroll = true;
    private boolean sendscroll=false;

    public MovieChatController() throws MalformedURLException {
        //für die Task und den Thread:
        //https://stackoverflow.com/questions/20497845/constantly-update-ui-in-java-fx-worker-thread
        task=new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while(true){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                update_messageview();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    //hier unten sleep, damit die Platform /der JavaFX nicht auch schläft und
                    // somit andere Eingaben blockiert
                    Thread.sleep(2000);
                }
                //return null;
            }
        };
        th=new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public void initialize() throws IOException {

        participantname.setText(teilnehmerVorname+" "+teilnehmerNachname);

        message_view.getItems().clear();
        JSONArray chatverlauf=csh.sendGETRequest("/controller/nachrichten/fromchat/"+chatID);

        if(chatverlauf.length()>0){
            for(int i=0; i<chatverlauf.length(); i++){
                JSONObject nachricht=chatverlauf.getJSONObject(i);
                String participantEmail=nachricht.get("participantEmail").toString();
                String content=nachricht.get("content").toString();

                if(participantEmail.equals(teilnehmerEmail)){
                    String otherMessage=teilnehmerNachname+": "+content;
                    message_view.getItems().add(otherMessage);
                }else{
                    String myMessage="Ich: "+content;
                    message_view.getItems().add(myMessage);
                }
            }
        }
        scroll=true;
        message_view.scrollTo(message_view.getItems().size());
    }

    public void send_message() throws IOException, InterruptedException {
        JSONObject messageobj=new JSONObject();
        messageobj.put("chatID", chatID);
        messageobj.put("participantEmail", loggedinUser);
        messageobj.put("content", message.getText().toString());
        csh.sendPostRequest("/controller/nachrichten/add", messageobj);

        message.clear();
        sendscroll=true;
    }

    //https://stackoverflow.com/questions/13880638/how-do-i-pick-up-the-enter-key-being-pressed-in-javafx2
    // Mittels Enter im TextFeld "message" die neue Nachricht versenden
    public void onEnter() throws IOException, InterruptedException {
        message.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    try {
                        send_message();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void back() throws IOException {
        th.stop();
        task.cancel();
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Chat_Choosing.fxml"));
        Stage stage = (Stage) back_button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void update_messageview() throws IOException {
        message_view.getItems().clear();
        JSONArray chatverlauf=csh.sendGETRequest("/controller/nachrichten/fromchat/"+chatID);

        if(chatverlauf.length()>0) {
            for (int i = 0; i < chatverlauf.length(); i++) {
                JSONObject nachricht = chatverlauf.getJSONObject(i);
                String participantEmail = nachricht.get("participantEmail").toString();
                String content = nachricht.get("content").toString();

                if (participantEmail.equals(teilnehmerEmail)) {
                    String otherMessage = teilnehmerNachname + ": " + content;
                    message_view.getItems().add(otherMessage);
                } else {
                    String myMessage = "Ich: " + content;
                    message_view.getItems().add(myMessage);
                }
            }
        }
        //Scrollt zur letzten Nachricht falls gewünscht, ist aber default
        if(scroll || sendscroll){
            message_view.scrollTo(message_view.getItems().size());
        }
        sendscroll=false;
    }

    //ob immer zum letzten Element gescrollt werden soll oder nicht
    public void scrollOnOff(){
        if(scroll){
            scroll=false;
            scroll_button.setText("Auto-Scroll is OFF");
        }else{
            scroll=true;
            scroll_button.setText("Auto-Scroll is ON");
        }
    }

    public void startview() throws IOException {
        th.stop();
        task.cancel();
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startview_button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }


    //Methode zum testen
    //für Datenbankanbindung
    //Date schicken und ob richtig angekommen später im Test
    public String testMethod(Integer chatID) throws IOException {
        //Strin gfür alle Nachrichten
        String totest="";

        //userEmail und chatID für den chat wo die nachricht hingeschickt wird
        JsonController jc=new JsonController();
        JSONObject chosenUser=jc.ueberpruefeLogin(AngemeldeterUser.getSpeicher().getEmail());
        Integer chosenChat=AusgewaehlterChat.getAusgewaehlterChat().getChatID();

        //Ausgaben für die Konsole
        if (chosenUser!=null){
            System.out.println("Nutzer wurde gefunden!");
        }
        if(chosenChat.equals(chatID)){
            System.out.println("Chat wurde gefunden!");
        }

        JSONArray msg=new JSONArray(csh.sendGETRequest("/controller/nachrichten/fromchat/"+chosenChat));

        for(int i=0; i<msg.length(); i++){
            JSONObject foundmsg=msg.getJSONObject(i);
            totest+= foundmsg.get("content").toString()+"\n";
        }
        return totest;


    }


}
