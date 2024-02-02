package client.client.Soziales.Discussion;

import Controller.ClientServerHandler;
import client.client.Soziales.Discussion.entities.ChosenDiscussion;
import client.client.login.AngemeldeterUser;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class Disc_ChatController {
    public ListView<String> message_view;
    public Label groupname=new Label();
    public ImageView icon;
    public TextField message;
    public Button startview_button;
    public Button back_button;
    public Button scroll_button;

    private ClientServerHandler csh=new ClientServerHandler();
    Integer groupID=ChosenDiscussion.getChosenDiscussion().getID();
    String email= AngemeldeterUser.getSpeicher().getEmail();

    Task task;
    Thread th;

    private boolean scroll=true;
    private boolean sendscroll=false;


    public Disc_ChatController() throws MalformedURLException {
        //aus MovieChatController.java:

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
                                initialize();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    //hier unten sleep, damit der JavaFX-Thread nicht schläft/blockiert wird
                    Thread.sleep(2000);
                }
            }
        };
        th=new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public void initialize() throws IOException {
        groupname.setText(ChosenDiscussion.getChosenDiscussion().getName());

        message_view.getItems().clear();
        JSONArray msgVerlauf=csh.sendGETRequest("/controller/diskussion/nachrichten/fromgroup/"+groupID);

        for(int i=0; i<msgVerlauf.length(); i++){
            JSONObject foundMsg=msgVerlauf.getJSONObject(i);
            String content=foundMsg.getString("content");

            if(email.equals(foundMsg.get("participant"))){
                String myMsg="Ich: "+content;
                message_view.getItems().add(myMsg);
            }else{
                String participantfullname=foundMsg.get("participantFirstName").toString()+" "+foundMsg.get("participantLastname").toString();
                String otherMsg=participantfullname+": "+content;
                message_view.getItems().add(otherMsg);
            }
        }
        if(scroll || sendscroll){
            message_view.scrollTo(message_view.getItems().size());
        }
        sendscroll=false;
    }

    //https://stackoverflow.com/questions/13880638/how-do-i-pick-up-the-enter-key-being-pressed-in-javafx2
    // Mittels Enter im TextFeld "message" die neue Nachricht versenden
    public void onEnter(ActionEvent actionEvent) {
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

    public void send_message() throws IOException, InterruptedException {
        JSONObject msgObj=new JSONObject();

        msgObj.put("participant", email);
        msgObj.put("participantFirstName", AngemeldeterUser.getSpeicher().getVorname());
        msgObj.put("participantLastname", AngemeldeterUser.getSpeicher().getNachname());
        msgObj.put("groupID", groupID);
        msgObj.put("content", message.getText().toString());
        csh.sendPostRequest("/controller/diskussion/nachrichten/add", msgObj);

        message.clear();
        sendscroll=true;
    }

    //Navigation
    public void startview(ActionEvent actionEvent) throws IOException {
        th.stop();
        task.cancel();
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startview_button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
    public void back(ActionEvent actionEvent) throws IOException {
        th.stop();
        task.cancel();
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_OpenView.fxml"));
        Stage stage = (Stage) back_button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }


    //Hilfsmethoden:
    public void scrollOnOff(ActionEvent actionEvent) {
        if(scroll){
            scroll=false;
            scroll_button.setText("Auto-Scroll is OFF");
        }else{
            scroll=true;
            scroll_button.setText("Auto-Scroll is ON");
        }
    }
}
