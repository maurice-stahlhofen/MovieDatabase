package client.client.Soziales.Discussion;

import Controller.ClientServerHandler;
import client.client.Soziales.Discussion.entities.ExistingDiscussions;
import client.client.login.AngemeldeterUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Disc_SearchController {
    public Button joinDiscussion_Button;
    public Button startview;
    public ListView<ExistingDiscussions> discussion_list;
    public Button createDiscussion_Button;
    public TextField nameSearch_Field;
    public Button back_Button;
    public Label Fehlermeldung;

    private ClientServerHandler csh;
    private List<String> freunde=new ArrayList<>();

    //Parameter werden fürs Testen gebraucht!
    public String email=AngemeldeterUser.getSpeicher().getEmail();
    public boolean wirdgetestet;
    public JSONArray allowedDiscussions=new JSONArray();
    public StringBuilder searchTest;

    public Disc_SearchController() throws IOException {
        csh=new ClientServerHandler();
        JSONArray allefreunde=csh.sendGETRequest("/controller/freundesliste/byEmail/"+email);
        for(int i=0; i<allefreunde.length(); i++){
            freunde.add(allefreunde.getJSONObject(i).get("emailFreund").toString());
        }
        wirdgetestet=false;
    }

    //Private Anzeige gerne nochmal überprüfen
    public void initialize() throws IOException {
        //If-Abfragen, damit der Modultest keine JavaFX-Fehler erzeugt
        if(!wirdgetestet){
            allowedDiscussions.clear();
            discussion_list.getItems().clear();
        }

        allowedDiscussions=csh.sendGETRequest("/controller/diskussion/gruppe/filteredGroupsByName/"+email);

        for(int i=0; i<allowedDiscussions.length(); i++){
            JSONObject diskussion=allowedDiscussions.getJSONObject(i);

            ExistingDiscussions neu = new ExistingDiscussions();
            neu.setCreatorMail(diskussion.get("creatorMail").toString());
            neu.setCreatorName(diskussion.get("creatorName").toString());
            neu.setID(Integer.parseInt(diskussion.get("id").toString()));
            neu.setName(diskussion.get("name").toString());
            neu.setPrivate(Boolean.parseBoolean(diskussion.get("isprivate").toString()));
            neu.initializeTeilnehmerAnzahl();

            if(!wirdgetestet) discussion_list.getItems().add(neu);
        }


        //Marco: Zeigt die privaten Diskussionsgruppen von Freunden an
        JSONArray alleDiskussionen = csh.sendGETRequest("/controller/diskussion/gruppe");

        JSONArray bereitsMitglied=csh.sendGETRequest("/controller/diskussion/teilnehmer/findByParticipant/"+AngemeldeterUser.getSpeicher().getEmail());
        // test test
        for (int i = 0; i < alleDiskussionen.length(); i++) {
            JSONObject diskussion = alleDiskussionen.getJSONObject(i);

            //Isabel: alle nicht-privaten Gruppen werden übersprungen, da diese
            //bereits oben geladen werden
            if(!Boolean.parseBoolean(diskussion.get("isprivate").toString())){
                continue;
            }
            ExistingDiscussions neu = new ExistingDiscussions();
            neu.setCreatorMail(diskussion.get("creatorMail").toString());
            neu.setCreatorName(diskussion.get("creatorName").toString());
            neu.setID(Integer.parseInt(diskussion.get("id").toString()));
            neu.setName(diskussion.get("name").toString());
            neu.setPrivate(Boolean.parseBoolean(diskussion.get("isprivate").toString()));

            boolean teilnehmer=false;
            for(int j=0; j<bereitsMitglied.length(); j++){
                Integer vorhandeneID=bereitsMitglied.getJSONObject(j).getInt("groupID");
                //beide ids stimmen überein, also ist man bereits mitlgied
                if(neu.getID().equals(vorhandeneID)){
                    teilnehmer=true;
                    break;
                }
            }
            //beigetretene Gruppen rausfiltern
            if(!teilnehmer){
                if (neu.isPrivate()) {

                    //Isabel: initializeTeilnehmerAnzahl lieber hier unten, da ansonsten für ALLE Gruppen
                    //         die Methode ausgeführt wird
                    neu.initializeTeilnehmerAnzahl();
                    discussion_list.getItems().add(neu);
                }
                //Isabel: else ist nicht nötig, da oben bereits die öffentlichen Gruppen geladen werden

                /*else {
                    if(checkFriends(diskussion.get("creatorMail").toString())
                            || email.equals(diskussion.get("creatorMail").toString())){
                        discussion_list.getItems().add(neu);
                    }
                }
                 */
            }
        }

        if(discussion_list.getItems().size()==0){
            Fehlermeldung.setText("Keine Diskussionsforen gefunden!");
        }else{
            Fehlermeldung.setText("");
        }
    }

    //Umstrukturierung zugunsten des Modultests
    public void joinDiscussion() throws IOException, InterruptedException {
        if(discussion_list.getSelectionModel().getSelectedItem()!=null){
            ExistingDiscussions selected=discussion_list.getSelectionModel().getSelectedItem();
            joinDisc(selected.getID());
        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Error!");
            alert.setContentText("Es wurde kein Diskussionsforum ausgewählt!");
            alert.showAndWait();
        }
    }

    //Methode für Modultest ausgelagert, um JavaFx-Elemente zu umgehen
    public void joinDisc(Integer id) throws IOException, InterruptedException {
            JSONArray alleTeilnehmer=csh.sendGETRequest("/controller/diskussion/teilnehmer");
            //überprüft, ob man schon in dem Diskussionsforum ist
            for(int i=0; i<alleTeilnehmer.length(); i++){
                JSONObject found=alleTeilnehmer.getJSONObject(i);
                Integer groupID=Integer.parseInt(found.get("groupID").toString());


                //dieser Fall sollte nicht mehr auftreten, da die Gruppenliste gefiltert wird
                //sollte man im Forum bereits Mitlgied sein
                if(groupID.equals(id) && found.get("participant").toString().equals(email)){
                    Alert alert=new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Error!");
                    alert.setContentText("Du bist bereits Mitglied in diesem Diskussionsforum!");
                    alert.showAndWait();
                    return;
                }
            }
            //noch kein Mitglied
            JSONObject teilnehmerschaft=new JSONObject();
            teilnehmerschaft.put("groupID", id);
            teilnehmerschaft.put("participant", email);
            csh.sendPostRequest("/controller/diskussion/teilnehmer/add", teilnehmerschaft);

        if (!wirdgetestet) {
            JSONObject msgObj = new JSONObject();

            msgObj.put("participant", email);
            msgObj.put("participantFirstName", AngemeldeterUser.getSpeicher().getVorname().toUpperCase());
            msgObj.put("participantLastname", AngemeldeterUser.getSpeicher().getNachname().toUpperCase());
            msgObj.put("groupID", id);
            msgObj.put("content", "IST BEIGETRETEN.");
            csh.sendPostRequest("/controller/diskussion/nachrichten/add", msgObj);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Erfolg!");
            alert.setContentText("Du bist jetzt Mitglied in diesem Diskussionsforum!");
            alert.showAndWait();
        }
            initialize();
        }

    public void search_discussion(ActionEvent actionEvent) throws IOException {
        discussion_list.getItems().clear();
        String search=nameSearch_Field.getText();
        searchDisc(search);

        if(discussion_list.getItems().size()==0){
            Fehlermeldung.setText("Keine Diskussionsforen gefunden!");
        }else{
            Fehlermeldung.setText("");
        }
    }
    public void searchDisc(String search) throws IOException {
        searchTest=new StringBuilder();
        for(int i=0; i<allowedDiscussions.length(); i++){
            JSONObject diskussion=allowedDiscussions.getJSONObject(i);
            String tosearch=diskussion.get("name").toString();
            if(tosearch.contains(search)){
                //wie oben neue Entität erstellen
                ExistingDiscussions neu = new ExistingDiscussions();
                neu.setCreatorMail(diskussion.get("creatorMail").toString());
                neu.setCreatorName(diskussion.get("creatorName").toString());
                neu.setID(Integer.parseInt(diskussion.get("id").toString()));
                neu.setName(diskussion.get("name").toString());
                neu.setPrivate(Boolean.parseBoolean(diskussion.get("isprivate").toString()));
                neu.initializeTeilnehmerAnzahl();

               if(!wirdgetestet){
                   discussion_list.getItems().add(neu);
               }else{
                   searchTest.append(neu).append("; ");
               }
            }
        }
    }

    public void createDiscussion_View(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_CreateView.fxml"));
        Stage stage = (Stage) createDiscussion_Button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void back_View(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_MenueView.fxml"));
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

    public boolean checkFriends(String creatorMail){
        for (String s : freunde) {
            if (creatorMail.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
