package client.client.Soziales.Discussion;

import Controller.ClientServerHandler;
import client.client.login.AngemeldeterUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class Disc_CreateController {
    public Button startview;
    public Button back_Button;
    public TextField name_Field;
    public CheckBox isprivate_CheckBox;
    public Button createDiscussion_Button;
    String email=AngemeldeterUser.getSpeicher().getEmail();
    String name=AngemeldeterUser.getSpeicher().getVorname()+" "+AngemeldeterUser.getSpeicher().getNachname();

    private ClientServerHandler csh=new ClientServerHandler();

    public Disc_CreateController() throws MalformedURLException {
    }

    public void createDiscussion(ActionEvent actionEvent) throws IOException, InterruptedException {
        //zuerst die Fehler-Abfragen , damit der Code übersichtlicher ist ;)
        if(name_Field.getText()==null) {
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Fehlender Name!");
            alert.setContentText("Bitte gebe deiner Gruppe einen Namen!");
            alert.showAndWait();
            return;
        }
        if(!checkSpacesInName(name_Field.getText())) {
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Unleserlicher Name!");
            alert.setContentText("Der eingegebene Name enthält zu viele Leerzeichen und ist somit unleserlich!");
            alert.showAndWait();
            return;
        }

        String groupname=name_Field.getText();
        JSONObject discussion=new JSONObject();
        boolean isPrivate= isprivate_CheckBox.isSelected();

        JSONArray allDiscussionsPublic=csh.sendGETRequest("/controller/diskussion/gruppe/byprivacy/false");
        JSONArray allDiscussionsPrivate=csh.sendGETRequest("/controller/diskussion/gruppe/byprivacy/true");

        //Überprüfung des Namens
        //ist der Name !öffentlich! bereits vergeben?
        if(!isPrivate) {
            for (int i = 0; i < allDiscussionsPublic.length(); i++) {
                JSONObject found = allDiscussionsPublic.getJSONObject(i);
                if (found.get("name").equals(groupname)) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Error!");
                    alert.setContentText("Es existiert bereits ein öffentliches Forum mit diesem Namen!" +
                            "\nBitte wähle einen anderen Namen, um die Gruppen übersichtlich zu halten!");
                    alert.showAndWait();
                    return;
                }
            }
        }
        
        if(isPrivate) {
            //ist der Name !privat! vom Client schonmal vergeben worden?
            for (int i = 0; i < allDiscussionsPrivate.length(); i++) {
                JSONObject found = allDiscussionsPrivate.getJSONObject(i);
                if (found.get("name").toString().equals(groupname)
                        && found.get("creatorMail").toString().equals(email)) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Error!");
                    alert.setContentText("Du hast bereits ein privates Forum mit diesem Namen erstellt!" +
                            "\nBitte wähle einen anderen Namen, um die Gruppen übersichtlich zu halten!");
                    alert.showAndWait();
                    return;
                }
            }
        }
        //Gruppenname zulässig -> erstellt Gruppe
        discussion.put("name", groupname);
        discussion.put("isprivate", isPrivate);
        discussion.put("creatorMail", AngemeldeterUser.getSpeicher().getEmail());
        discussion.put("creatorName",name);
        csh.sendPostRequest("/controller/diskussion/gruppe/add", discussion);


        JSONArray createddiscussion=csh.sendGETRequest("/controller/diskussion/gruppe/" +
                "bySpecificGroupOhneName/"+isPrivate+"/"+email);

        Integer groupID=0;
        for(int i=0; i<createddiscussion.length(); i++){
            JSONObject found=createddiscussion.getJSONObject(i);
            if(groupname.equals(found.getString("name"))){
                groupID=found.getInt("id");
            }
        }
        //man sollte als Gruppenersteller auch automatisch ein Teilnehmer dieser sein
        JSONObject teilnehmer=new JSONObject();
        teilnehmer.put("groupID", groupID);
        teilnehmer.put("participant", email);
        csh.sendPostRequest("/controller/diskussion/teilnehmer/add",teilnehmer );


        JSONObject msgObj=new JSONObject();
        msgObj.put("participant", email);
        msgObj.put("participantFirstName", AngemeldeterUser.getSpeicher().getVorname().toUpperCase());
        msgObj.put("participantLastname", AngemeldeterUser.getSpeicher().getNachname().toUpperCase());
        msgObj.put("groupID", groupID);
        msgObj.put("content", "HAT DIE GRUPPE ERSTELLT.");
        csh.sendPostRequest("/controller/diskussion/nachrichten/add", msgObj);

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Erfolg!");
        alert.setContentText("Es wurde ein neues Diskussionsforum erstellt!");
        alert.showAndWait();

        back_View();
    }


    //Navigation
    public void back_View() throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_SearchView.fxml"));
        Stage stage = (Stage) back_Button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
    public void switchtostartview(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startview.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }


    //Hilfsmethoden

    //überprüft ob der name nur aus Leerzeichen besteht
    //dies ist nicht erlaubt, da der Name sonst unleserlich wäre
    public boolean checkSpacesInName(String name){
        int counter=0;
        for(int i=0; i<name.length(); i++){
            String tmp="" + name.charAt(i);
            if(tmp.equals(" ")) counter++;
        }
        return counter != name.length();
    }
}
